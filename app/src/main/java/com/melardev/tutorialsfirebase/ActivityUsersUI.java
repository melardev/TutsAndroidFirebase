package com.melardev.tutorialsfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class ActivityUsersUI extends AppCompatActivity {

    private RecyclerView recycler;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference databaseUsers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_ui);
        databaseUsers = FirebaseDatabase.getInstance().getReference().child("users");
        mAuth = FirebaseAuth.getInstance();
        recycler = (RecyclerView) findViewById(R.id.recyclerView);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent logIntent = new Intent(ActivityUsersUI.this, ActivityLoginEmailPass.class);
                    logIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(logIntent);
                    finish();
                } else {
                    FirebaseRecyclerAdapter<User, UserViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(
                            User.class,
                            R.layout.user_row,
                            UserViewHolder.class,
                            databaseUsers
                    ) {
                        @Override
                        protected void populateViewHolder(final UserViewHolder holder, User model, int position) {
                            holder.txtName.setText(model.name);
                            if (!model.image.equals("default"))
                                Picasso.with(ActivityUsersUI.this).load(model.image).into(holder.imgProfile);
                        }
                    };
                    recycler.setAdapter(firebaseRecyclerAdapter);
                }
            }
        };
    }

    public static class User {
        String name;
        String image;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() == null)
            return;

        mAuth.addAuthStateListener(mAuthListener);
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        ImageView imgProfile;

        public UserViewHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txtUser);
            imgProfile = (ImageView) itemView.findViewById(R.id.imageUser);
        }
    }
}
