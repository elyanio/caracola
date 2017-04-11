package com.polymitasoft.caracola.components;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Window;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.dataaccess.DatabaseSetup;
import com.polymitasoft.caracola.view.booking.ReservaPrincipal;

import java.util.Timer;
import java.util.TimerTask;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class SplashScreenActivity extends Activity {

    // Set the duration of the splash screen
    private static final long SPLASH_SCREEN_DELAY = 500;
    private static final int REQUEST_WRITE_ES = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_screen);

        if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) == PERMISSION_GRANTED) {
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    init();
                }
            };
            Timer timer = new Timer();
            timer.schedule(task, 0);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_ES);
        }
    }

    private void init() {
        if (!DataStoreHolder.INSTANCE.existsDbFile()) {
            new DatabaseSetup().mockDatabase();
        }
        Intent mainIntent = new Intent().setClass(this, ReservaPrincipal.class);
        startActivity(mainIntent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_WRITE_ES) {
            if (grantResults[0] == PERMISSION_GRANTED) {
                init();
            } else {
                new AlertDialog.Builder(this)
                        .setMessage("La aplicación deberá cerrarse por no contar con los permisos necesarios para funcionar correctamente.")
                        .setPositiveButton(R.string.ok_action_button, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                finishAffinity();
                            }
                        })
                        .show();
            }
        }
    }

}