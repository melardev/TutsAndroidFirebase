package com.melardev.tutorialsfirebase;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ActivityUpdateUser extends AppCompatActivity {

    private static final String TAG = "";
    FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText etxtNewEmail;
    private EditText etxtNewPassword;
    private EditText etxtNewName;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        etxtNewName = (EditText) findViewById(R.id.etxtNewName);
        etxtNewEmail = (EditText) findViewById(R.id.etxtNewEmail);
        etxtNewPassword = (EditText) findViewById(R.id.etxtNewPassword);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    Intent intent = new Intent(ActivityUpdateUser.this, ActivityLoginEmailPass.class);
                    intent.putExtra("goToActivityUsers", true);
                    startActivity(intent);
                    finish();
                } else {
                    etxtNewName.setText(user.getDisplayName());
                    etxtNewEmail.setText(user.getEmail());
                }
            }
        };
    }

    public void sendPasswordResetEmail(View view) {
        String emailAddress = "melardev_email@email.com";
        mAuth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ActivityUpdateUser.this, "password reset email sent", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getUserProviderProfileInfo(View view) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();

                // UID specific to the provider
                String uid = profile.getUid();

                // Name, email address, and profile photo Url
                String name = profile.getDisplayName();
                String email = profile.getEmail();
                Uri photoUrl = profile.getPhotoUrl();

                Toast.makeText(this, "id : " + providerId + ", uid : " + uid + " name: " + name
                        + " email : " + email + " " + photoUrl, Toast.LENGTH_SHORT).show();
            }
            ;
        }
    }

    public void updateUserProfile(View view) {
        FirebaseUser user = mAuth.getCurrentUser();

        String newName = etxtNewName.getText().toString();
        if (TextUtils.isEmpty(newName))
            return;

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(newName)
                //.setPhotoUri(Uri.parse("https://www.famouslogos.net/images/android-logo.jpg"))
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ActivityUpdateUser.this, "User updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void setUserEmailAddr(View view) {
        String newEmail = etxtNewEmail.getText().toString();
        if (TextUtils.isEmpty(newEmail))
            return;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    Toast.makeText(ActivityUpdateUser.this, "email updated", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void setUserPassword(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String newPassword = etxtNewPassword.getText().toString();
        if (TextUtils.isEmpty(newPassword))
            return;

        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            Toast.makeText(ActivityUpdateUser.this, "password updated", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void userVerification(View view) {
        FirebaseUser user = mAuth.getCurrentUser();

        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ActivityUpdateUser.this, "Email sent", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void deleteUser(View view) {
        FirebaseUser user = mAuth.getCurrentUser();
        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    Toast.makeText(ActivityUpdateUser.this, "User deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void signOut(View view) {
        mAuth.signOut();
    }
}
