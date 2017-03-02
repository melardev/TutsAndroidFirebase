package com.melardev.tutorialsfirebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ActivityUIListAdapter extends AppCompatActivity {

    private ListView listView;
    private DatabaseReference dbUsers;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uilist_adapter);

        listView = (ListView) findViewById(R.id.listView);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent logIntent = new Intent(ActivityUIListAdapter.this, ActivityLoginEmailPass.class);
                    logIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(logIntent);
                } else {
                    dbUsers = FirebaseDatabase.getInstance().getReference().child("users");
                    FirebaseListAdapter<ActivityUsersUI.User> firebaseRecyclerAdapter = new FirebaseListAdapter<ActivityUsersUI.User>(
                            ActivityUIListAdapter.this,
                            ActivityUsersUI.User.class,
                            R.layout.user_row,
                            dbUsers
                    ) {
                        TextView txtName;
                        ImageView imgProfile;

                        @Override
                        protected void populateView(View v, ActivityUsersUI.User model, int position) {
                            txtName = (TextView) v.findViewById(R.id.txtUser);
                            imgProfile = (ImageView) v.findViewById(R.id.imageUser);
                            txtName.setText(model.name);
                            if (!model.image.equals("default"))
                                Picasso.with(ActivityUIListAdapter.this).load(model.image).into(imgProfile);
                        }
                    };
                    listView.setAdapter(firebaseRecyclerAdapter);
                }
            }
        };
    }
}
