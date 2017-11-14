package com.melardev.tutorialsfirebase.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.melardev.tutorialsfirebase.R;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*new AlertDialog.Builder(this).setMessage("Please note that the demos may not work, because I have added a sha1 fingerprint in my firebase console," +
                " the code shown in my tutorials in youtube was tested and it doest work, take it as a template and copy it to your android studio project, " +
                "update the values in strings.xml accordingly, search me in youtube by searching Melar Dev").setPositiveButton(android.R.string.ok, null).setCancelable(false).show();*/
    }


    public void registerDemo(View view) {
        startDemo(ActivityRegister.class);
    }

    public void loginDemo(View view) {
        startDemo(ActivityLoginEmailPass.class);
    }

    public void viewProfile(View view) {
        startDemo(ActivityAccount.class);
    }

    public void adsDemo(View view) {
        startDemo(ActivityAds.class);
    }

    public void iterateDbDemo(View view) {
        startDemo(ActivityUsers.class);
    }

    public void firebaseUIRecyclerDemo(View view) {
        startDemo(ActivityUsersUI.class);
    }

    public void pushNotifConsoleDemo(View view) {
        startDemo(ActivityPushNotificationsConsole.class);
    }

    public void pushNotifServer(View view) {
        startDemo(ActivityPushNotificationsServer.class);
    }

    public void facebookDemo(View view) {
        startDemo(ActivityLoginFacebook.class);
    }

    public void loginAnonymously(View view) {
        startDemo(ActivityAnonymous.class);
    }

    public void startDemo(Class className) {
        startActivity(new Intent(this, className));
    }

    public void dbObjMap(View view) {
        startDemo(ActivityDBRetriever.class);
    }

    public void manageUser(View view) {
        startDemo(ActivityUpdateUser.class);
    }

    public void monitoringTool(View view) {
        startDemo(ActivityMonitoring.class);
    }

    public void firebaseAnalytics(View view) {
        startDemo(ActivityAnalytics.class);
    }

    public void dynamicLink(View view) {
        startDemo(ActivityDynamicLink.class);
    }

    public void fabricCrashlytics(View view) {
        startDemo(ActivityFabricCrashlytics.class);
    }

    public void phoneNumber(View view) {
        startDemo(ActivityPhoneAuth.class);
    }

    public void loginGithub(View view) {
        startDemo(ActivityGithub.class);
    }

    public void googleLogin(View view) {
        startDemo(ActivityGoogleLogin.class);
    }

    public void firebaseCrashReporter(View view) {
        startDemo(ActivityCrash.class);
    }

    public void twitterLogin(View view) {
        startDemo(ActivityTwitterAuth.class);
    }
}
