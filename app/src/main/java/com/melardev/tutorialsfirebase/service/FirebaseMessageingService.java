package com.melardev.tutorialsfirebase.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.melardev.tutorialsfirebase.ActivityPushServer;

import java.util.Map;

public class FirebaseMessageingService extends FirebaseMessagingService {
    public FirebaseMessageingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String result = "From : " + remoteMessage.getFrom() + "\nMessageId = " + remoteMessage.getMessageId() + "\nMessageType =  " + remoteMessage.getMessageType()
                + "\nCollapeseKey = " + remoteMessage.getCollapseKey() + "\nTo: " + remoteMessage.getTo() + "\nTtl = " + remoteMessage.getTtl()
                + "\nSent Time = " + remoteMessage.getSentTime();/*+"\nTitle = " + remoteMessage.getNotification().getTitle()
                + "\nBody = " + remoteMessage.getNotification().getBody()*/
        Map<String, String> map = remoteMessage.getData();
        for (String key : map.keySet())
            result += "\n(" + key + "," + map.get(key) + ")";

        Intent intent = new Intent(this, ActivityPushServer.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("result", result);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("Firebase Cloud Messaging Demo")
                .setSmallIcon(android.R.drawable.stat_notify_chat)
                .setContentIntent(pi);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
}
