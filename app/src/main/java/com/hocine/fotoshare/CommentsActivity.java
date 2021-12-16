package com.hocine.fotoshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
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
        getSupportActionBar().setTitle(getString(R.string.comments));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //toolbar.setNavigationOnClickListener(new View.OnClickListener() {
          //  @Override
            //public void onClick(View view) {
              //  finish();
            //}
        //});
        toolbar.setNavigationOnClickListener((view -> {finish();}));

        Intent intent = getIntent();
        postid = intent.getStringExtra("postid");
        publisherid = intent.getStringExtra("publisherid");

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(this, commentList, postid);
        recyclerView.setAdapter(commentAdapter);


        addcomment = findViewById(R.id.comment);
        image_profile = findViewById(R.id.image_profile);
        post = findViewById(R.id.post);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addcomment.getText().toString().equals("")){
                    Toast.makeText(CommentsActivity.this, getString(R.string.comments_empty), Toast.LENGTH_SHORT).show();
                }else{
                    addComment();
                    Toast.makeText(CommentsActivity.this, getString(R.string.comment_published), Toast.LENGTH_SHORT).show();
                }
            }
        });
        getImage();
        readComments();
    }

    private void addComment(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comments").child(postid);

        String commentid = reference.push().getKey();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("comment", addcomment.getText().toString());
        hashMap.put("publisher", firebaseUser.getUid());
        hashMap.put("commentid", commentid);

        reference.child(commentid).setValue(hashMap);

        addNotifications();
        addcomment.setText("");
    }

    private void addNotifications(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(publisherid);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userid", firebaseUser.getUid());
        hashMap.put("text", getString(R.string.notif_comments_fragment)+" : "+addcomment.getText().toString());
        hashMap.put("postid", postid);
        hashMap.put("ispost", true);

        reference.push().setValue(hashMap);
        Log.d("idUserKaka", firebaseUser.getUid());

        // Déclenchement d'une notification
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("Ma notification", "Ma notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(CommentsActivity.this, "Ma notification");
        builder.setContentTitle("FotoShare - Notifications");
        builder.setContentText(getString(R.string.notif_comments));
        builder.setSmallIcon(R.drawable.logo);
        builder.setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(CommentsActivity.this);
        managerCompat.notify(1, builder.build());

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