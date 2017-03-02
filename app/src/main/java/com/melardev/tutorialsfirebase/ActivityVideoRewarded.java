package com.melardev.tutorialsfirebase;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class ActivityVideoRewarded extends AppCompatActivity implements RewardedVideoAdListener {

    private RewardedVideoAd adReward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_rewarded);
        MobileAds.initialize(this, getString(R.string.app_id));
        adReward = MobileAds.getRewardedVideoAdInstance(this);
        adReward.setRewardedVideoAdListener(this);


        AdRequest adRequest = new AdRequest.Builder().build();
        adReward.loadAd(getResources().getString(R.string.rewarded_aui), adRequest);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (adReward.isLoaded())
                    adReward.show();
                else
                    handler.postDelayed(this, 300);
            }
        }, 300);
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        Toast.makeText(this, "AdLoaded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdOpened() {
        Toast.makeText(this, "Started", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoStarted() {
        Toast.makeText(this, "Started", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdClosed() {
        Toast.makeText(this, "Closed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        Toast.makeText(this, rewardItem.getAmount(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        Toast.makeText(this, "onRewardedVideoAdLeftApplication", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
        Toast.makeText(this, "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        adReward.pause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adReward.resume(this);
    }

}
