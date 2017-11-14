package com.melardev.tutorialsfirebase.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.TwitterAuthProvider;
import com.melardev.tutorialsfirebase.R;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.SessionManager;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

public class ActivityTwitterAuth extends AppCompatActivity {

    private static final String TAG = "";
    private TwitterLoginButton btnTwitter;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView txtTwitter;
    private ImageView imgTwitter;
    private Button btnSignOut;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // https://dev.twitter.com/twitterkit/android/overview
        // Configure Twitter SDK
        TwitterAuthConfig authConfig = new TwitterAuthConfig(
                getString(R.string.consumer_key),
                getString(R.string.consumer_secret));
        TwitterConfig twitterConfig = new TwitterConfig.Builder(this)
                .twitterAuthConfig(authConfig)
                .build();

        Twitter.initialize(twitterConfig);

        setContentView(R.layout.activity_twitter_auth);

        txtTwitter = (TextView) findViewById(R.id.txtTwitter);
        imgTwitter = (ImageView) findViewById(R.id.img_twitter);
        btnTwitter = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        btnSignOut = (Button) findViewById(R.id.btn_twitter_signout);

        updateButtons();
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //https://stackoverflow.com/a/35350301/6393636
                SessionManager<TwitterSession> sessionManager = TwitterCore.getInstance().getSessionManager();
                if (sessionManager.getActiveSession() != null) {
                    sessionManager.clearActiveSession();
                    mAuth.signOut();
                    updateButtons();
                }
            }
        });

        btnTwitter.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Toast.makeText(ActivityTwitterAuth.this, "Signed in to twitter succesfully " + result.data, Toast.LENGTH_SHORT).show();
                signToFirebaseWithTwitterSession(result.data);
                updateButtons();
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(ActivityTwitterAuth.this, "Failure to login to Twitter", Toast.LENGTH_SHORT).show();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(ActivityTwitterAuth.this, "signed in", Toast.LENGTH_SHORT).show();
                    txtTwitter.setText(user.getDisplayName());
                    Picasso.with(ActivityTwitterAuth.this).load(user.getPhotoUrl()).into(imgTwitter);
                } else {
                    Toast.makeText(ActivityTwitterAuth.this, "signed out from Firebase", Toast.LENGTH_SHORT).show();
                    txtTwitter.setText("");
                    imgTwitter.setImageBitmap(null);
                }
                // ...
            }
        };
    }

    private void updateButtons() {
        if (TwitterCore.getInstance().getSessionManager().getActiveSession() == null) {
            btnTwitter.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);
        } else {
            btnSignOut.setVisibility(View.VISIBLE);
            btnTwitter.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        btnTwitter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    private void signToFirebaseWithTwitterSession(TwitterSession session) {
        AuthCredential credential = TwitterAuthProvider.getCredential(session.getAuthToken().token,
                session.getAuthToken().secret);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(ActivityTwitterAuth.this,
                                R.string.login_ok_twitter, Toast.LENGTH_SHORT).show();

                        if (!task.isSuccessful()) {
                            Toast.makeText(ActivityTwitterAuth.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
