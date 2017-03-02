package com.melardev.tutorialsfirebase;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


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
        registerTokenServer(token);
    }

    private void registerTokenServer(String token) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("token", token)
                .build();

        Request request = new Request.Builder()
                .url("http://localhost:3333/firebase/register.php")
                .post(body)
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}