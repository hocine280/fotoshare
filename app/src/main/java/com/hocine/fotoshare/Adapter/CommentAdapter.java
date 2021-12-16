package com.hocine.fotoshare.Adapter;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hocine.fotoshare.CommentsActivity;
import com.hocine.fotoshare.MainActivity;
import com.hocine.fotoshare.Model.Comment;
import com.hocine.fotoshare.Model.User;
import com.hocine.fotoshare.R;

import java.util.List;

/**
 * Class CommentAdapter permettant de faire la liaison entre la vue des commentaires et les données, ici les commentaires
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    /**
     * Déclaration des attributs
     */
    private Context mContext;
    private List<Comment> mComment;
    private String postid;
    private FirebaseUser firebaseUser;

    /**
     * Constructeurs par initialisation
     *
     * @param mContext
     * @param mComment
     * @param postid
     */
    public CommentAdapter(Context mContext, List<Comment> mComment, String postid) {
        this.mContext = mContext;
        this.mComment = mComment;
        this.postid = postid;
    }

    /**
     * Méthode permettant d'implémenter le fichier xml comment_item, qui permet d'afficher chaque commentaire
     *
     * @param viewGroup
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.comment_item, viewGroup, false);
        return new CommentAdapter.ViewHolder(view);
    }

    /**
     * Méthode permettant de mettre à jour chaque commentaire (comment_item)
     *
     * @param viewHolder
     * @param i
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        // Récupération de l'utilisateur en base de données
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Comment comment = mComment.get(i);
        viewHolder.comment.setText(comment.getComment());
        getUserInfo(viewHolder.image_profile, viewHolder.prenom, comment.getPublisher());
        viewHolder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra("publisherid", comment.getPublisher());
                mContext.startActivity(intent);
            }
        });

        viewHolder.image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra("publisherid", comment.getPublisher());
                mContext.startActivity(intent);
            }
        });

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            /**
             * Méthode permettant lors d'un long appui sur un commentaire d'afficher une pop-up (AlertDialog), afin de supprimer ou non le commentaire en question
             * @param v
             * @return boolean
             */
            public boolean onLongClick(View v) {
                if (comment.getPublisher().equals(firebaseUser.getUid())) {
                    // Création de la pop-up
                    AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                    alertDialog.setTitle(mContext.getString(R.string.delete_comment));
                    // Annule la suppression d'un commentaire
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, mContext.getString(R.string.no),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    dialogInterface.dismiss();
                                }
                            });
                    // Supprime le commentaire selectionné
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, mContext.getString(R.string.yes),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    FirebaseDatabase.getInstance().getReference("Comments")
                                            .child(postid).child(comment.getCommentid())
                                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(mContext, "Supprimé !", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    dialogInterface.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                return true;
            }
        });

    }

    /**
     * Permet de récuperer le nombre total de commentaire que le post selectionné possède
     * @return int
     */
    @Override
    public int getItemCount() {
        return mComment.size();
    }

    /**
     * Class ViewHolder permettant de récuperer les élements de la vue (fichier .xml)
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image_profile;
        public TextView prenom, comment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_profile = itemView.findViewById(R.id.image_profile);
            prenom = itemView.findViewById(R.id.prenom);
            comment = itemView.findViewById(R.id.comment);

        }
    }

    /**
     * Méthode permettant de récuper les informations du profil de l'utilisateur ayant poster le commentaire
     * @param imageView
     * @param prenom
     * @param publisherid
     */
    private void getUserInfo(ImageView imageView, TextView prenom, String publisherid) {
        // Récupération des infos de l'utilisateur en question
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(publisherid);

        reference.addValueEventListener(new ValueEventListener() {
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
}
