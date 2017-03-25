package com.melardev.tutorialsfirebase.service;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.melardev.tutorialsfirebase.ActivityPushServer;


import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;



/*
to handle the creation, rotation, and updating of registration tokens. This is required for sending to specific devices or for creating device groups.
*/
public class FirebaseInstanceIDService extends FirebaseInstanceIdService {
    public FirebaseInstanceIDService() {
    }

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token = FirebaseInstanceId.getInstance().getToken();

        ActivityPushServer.registerToken(token);
    }

}