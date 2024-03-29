package com.hocine.fotoshare.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hocine.fotoshare.Fragment.PostDetailFragment;
import com.hocine.fotoshare.Fragment.ProfileFragment;
import com.hocine.fotoshare.Model.Notification;
import com.hocine.fotoshare.Model.Post;
import com.hocine.fotoshare.Model.User;
import com.hocine.fotoshare.R;

import java.util.List;

/**
 * Classe permettant la gestion de la partie Notification
 *
 * @author Hocine
 * @version 1.0
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    /**
     * Attribut de la classe NotificationAdapter
     */
    private Context mContext;
    private List<Notification> mNotification;

    /**
     * Constructeur par initialisation
     *
     * @param mContext
     * @param mNotification
     */
    public NotificationAdapter(Context mContext, List<Notification> mNotification) {
        this.mContext = mContext;
        this.mNotification = mNotification;
    }

    /**
     * Méthode permettant d'implémenter le fichier xml notifications_item.xml
     *
     * @param viewGroup
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notification_item, viewGroup, false);
        return new NotificationAdapter.ViewHolder(view);
    }

    /**
     * Méthode permettant d'afficher les notification si cette dernière a été postée
     *
     * @param viewHolder
     * @param i
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Notification notification = mNotification.get(i);
        viewHolder.text.setText(notification.getText());
        getUserInfo(viewHolder.image_profile, viewHolder.prenom, notification.getUserid());
        // Vérifie si la notification a été postée
        if (notification.isIspost()) {
            viewHolder.post_image.setVisibility(View.VISIBLE);
            getPostImage(viewHolder.post_image, notification.getPostid());
        } else {
            viewHolder.post_image.setVisibility(View.GONE);
        }
        // Mise en place d'un sharedPreferences
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (notification.isIspost()) {
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                    editor.putString("postid", notification.getPostid());
                    editor.apply();
                    ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PostDetailFragment()).commit();
                } else {
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                    editor.putString("profileid", notification.getUserid());
                    editor.apply();
                    ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                }
            }
        });
    }

    /**
     * Méthode permettant de récuperer le nombre de notification total
     * Retourne le nombre d'élements de la liste mNotification
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return mNotification.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image_profile, post_image;
        public TextView prenom, text;

        /**
         * Récupère les éléments du fichier xml notification_item.xml
         *
         * @param itemView
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_profile = itemView.findViewById(R.id.image_profile);
            post_image = itemView.findViewById(R.id.post_image);
            prenom = itemView.findViewById(R.id.prenom);
            text = itemView.findViewById(R.id.comment);
        }
    }

    /**
     * Méthode permettant de récuperer les infos de l'utilisateur
     *
     * @param imageView
     * @param prenom
     * @param publisherid
     */
    private void getUserInfo(final ImageView imageView, final TextView prenom, String publisherid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(publisherid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Glide.with(mContext).load(user.getImageurl()).into(imageView);
                prenom.setText(user.getPrenom());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Méthode  permettant de récuperer l'image qui a été commenté ou liké
     *
     * @param imageView
     * @param postid
     */
    private void getPostImage(ImageView imageView, String postid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts").child(postid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
                Glide.with(mContext).load(post.getPostimage()).into(imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
