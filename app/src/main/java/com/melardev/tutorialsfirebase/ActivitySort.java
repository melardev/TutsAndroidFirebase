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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;

public class ActivitySort extends AppCompatActivity {

    private DatabaseReference databasePost;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseAuth mAuth;
    private RecyclerView recyclerview;
    private DatabaseReference mDatabase;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);

        mAuth = FirebaseAuth.getInstance();
        recyclerview = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent intent = new Intent(ActivitySort.this, ActivityLoginEmailPass.class);
                    intent.putExtra("goToActivityUsers", true);
                    startActivity(intent);
                    finish();
                } else {
                    getUsers();
                }
            }
        };
    }

    private static class UserDB {
        public String name, image;

        public UserDB(String name, String image) {
            this.name = name;
            this.image = image;
        }
    }
    private ArrayList<UserDB> entries;
    private void getUsers() {
        mDatabase.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> items = dataSnapshot.getChildren().iterator();
                Toast.makeText(ActivitySort.this, "Total Users : " + dataSnapshot.getChildrenCount(), Toast.LENGTH_SHORT).show();
                entries.clear();
                while (items.hasNext()) {
                    DataSnapshot item = items.next();
                    String name, image;
                    name = item.child("name").getValue().toString();
                    image = item.child("image").getValue().toString();
                    UserDB entry = new UserDB(name, image);
                    entries.add(entry);
                }

                recyclerview.setAdapter(new RecUsersAdapter(ActivitySort.this, entries));
                recyclerview.getAdapter().notifyDataSetChanged();
                mDatabase.child("users").removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private class RecUsersAdapter extends RecyclerView.Adapter<RecUsersAdapter.RecViewHolder> {


        private Context context;
        private ArrayList<UserDB> entries;

        public RecUsersAdapter(Context context, ArrayList<UserDB> entries) {
            this.context = context;
            this.entries = entries;
        }

        @Override
        public RecUsersAdapter.RecViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.user_row, null);
            return new RecUsersAdapter.RecViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecUsersAdapter.RecViewHolder holder, int position) {
            UserDB user = entries.get(position);
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
