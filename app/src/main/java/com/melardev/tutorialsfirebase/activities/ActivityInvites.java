package com.melardev.tutorialsfirebase.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.gms.appinvite.AppInviteInvitation;
import com.melardev.tutorialsfirebase.R;

public class ActivityInvites extends AppCompatActivity {

    private static final int RC_INVITE = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invites);
    }

    public void inviteFriend(View view) {
        Intent intent = new AppInviteInvitation.IntentBuilder("Check this awesome app!")
                .setMessage("Hi!, have you tested the new TutorialsFirebase App! check this out!")
                .setDeepLink(Uri.parse("https://m9guj.app.goo.gl/j6mu"))
                .setCustomImage(Uri.parse("http://www.iconsdb.com/icons/preview/orange/gift-3-xxl.png"))
                .setCallToActionText("LET'S GIVE IT A TRy!")
                //.setOtherPlatformsTargetApplication(AppInviteInvitation.IntentBuilder.PlatformMode.PROJECT_PLATFORM_IOS, "IOS_APP_CLIENT_ID")
                .build();
        startActivityForResult(intent, RC_INVITE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_INVITE) {
            if (resultCode == RESULT_OK) {
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                String message = "Invitations sent to the following ids : ";
                for (String id : ids) {
                    message += id + ", ";
                }
                Toast.makeText(this, message.substring(0, message.length() - 1 - 1/*do not show last comma*/), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "invitation failed to send or canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
