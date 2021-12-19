package com.hocine.fotoshare.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hocine.fotoshare.Fragment.PostDetailFragment;
import com.hocine.fotoshare.Model.Post;
import com.hocine.fotoshare.R;

import java.util.List;

/**
 * Classe permettant la gestion de la photo de profil
 *
 * @author Hocine
 * @version 1.0
 */
public class MyFotoAdapter extends RecyclerView.Adapter<MyFotoAdapter.ViewHolder> {

    /**
     * Déclaration des attributs
     */
    private Context context;
    public List<Post> mPosts;

    /**
     * Constructeurs par initialisation
     *
     * @param context
     * @param mPosts
     */
    public MyFotoAdapter(Context context, List<Post> mPosts) {
        this.context = context;
        this.mPosts = mPosts;
    }

    /**
     * Méthode permettant d'implémenter le fichier xml fotos_item.xml
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fotos_item, parent, false);
        return new MyFotoAdapter.ViewHolder(view);
    }

    /**
     * Mise en place du sharedPreferences
     *
     * @param viewHolder
     * @param i
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Post post = mPosts.get(i);
        Glide.with(context).load(post.getPostimage()).into(viewHolder.post_image);

        viewHolder.post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = context.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("postid", post.getPostid());
                editor.apply();

                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PostDetailFragment()).commit();
            }
        });
    }

    /**
     * Permet de récuperer le nombre total de post
     * Retourne le nombre d'élements de la list mPosts
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return mPosts.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * Atrribut de la classe
         */
        public ImageView post_image;

        /**
         * Récupère l'element post_image de la vue
         *
         * @param itemView
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            post_image = itemView.findViewById(R.id.post_image);
        }
    }
}
