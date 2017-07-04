package com.melardev.tutorialsfirebase.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.appinvite.FirebaseAppInvite;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.melardev.tutorialsfirebase.R;

public class ActivityDynamicLink extends AppCompatActivity {

    private final String TAG = getClass().getName();
    private FirebaseAnalytics analytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_link);


        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(AppInvite.API)
                .build();

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        if (pendingDynamicLinkData != null) {
                            //init analytics if you want to get analytics from your dynamic links
                            analytics = FirebaseAnalytics.getInstance(ActivityDynamicLink.this);

                            Uri deepLink = pendingDynamicLinkData.getLink();
                            Toast.makeText(ActivityDynamicLink.this, "onSuccess called " + deepLink.toString(), Toast.LENGTH_SHORT).show();
                            //logic here, redeem code or whatever

                            FirebaseAppInvite invite = FirebaseAppInvite.getInvitation(pendingDynamicLinkData);
                            if (invite != null) {
                                String invitationId = invite.getInvitationId();
                                if (!TextUtils.isEmpty(invitationId))
                                    Toast.makeText(ActivityDynamicLink.this, "invitation Id " + invitationId, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ActivityDynamicLink.this, "onFailure", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void shareLongDynamicLink(View view) {
        Intent intent = new Intent();
        String msg = "visit my awesome website: " + buildDynamicLink();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, msg);
        intent.setType("text/plain");
        startActivity(intent);
    }

    private String buildDynamicLink(/*String link, String description, String titleSocial, String source*/) {
        //more info at https://firebase.google.com/docs/dynamic-links/create-manually

        /*String path = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setDynamicLinkDomain("m9guj.app.goo.gl")
                .setLink(Uri.parse("https://youtube.com/c/Melardev"))
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build()) //com.melardev.tutorialsfirebase
                .setSocialMetaTagParameters(new DynamicLink.SocialMetaTagParameters.Builder().setTitle("Share this App").setDescription("blabla").build())
                .setGoogleAnalyticsParameters(new DynamicLink.GoogleAnalyticsParameters.Builder().setSource("AndroidApp").build())
                .buildDynamicLink().getUri().toString();*/

        return "https://m9guj.app.goo.gl/?" +
                "link=" + /*link*/
                "https://youtube.com/c/Melardev" +
                "&apn=" + /*getPackageName()*/
                "com.melardev.tutorialsfirebase" +
                "&st=" + /*titleSocial*/
                "Share+this+App" +
                "&sd=" + /*description*/
                "looking+to+learn+how+to+use+Firebase+in+Android?+this+app+is+what+you+are+looking+for." +
                "&utm_source=" + /*source*/
                "AndroidApp";
    }

    public void shareShortDynamicLink(View view) {
        Task<ShortDynamicLink> createLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(Uri.parse(buildDynamicLink()))
                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink(); //flowchart link is a debugging URL

                            Log.d(TAG, shortLink.toString());
                            Log.d(TAG, flowchartLink.toString());
                            Intent intent = new Intent();
                            String msg = "visit my awesome website: " + shortLink.toString();
                            intent.setAction(Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_TEXT, msg);
                            intent.setType("text/plain");
                            startActivity(intent);

                        } else {
                            // Error
                            Toast.makeText(ActivityDynamicLink.this, "Error building short link", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
