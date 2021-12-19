package com.hocine.fotoshare.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hocine.fotoshare.AddStoryActivity;
import com.hocine.fotoshare.Model.Story;
import com.hocine.fotoshare.Model.User;
import com.hocine.fotoshare.R;
import com.hocine.fotoshare.StartActivity;
import com.hocine.fotoshare.StoryActivity;

import java.util.List;

/**
 * Classe permettant de gérer les story
 *
 * @author Hocine
 * @version 1.0
 */
public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {

    /**
     * Attribut de la classe
     */
    private Context mContext;
    private List<Story> mStory;

    /**
     * Constructeur par initialisation
     *
     * @param mContext
     * @param mStory
     */
    public StoryAdapter(Context mContext, List<Story> mStory) {
        this.mContext = mContext;
        this.mStory = mStory;
    }

    /**
     * Méthode permettant d'implémenter le fichier add_story_item si l'utilisateur n'a pas encore posté de story
     * Ou le fichier story_item si l'utilisateur à deja
     *
     * @param viewGroup
     * @param i
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == 0) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.add_story_item, viewGroup, false);
            return new StoryAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.story_item, viewGroup, false);
            return new StoryAdapter.ViewHolder(view);
        }
    }

    /**
     * Méthode permettant d'afficher les elements en fonction de si l'utilisateur a déjà posté une story ou non
     *
     * @param viewHolder
     * @param i
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Story story = mStory.get(i);
        userInfo(viewHolder, story.getUserid(), i);
        if (viewHolder.getAdapterPosition() != 0) {
            seenStory(viewHolder, story.getUserid());
        }
        if (viewHolder.getAdapterPosition() == 0) {
            myStory(viewHolder.addstory_text, viewHolder.story_plus, false);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.getAdapterPosition() == 0) {
                    myStory(viewHolder.addstory_text, viewHolder.story_plus, true);
                } else {
                    Intent intent = new Intent(mContext, StoryActivity.class);
                    intent.putExtra("userid", story.getUserid());
                    mContext.startActivity(intent);
                }
            }
        });
    }

    /**
     * Permet de récuperer le nombre total de story
     * Retourne le nombre d'élements de la list mStory
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return mStory.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * Attribut de la classe ViewHolder
         */
        public ImageView story_photo, story_plus, story_photo_seen;
        public TextView story_prenom, addstory_text;

        /**
         * Méthode permettant de récuperer les élements du fichier xml
         *
         * @param itemView
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            story_photo = itemView.findViewById(R.id.story_photo);
            story_plus = itemView.findViewById(R.id.story_plus);
            story_photo_seen = itemView.findViewById(R.id.story_photo_seen);
            story_prenom = itemView.findViewById(R.id.story_prenom);
            addstory_text = itemView.findViewById(R.id.addstory_text);

        }
    }

    /**
     * Méthode permettant de gérer différents fichier xml dans une seul vue
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        return 1;
    }

    /**
     * Permet de récuperer les informations de l'utilisateur
     *
     * @param viewHolder
     * @param userid
     * @param pos
     */
    private void userInfo(ViewHolder viewHolder, String userid, int pos) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Glide.with(mContext).load(user.getImageurl()).into(viewHolder.story_photo);
                if (pos != 0) {
                    Glide.with(mContext).load(user.getImageurl()).into(viewHolder.story_photo_seen);
                    viewHolder.story_prenom.setText(user.getPrenom());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Méthode gérant l'affichage de l'element rond correspondant à une story en fonction si l'utilisateur en a posté une ou non
     *
     * @param textView
     * @param imageView
     * @param click
     */
    private void myStory(TextView textView, ImageView imageView, boolean click) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Story").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 0;
                long timecurrent = System.currentTimeMillis();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Story story = snapshot.getValue(Story.class);
                    if (timecurrent > story.getTimestart() && timecurrent < story.getTimeend()) {
                        count++;
                    }
                }

                if (click) {
                    if (count > 0) {
                        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, mContext.getString(R.string.view_story), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                Intent intent = new Intent(mContext, StoryActivity.class);
                                intent.putExtra("userid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                mContext.startActivity(intent);
                                dialogInterface.dismiss();
                            }
                        });
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, mContext.getString(R.string.add_story), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                Intent intent = new Intent(mContext, AddStoryActivity.class);
                                mContext.startActivity(intent);
                                dialogInterface.dismiss();
                            }
                        });
                        alertDialog.show();
                    } else {
                        Intent intent = new Intent(mContext, AddStoryActivity.class);
                        mContext.startActivity(intent);
                    }
                } else {
                    if (count > 0) {
                        textView.setText(mContext.getString(R.string.my_story));
                        imageView.setVisibility(View.GONE);
                    } else {
                        textView.setText(mContext.getString(R.string.story));
                        imageView.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Permet de définir les story que l'on a vu, si on les a vu cela retire le cercle rouge
     *
     * @param viewHolder
     * @param userid
     */
    private void seenStory(ViewHolder viewHolder, String userid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Story").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (!snapshot.child("views").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists() &&
                            System.currentTimeMillis() < snapshot.getValue(Story.class).getTimeend()) {
                        i++;
                    }
                }
                if (i > 0) {
                    viewHolder.story_photo.setVisibility(View.VISIBLE);
                    viewHolder.story_photo_seen.setVisibility(View.GONE);
                } else {
                    viewHolder.story_photo.setVisibility(View.GONE);
                    viewHolder.story_photo_seen.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
