package com.melardev.tutorialsfirebase.activities;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.melardev.tutorialsfirebase.R;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ActivityAnalytics extends AppCompatActivity {

    private static final String EVT_RANDOM_NUMBER = "random_number_generator";
    FirebaseAnalytics firebaseAnalytics;
    private Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.TUTORIAL_BEGIN, null);
        setupFirebaseAnalytics();
        random = new Random();
    }

    private void setupFirebaseAnalytics() {
        String osVersion = Build.VERSION.RELEASE;
        String model = Build.MANUFACTURER + " - " + Build.MODEL;
        firebaseAnalytics.setMinimumSessionDuration(10 * 1000); //10seconds in this app before starting a session and collecting data
        firebaseAnalytics.setSessionTimeoutDuration(30 * 60 * 1000); //30 minutes of inactivity will lead to termination of this session
        firebaseAnalytics.setUserProperty("osVersion", osVersion); //useless because the Firebase SDK already gives us this info.
        firebaseAnalytics.setUserProperty("deviceModel", model); //useless because the Firebase SDK already gives us this info.
        firebaseAnalytics.setUserProperty("nonExistingProperty", "whatWillHappen????"); //useless because the Firebase SDK already gives us this info.

    }

    public void logEvent(View view) {

        Bundle bundle = new Bundle();
        bundle.putInt("randomAPI_1", random.nextInt(1000 + 1));
        bundle.putInt("randomAPI_2", random.nextInt(1000 + 1));
        bundle.putInt("randomAPI_3", random.nextInt(1000 + 1));

        //custom event
        firebaseAnalytics.logEvent(EVT_RANDOM_NUMBER, bundle);

        if (false) {
            Bundle bundle2 = new Bundle();
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.REMOVE_FROM_CART, bundle2);
            firebaseAnalytics.logEvent(FirebaseAnalytics.Param.LOCATION, bundle2);
        }
    }

    @Override
    protected void onStop() {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.TUTORIAL_COMPLETE, null);
        super.onStop();
    }
}
