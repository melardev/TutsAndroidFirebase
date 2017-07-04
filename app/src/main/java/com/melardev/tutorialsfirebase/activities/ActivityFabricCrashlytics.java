package com.melardev.tutorialsfirebase.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.melardev.tutorialsfirebase.R;

import io.fabric.sdk.android.Fabric;

public class ActivityFabricCrashlytics extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_fabric_crashlytics);

        //in the dashboard we are going to see this info
    }

    public void forceCrash(View view) {
        throw new RuntimeException("this is a crash");
    }
}
