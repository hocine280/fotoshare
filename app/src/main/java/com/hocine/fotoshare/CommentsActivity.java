package com.hocine.fotoshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hocine.fotoshare.Adapter.CommentAdapter;
import com.hocine.fotoshare.Model.Comment;
import com.hocine.fotoshare.Model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CommentsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;


    EditText addcomment;
    ImageView image_profile;
    TextView post;
    String postid;
    String publisherid;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Commentaires");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //toolbar.setNavigationOnClickListener(new View.OnClickListener() {
          //  @Override
            //public void onClick(View view) {
              //  finish();
            //}
        //});
        toolbar.setNavigationOnClickListener((view -> {finish();}));

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(this, commentList);
        recyclerView.setAdapter(commentAdapter);


        addcomment = findViewById(R.id.comment);
        image_profile = findViewById(R.id.image_profile);
        post = findViewById(R.id.post);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = getIntent();
        postid = intent.getStringExtra("postid");
        publisherid = intent.getStringExtra("publisherid"); 
        
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addcomment.getText().toString().equals("")){
                    Toast.makeText(CommentsActivity.this, "Vous ne pouvez pas publier un commentaire vide", Toast.LENGTH_SHORT).show();
                }else{
                    addComment();
                    Toast.makeText(CommentsActivity.this, "Commentaire publié", Toast.LENGTH_SHORT).show();
                }
            }
        });
        getImage();
        Log.d("commentaire", "Avant appel fonction readComments");
        readComments();
        Log.d("commentaire", "Après appel fonction readComments");
    }

    private void addComment(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comments").child(postid);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("comment", addcomment.getText().toString());
        hashMap.put("publisher", firebaseUser.getUid());

        reference.push().setValue(hashMap);
        addcomment.setText("");
    }

    private void getImage(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Glide.with(getApplicationContext()).load(user.getImageurl()).into(image_profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readComments(){
        Log.d("commentaire", "Rentrer dans la fonction readComments");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comments").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentList.clear();
                Log.d("commentaire", "fonction onDataChange");
                //dataSnapshot.getChildren().toString()
                Log.d("commentaire", "avant Boucle for de la fonction onDataChange");

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Comment comment = snapshot.getValue(Comment.class);
                    commentList.add(comment);
                }
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}