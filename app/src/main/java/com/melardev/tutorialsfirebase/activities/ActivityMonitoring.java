package com.melardev.tutorialsfirebase.activities;

import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.AddTrace;
import com.google.firebase.perf.metrics.Trace;
import com.melardev.tutorialsfirebase.R;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ActivityMonitoring extends AppCompatActivity {

    private Random random;
    Handler mHandlerMainThread = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Toast.makeText(ActivityMonitoring.this, "Done ...", Toast.LENGTH_SHORT).show();
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring);
        random = new Random();
    }

    public void loadData(View view) {
        Trace tracer = FirebasePerformance.getInstance().newTrace("loadDataTrace");
        tracer.start();
        //load some data
        int value = random.nextInt(100);
        if (value < 100)
            tracer.incrementCounter("load_data_fails");
        else
            tracer.incrementCounter("load_data_successes");

        tracer.stop();
        mHandlerMainThread.sendEmptyMessage(0);
    }


    public void loadData2(View view) {
        final int min = 2 * 1000;
        final int max = 10 * 1000;
        new Thread(new Runnable() {
            @AddTrace(name = "loadData", enabled = true)
            @Override
            public void run() {
                try {
                    int timeToSleep;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        timeToSleep = ThreadLocalRandom.current().nextInt(min, max);
                    } else
                        timeToSleep = random.nextInt((max - min) + 1) + min;
                    Thread.sleep(timeToSleep);
                    mHandlerMainThread.sendEmptyMessage(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
