package com.melardev.tutorialsfirebase;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityAnonymous extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authListener;
    private Button btnLogIn;
    private Button btnLogOut;
    private Button btnPermanent;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anonymous);
        btnLogIn = (Button) findViewById(R.id.btn_login);
        btnLogOut = (Button) findViewById(R.id.btn_sign_out);
        btnPermanent = (Button) findViewById(R.id.btn_permanent);
        mAuth = FirebaseAuth.getInstance();

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task<AuthResult> resultTask = mAuth.signInAnonymously();
                resultTask.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                    }
                });
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });

        btnPermanent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = "password";
                String email = "malo@malo.com";

                AuthCredential credential = EmailAuthProvider.getCredential(email, password);
                mAuth.getCurrentUser().linkWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        btnPermanent.setVisibility(View.GONE);
                        Toast.makeText(ActivityAnonymous.this, "done!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    // Toast.makeText(ActivityAnonymous.this, "SignedIn", Toast.LENGTH_SHORT).show();
                    btnLogIn.setVisibility(View.GONE);
                    btnLogOut.setVisibility(View.VISIBLE);
                    btnPermanent.setVisibility(View.VISIBLE);
                    //Toast.makeText(ActivityAnonymous.this, mAuth.getCurrentUser().getProviderId(), Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(ActivityAnonymous.this, "SignedOut", Toast.LENGTH_SHORT).show();
                    btnLogOut.setVisibility(View.GONE);
                    btnLogIn.setVisibility(View.VISIBLE);
                    btnPermanent.setVisibility(View.GONE);
                }
            }
        };
    }

}
