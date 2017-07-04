package com.melardev.tutorialsfirebase.service;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.melardev.tutorialsfirebase.activities.ActivityPushNotificationsServer;

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

        ActivityPushNotificationsServer.registerToken(token);
    }

}