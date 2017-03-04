package com.polymitasoft.caracola.components;


import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;

import com.polymitasoft.caracola.BuildConfig;
import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.view.booking.ReservaPrincipal;

public class SplashScreenActivity extends Activity {

    // Set the duration of the splash screen
    private static final long SPLASH_SCREEN_DELAY = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (BuildConfig.DEBUG) {
            Intent mainIntent = new Intent().setClass(
                    SplashScreenActivity.this, ReservaPrincipal.class);
            startActivity(mainIntent);
            finish();
        }else{
            // do something for a debug build
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            // Hide title bar
            requestWindowFeature(Window.FEATURE_NO_TITLE);

            setContentView(R.layout.splash_screen);
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    Intent mainIntent = new Intent().setClass(
                            SplashScreenActivity.this, ReservaPrincipal.class);
                    startActivity(mainIntent);
                    // Close the activity so the user won't able to go back this
                    // activity pressing Back button
                    finish();
                }
            };

            // Simulate a long loading process on application startup.
            Timer timer = new Timer();
            timer.schedule(task, SPLASH_SCREEN_DELAY);
            // Start the next activitys
        }
        // Set portrait orientation


    }

}