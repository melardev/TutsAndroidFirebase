package com.melardev.tutorialsfirebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityLoginEmailPass extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText textEmail;
    private EditText textPass;
    private Button btnRegister;
    private ProgressDialog progressDialog;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email_pass);

        progressDialog = new ProgressDialog(this);
        textEmail = (EditText) findViewById(R.id.etxt_email);
        textPass = (EditText) findViewById(R.id.etxt_password);
        btnRegister = (Button) findViewById(R.id.btn_login);
        mAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    Toast.makeText(ActivityLoginEmailPass.this, "Now you are logged In " + firebaseAuth.getCurrentUser().getUid(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ActivityLoginEmailPass.this, ActivityAccount.class);
                    startActivity(intent);
                    finish();
                    //mAuth.signOut();
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void doLogin() {
        String email = textEmail.getText().toString().trim();
        String password = textPass.getText().toString().trim();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            progressDialog.setMessage("Loging , please wait");
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                Toast.makeText(ActivityLoginEmailPass.this, "Login succesful", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(ActivityLoginEmailPass.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
