package com.melardev.tutorialsfirebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.NativeExpressAdView;

public class ActivityAds extends AppCompatActivity {

    private AdView adBanner;
    private InterstitialAd adInterstitial;
    private NativeExpressAdView adNativeExpress;
    private AdRequest adRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);
        MobileAds.initialize(getApplicationContext(), getResources().getString(R.string.app_id));
        adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adBanner = (AdView) findViewById(R.id.ad_banner);
        adNativeExpress = (NativeExpressAdView) findViewById(R.id.adNativeExpress);

        adInterstitial = new InterstitialAd(this);
        adInterstitial.setAdUnitId(getResources().getString(R.string.intersticial_aui));
        adInterstitial.loadAd(adRequest);
        adInterstitial.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if(adInterstitial.isLoaded())
                    adInterstitial.show();
            }

            @Override
            public void onAdClosed() {
                loadOtherAds();
            }
        });

    }

    private void loadOtherAds() {
        adBanner.loadAd(adRequest);
        adNativeExpress.loadAd(adRequest);
    }

    @Override
    protected void onPause() {
        if (adBanner != null)
            adBanner.pause();

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adBanner != null)
            adBanner.resume();

    }

    @Override
    protected void onDestroy() {
        if (adBanner != null)
            adBanner.destroy();

        super.onDestroy();
    }
}
