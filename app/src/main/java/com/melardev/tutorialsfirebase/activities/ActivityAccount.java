package com.melardev.tutorialsfirebase.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.melardev.tutorialsfirebase.R;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.math.BigInteger;
import java.security.SecureRandom;

public class ActivityAccount extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private Button btnLogOut;
    private int CAMERA_REQUEST_CODE = 0;
    private ProgressDialog progressDialog;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private ImageView imageProfile;
    private TextView textName;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        textName = (TextView) findViewById(R.id.txtName);
        imageProfile = (ImageView) findViewById(R.id.imageView);
        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(Intent.createChooser(intent, "Select a picture for your profile"), CAMERA_REQUEST_CODE);
                }
            }
        });
        btnLogOut = (Button) findViewById(R.id.btn_singout);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAuth.getCurrentUser() != null)
                    mAuth.signOut();
            }
        });

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    mStorage = FirebaseStorage.getInstance().getReference();
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
                    mDatabase.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            textName.setText(String.valueOf(dataSnapshot.child("name").getValue()));
                            String imageUrl = String.valueOf(dataSnapshot.child("image").getValue());
                            if (URLUtil.isValidUrl(imageUrl))
                                Picasso.with(ActivityAccount.this).load(Uri.parse(imageUrl)).into(imageProfile);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    startActivity(new Intent(ActivityAccount.this, ActivityLoginEmailPass.class));
                    finish();
                }
            }
        };
    }

    public String getRandomString() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UCrop.RESULT_ERROR) {
            Toast.makeText(this, "uCrop error", Toast.LENGTH_SHORT).show();
            return;
        }
        if (requestCode == UCrop.REQUEST_CROP) {
            final Uri imgUri = UCrop.getOutput(data);
            Toast.makeText(this, imgUri.getPath(), Toast.LENGTH_SHORT).show();
            uploadImage(imgUri);
            return;
        }

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {

            final Uri sourceUri = data.getData();
            if (sourceUri == null) {
                progressDialog.dismiss();
                return;
            } else {
                File tempCropped = new File(getCacheDir(), "tempImgCropped.png");
                Uri destinationUri = Uri.fromFile(tempCropped);
                UCrop.of(sourceUri, destinationUri)
                        //.withAspectRatio(3, 2)
                        //.withMaxResultSize(MAX_WIDTH, MAX_HEIGHT)
                        .start(this);
            }
        }
    }


    public void uploadImage(final Uri fileUri) {
        if (mAuth.getCurrentUser() == null)
            return;

        if (mStorage == null)
            mStorage = FirebaseStorage.getInstance().getReference();
        if (mDatabase == null)
            mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        final StorageReference filepath = mStorage.child("Photos").child(getRandomString());/*uri.getLastPathSegment()*/
        final DatabaseReference currentUserDB = mDatabase.child(mAuth.getCurrentUser().getUid());

        progressDialog.setMessage("Uploading image...");
        progressDialog.show();

        currentUserDB.child("image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String image = dataSnapshot.getValue().toString();

                if (!image.equals("default") && !image.isEmpty()) {
                    Task<Void> task = FirebaseStorage.getInstance().getReferenceFromUrl(image).delete();
                    task.addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                                Toast.makeText(ActivityAccount.this, "Deleted image succesfully", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(ActivityAccount.this, "Deleted image failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                currentUserDB.child("image").removeEventListener(this);

                filepath.putFile(fileUri).addOnSuccessListener(ActivityAccount.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Uri downloadUri = taskSnapshot.getDownloadUrl();
                        Toast.makeText(ActivityAccount.this, "Finished", Toast.LENGTH_SHORT).show();
                        Picasso.with(ActivityAccount.this).load(fileUri).fit().centerCrop().into(imageProfile);
                        DatabaseReference currentUserDB = mDatabase.child(mAuth.getCurrentUser().getUid());
                        currentUserDB.child("image").setValue(downloadUri.toString());
                    }
                }).addOnFailureListener(ActivityAccount.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(ActivityAccount.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
