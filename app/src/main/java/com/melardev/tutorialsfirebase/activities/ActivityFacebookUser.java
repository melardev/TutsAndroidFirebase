package com.melardev.tutorialsfirebase.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.melardev.tutorialsfirebase.R;
import com.squareup.picasso.Picasso;

public class ActivityFacebookUser extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private Button btnOut;
    private TextView txtEmail, txtUser;
    private ImageView imgProfile;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_user);

        mAuth = FirebaseAuth.getInstance();
        btnOut = (LoginButton) findViewById(R.id.btnOut);
        txtUser = (TextView) findViewById(R.id.txtUser);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        imgProfile = (ImageView) findViewById(R.id.imgProfile);

        btnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    for (UserInfo userInfo : user.getProviderData()) {
                        Log.d("TAG", userInfo.getProviderId());
                    }

                    txtUser.setText(user.getDisplayName());
                    txtEmail.setText(user.getEmail());
                    Picasso.with(ActivityFacebookUser.this).load(user.getPhotoUrl()).into(imgProfile);
                } else {
                    Intent intent = new Intent(ActivityFacebookUser.this, ActivityLoginFacebook.class);
                    intent.putExtra("logout", true);
                    startActivity(intent);
                    finish();
                }

            }
        };
    }
}
