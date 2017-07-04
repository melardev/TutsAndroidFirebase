package com.melardev.tutorialsfirebase.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.appcompat.BuildConfig;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.melardev.tutorialsfirebase.R;

public class ActivityRemoteConfig extends AppCompatActivity {

    private static final java.lang.String PERF_ENABLE = "performance_enabled";
    private static final java.lang.String MSG_DAILY = "msg_of_the_day";
    private FirebaseRemoteConfig remoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_config);

        remoteConfig = FirebaseRemoteConfig.getInstance();

        //Build a developer mode RemoteConfigSettings, increasing the number of requests we can make during development.
        //In the future class this will offer more features
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();

        remoteConfig.setConfigSettings(configSettings);

        //first to be safe you set the defaults
        remoteConfig.setDefaults(R.xml.remote_config_defaults);
        //then override the settings with the remote values
        fetchConfigs();
    }

    private void fetchConfigs() {
        long expirationTime;
        if (remoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled())
            expirationTime = 0; //we do not have that limit of fetches per hour, so each time we are going to fetch
        else
            expirationTime = 60 * 60; //1 hour since we have some limitation in the number of  fetches

        remoteConfig.fetch(expirationTime) //fetches configs either from local cache or remote config server (if LAST_FETCH_STATUS_SUCCESS > expirationTime)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ActivityRemoteConfig.this, "fetched succesfully", Toast.LENGTH_SHORT).show();
                            remoteConfig.activateFetched(); //after a fetched success, we have to activate the fetched config so they can be used(active config)

                            if (remoteConfig.getBoolean(PERF_ENABLE))
                                FirebasePerformance.getInstance().setPerformanceCollectionEnabled(true);
                            else
                                FirebasePerformance.getInstance().setPerformanceCollectionEnabled(false);

                            String tipOfDay = remoteConfig.getString(MSG_DAILY);
                            Toast.makeText(ActivityRemoteConfig.this, tipOfDay, Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(ActivityRemoteConfig.this, "Failed to fetch remote config", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
