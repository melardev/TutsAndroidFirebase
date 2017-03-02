package com.melardev.tutorialsfirebase;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ActivityUpdateUser extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        UserProfileChangeRequest updateRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName("")
                .setPhotoUri(Uri.parse(""))
                .build();

        mAuth.getCurrentUser().updateProfile(updateRequest)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful())
                            Toast.makeText(ActivityUpdateUser.this, "", Toast.LENGTH_SHORT).show();

                    }
                });
    }
}
