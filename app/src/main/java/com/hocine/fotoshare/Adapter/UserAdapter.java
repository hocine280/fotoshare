package com.hocine.fotoshare.Adapter;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hocine.fotoshare.CommentsActivity;
import com.hocine.fotoshare.Fragment.ProfileFragment;
import com.hocine.fotoshare.MainActivity;
import com.hocine.fotoshare.Model.User;
import com.hocine.fotoshare.R;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Classe permettant de gérer un compte utilisateur
 *
 * @author Hocine
 * @version 1.0
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    /**
     * Attributs de la classe
     */
    private Context mContext;
    private List<User> mUsers;
    private boolean isfragment;
    private FirebaseUser firebaseUser;

    /**
     * Constructeur par initialisation
     *
     * @param mContext
     * @param mUsers
     * @param isfragment
     */
    public UserAdapter(Context mContext, List<User> mUsers, boolean isfragment) {
        this.mContext = mContext;
        this.mUsers = mUsers;
        this.isfragment = isfragment;
    }

    /**
     * Méthode permettant d'implémenter le fichier user_item
     *
     * @param viewGroup
     * @param i
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, viewGroup, false);
        return new UserAdapter.ViewHolder(view);
    }

    /**
     * Méthode permettant de gérer les utilisateurs qui apparaissent dans le fragment_search
     *
     * @param viewHolder
     * @param i
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final User user = mUsers.get(i);
        viewHolder.btn_follow.setVisibility(View.VISIBLE);

        // Affichage des nom et prenom
        viewHolder.prenom.setText(user.getPrenom());
        viewHolder.nom.setText(user.getNom());

        Glide.with(mContext).load(user.getImageurl()).into(viewHolder.image_profile);
        estSuivi(user.getId(), viewHolder.btn_follow);

        if (user.getId().equals(firebaseUser.getUid())) {
            viewHolder.btn_follow.setVisibility(View.GONE);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isfragment) {
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                    editor.putString("profileid", user.getId());
                    editor.apply();

                    ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                } else {
                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.putExtra("publisherid", user.getId());
                    mContext.startActivity(intent);
                }
            }
        });

        // Suivi de l'état du bouton pour s'abonner ou désabonner d'une personne
        viewHolder.btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewHolder.btn_follow.getText().toString().equals(mContext.getString(R.string.follow))) {
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("following").child(user.getId()).setValue(true);
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("followers").child(firebaseUser.getUid()).setValue(true);
                    addNotifications(user.getId());
                    // Déclenchement d'une notification
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel channel = new NotificationChannel("Ma notification", "Ma notification", NotificationManager.IMPORTANCE_DEFAULT);
                        NotificationManager manager = getSystemService(mContext, NotificationManager.class);
                        manager.createNotificationChannel(channel);
                    }
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, "Ma notification");
                    builder.setContentTitle("FotoShare - Notifications");
                    builder.setContentText(mContext.getString(R.string.follow_notif));
                    builder.setSmallIcon(R.drawable.logo);
                    builder.setAutoCancel(true);

                    NotificationManagerCompat managerCompat = NotificationManagerCompat.from(mContext);
                    managerCompat.notify(1, builder.build());
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).
                            child("following").child(user.getId()).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).
                            child("followers").child(firebaseUser.getUid()).removeValue();
                    // Déclenchement d'une notification
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel channel = new NotificationChannel("Ma notification", "Ma notification", NotificationManager.IMPORTANCE_DEFAULT);
                        NotificationManager manager = getSystemService(mContext, NotificationManager.class);
                        manager.createNotificationChannel(channel);
                    }
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, "Ma notification");
                    builder.setContentTitle("FotoShare - Notifications");
                    builder.setContentText(mContext.getString(R.string.unfollow_notif));
                    builder.setSmallIcon(R.drawable.logo);
                    builder.setAutoCancel(true);

                    NotificationManagerCompat managerCompat = NotificationManagerCompat.from(mContext);
                    managerCompat.notify(1, builder.build());
                }
            }
        });
    }

    /**
     * Méthode permettant d'afficher qu'une personne a commencé à vous suivre dans fragment_notification
     *
     * @param userid
     */
    private void addNotifications(String userid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userid);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userid", firebaseUser.getUid());
        hashMap.put("text", mContext.getString(R.string.follow_notif_fragment));
        hashMap.put("postid", "");
        hashMap.put("ispost", false);

        reference.push().setValue(hashMap);
    }


    /**
     * Permet de récuperer le nombre total d'utilisateur
     * Retourne le nombre d'élements de la list mUsers
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * Attribut de la classe
         */
        public TextView prenom;
        public TextView nom;
        public CircleImageView image_profile;
        public Button btn_follow;

        /**
         * Méthode permettant de récuperer les éléments du fichier xml user_item
         *
         * @param itemView
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            prenom = itemView.findViewById(R.id.prenom);
            nom = itemView.findViewById(R.id.nom);
            image_profile = itemView.findViewById(R.id.image_profile);
            btn_follow = itemView.findViewById(R.id.btn_follow);
        }

    }

    /**
     * Méthode permettant de connaitre le statut du bouton qui permet de s'abonner ou de se désabonner
     *
     * @param userid
     * @param button
     */
    private void estSuivi(final String userid, final Button button) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(userid).exists()) {
                    button.setText(mContext.getString(R.string.subscriber));
                } else {
                    button.setText(mContext.getString(R.string.follow));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
