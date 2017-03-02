package com.melardev.tutorialsfirebase;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new AlertDialog.Builder(this).setMessage("Please note that the demos may not work, because I have added a sha1 fingerprint in my firebase console," +
                " the code shown in my tutorials in youtube was tested and it doest work, take it as a template and copy it to your android studio project, " +
                "update the values in strings.xml accordingly, search me in youtube by searching Melar Dev").setPositiveButton(android.R.string.ok, null).setCancelable(false).show();
    }

    public void registerDemo(View view) {
        startActivity(new Intent(this, ActivityRegister.class));
    }

    public void loginDemo(View view) {
        startActivity(new Intent(this, ActivityLoginEmailPass.class));
    }

    public void manageAccount(View view) {
        startActivity(new Intent(this, ActivityAccount.class));
    }

    public void adsDemo(View view) {
        startActivity(new Intent(this, ActivityAds.class));
    }

    public void iterateDbDemo(View view) {
        startActivity(new Intent(this, ActivityUsers.class));
    }

    public void firebaseUIRecyclerDemo(View view) {
        startActivity(new Intent(this, ActivityUsersUI.class));

    }

    public void pushNotifDemo(View view) {
        startActivity(new Intent(this, ActivityPushNotifications.class));
    }

    public void facebookDemo(View view) {
        startActivity(new Intent(this, ActivityLoginFacebook.class));
    }

    public void loginAnonymously(View view) {
        startActivity(new Intent(this, ActivityAnonymous.class));
    }
}
