package com.melardev.tutorialsfirebase;

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
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.squareup.picasso.Picasso;

public class ActivityGoogleLogged extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private SignInButton btnOut;
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
        setContentView(R.layout.activity_google_logged);

        mAuth = FirebaseAuth.getInstance();
        btnOut = (SignInButton) findViewById(R.id.btn_sign_google);
        txtUser = (TextView) findViewById(R.id.txtUser);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        imgProfile = (ImageView) findViewById(R.id.imgProfile);

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
                    Picasso.with(ActivityGoogleLogged.this).load(user.getPhotoUrl()).into(imgProfile);

                    btnOut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mAuth.signOut();
                        }
                    });
                } else {
                    Intent intent = new Intent(ActivityGoogleLogged.this, ActivityGoogleLogin.class);
                    startActivity(intent);
                    finish();
                }

            }
        };

    }
}
