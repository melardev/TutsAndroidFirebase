package com.melardev.tutorialsfirebase.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.melardev.tutorialsfirebase.R;

public class ActivityPushNotificationsConsole extends AppCompatActivity {

    private TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_notifications);

        txt = (TextView) findViewById(R.id.txtResult);
//        FirebaseMessaging.getInstance().subscribeToTopic("Tutorials");
        if (getIntent().hasExtra("result")) {
            txt.setText(getIntent().getStringExtra("result"));
            return;
        }
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("token", token);
    }
}
