package com.melardev.tutorialsfirebase.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.melardev.tutorialsfirebase.R;

public class ActivityDelete extends AppCompatActivity {

    private DatabaseReference databasePost;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Button btnCreate;
    private Button btnDelete;
    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        mAuth = FirebaseAuth.getInstance();
        btnCreate = (Button) findViewById(R.id.createPost);
        btnDelete = (Button) findViewById(R.id.deletePosts);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAuth.getCurrentUser() == null || databasePost == null)
                    return;

                DatabaseReference newPost = databasePost.push();
                newPost.child("title").setValue("titleExample");
                newPost.child("description").setValue("descriptionExample");
            }
        });


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAuth.getCurrentUser() == null || databasePost == null)
                    return;

                databasePost.setValue(null);
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    databasePost = FirebaseDatabase.getInstance().getReference().child("posts");

                }
            }
        };
    }
}
