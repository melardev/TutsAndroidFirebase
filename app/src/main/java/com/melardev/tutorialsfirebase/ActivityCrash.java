package com.melardev.tutorialsfirebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.crash.FirebaseCrash;


public class ActivityCrash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash);
    }

    public void report(View view) {
        FirebaseCrash.logcat(Log.INFO, "INFO_LOGCAT", "nullpointer has ocurred");
        FirebaseCrash.log("LogExample");
        try {
            throw new NullPointerException();
        } catch (NullPointerException ex) {
            //FirebaseCrash.report(ex);
            FirebaseCrash.logcat(Log.ERROR, "ERROR_LOGCAT", "NPE caught");
        }
    }
}
