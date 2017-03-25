package com.melardev.tutorialsfirebase;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.melardev.tutorialsfirebase.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ActivityDBRetriever extends AppCompatActivity {

    private RecyclerView recyclerview;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    ArrayList<User> entries = new ArrayList<>();

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbretriever);

        mAuth = FirebaseAuth.getInstance();
        recyclerview = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent intent = new Intent(ActivityDBRetriever.this, ActivityLoginEmailPass.class);
                    intent.putExtra("goToActivityUsers", true);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }


    private void getUsersObjects() {

        //mDatabase.child("users").limitToFirst(2).addValueEventListener(new ValueEventListener() {
        //mDatabase.child("users").orderByChild("name").addValueEventListener(new ValueEventListener() {
        mDatabase.child("users")
                .addValueEventListener(new ValueEventListener() {
                                           //mDatabase.child("users").addValueEventListener(new ValueEventListener() {
                                           @Override
                                           public void onDataChange(DataSnapshot dataSnapshot) {
                                               Iterator<DataSnapshot> items = dataSnapshot.getChildren().iterator();
                                               Toast.makeText(ActivityDBRetriever.this, "Total Users : " + dataSnapshot.getChildrenCount(), Toast.LENGTH_SHORT).show();
                                               entries.clear();
                                               while (items.hasNext()) {
                                                   DataSnapshot item = items.next();
                                                   User user = item.getValue(User.class);
                                                   entries.add(user);
                                               }

                                               recyclerview.setAdapter(new RecUsersAdapter(ActivityDBRetriever.this, entries));
                                               recyclerview.getAdapter().notifyDataSetChanged();
                                               mDatabase.child("users").removeEventListener(this);
                                           }

                                           @Override
                                           public void onCancelled(DatabaseError databaseError) {
                                               Toast.makeText(ActivityDBRetriever.this, "onCancelled called", Toast.LENGTH_SHORT).show();
                                           }
                                       }

                );
    }

    public void getDBObjects(View view) {
        if (mAuth.getCurrentUser() == null)
            return;
        getUsersObjects();
    }

    public void getDBMap(View view) {
        if (mAuth.getCurrentUser() == null)
            return;
        getUsersMap();
    }

    private void getUsersMap() {
        //mDatabase.child("users").limitToFirst(2).addValueEventListener(new ValueEventListener() {
        //mDatabase.child("users").orderByChild("name").addValueEventListener(new ValueEventListener() {
        mDatabase.child("users")
                .addValueEventListener(new ValueEventListener() {
                                           //mDatabase.child("users").addValueEventListener(new ValueEventListener() {
                                           @Override
                                           public void onDataChange(DataSnapshot dataSnapshot) {
                                               Iterator<DataSnapshot> items = dataSnapshot.getChildren().iterator();
                                               Toast.makeText(ActivityDBRetriever.this, "Total Users : " + dataSnapshot.getChildrenCount(), Toast.LENGTH_SHORT).show();
                                               entries.clear();
                                               HashMap<String, Object> user = null;
                                               while (items.hasNext()) {
                                                   DataSnapshot item = items.next();
                                                   user = (HashMap<String, Object>) item.getValue();
                                                   entries.add(new User(user.get("name").toString(), user.get("image").toString()));
                                               }

                                               recyclerview.setAdapter(new RecUsersAdapter(ActivityDBRetriever.this, entries));
                                               recyclerview.getAdapter().notifyDataSetChanged();
                                               mDatabase.child("users").removeEventListener(this);
                                           }

                                           @Override
                                           public void onCancelled(DatabaseError databaseError) {

                                           }
                                       }

                );
    }


    private class RecUsersAdapter extends RecyclerView.Adapter<RecUsersAdapter.RecViewHolder> {


        private Context context;
        private ArrayList<User> entries;

        public RecUsersAdapter(ActivityDBRetriever context, ArrayList<User> entries) {
            this.context = context;
            this.entries = entries;
        }

        @Override
        public RecUsersAdapter.RecViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.user_row, null);
            return new RecViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecViewHolder holder, int position) {
            User user = entries.get(position);
            if (!user.image.equals("default"))
                Picasso.with(context).load(user.image).into(holder.imageView);
            holder.textView.setText(user.name);
        }


        @Override
        public int getItemCount() {
            return entries.size();
        }

        public class RecViewHolder extends RecyclerView.ViewHolder {

            public ImageView imageView;
            public TextView textView;

            public RecViewHolder(View itemView) {
                super(itemView);

                imageView = (ImageView) itemView.findViewById(R.id.imageUser);
                textView = (TextView) itemView.findViewById(R.id.txtUser);
            }
        }
    }
}
