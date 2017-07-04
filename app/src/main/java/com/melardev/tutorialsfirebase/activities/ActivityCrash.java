package com.melardev.tutorialsfirebase.activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.crash.FirebaseCrash;
import com.melardev.tutorialsfirebase.R;

import java.util.ArrayList;


public class ActivityCrash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash);
    }

    public void reportFatal(View view) {
        ArrayList<String> array = new ArrayList<String>();
        String value = array.get(2);
        Log.d("Log", value);
    }

    public void reportNonFatal(View view) {
        FirebaseCrash.logcat(Log.INFO, "INFO_LOGCAT", "ClassCastException will ocurr");
        FirebaseCrash.log("LogExample");
        try {
            throw new ClassCastException();
        } catch (ClassCastException ex) {

            FirebaseCrash.logcat(Log.ERROR, "ERROR_LOGCAT", "ClassCastException caught");
            FirebaseCrash.report(ex);
        }
    }

    public void reportNonFatal2(View view) {
        FirebaseCrash.logcat(Log.INFO, "INFO_LOGCAT", "RuntimeException will occur");
        FirebaseCrash.log("LogExample");
        try {
            throw new RuntimeException();
        } catch (RuntimeException ex) {
            FirebaseCrash.report(ex);
            FirebaseCrash.logcat(Log.ERROR, "ERROR_LOGCAT", "RuntimeException caught");

        }
    }
}
