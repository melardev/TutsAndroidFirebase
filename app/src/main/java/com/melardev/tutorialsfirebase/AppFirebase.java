package com.melardev.tutorialsfirebase;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

/**
 * Created by melardev on 6/30/2017.
 */

public class AppFirebase extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        Crashlytics.setUserName("Melardev_tester");
        Crashlytics.setUserEmail("melardev@test.com");
        Crashlytics.setUserIdentifier("owner");
    }
}
