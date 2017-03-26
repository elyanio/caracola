package com.polymitasoft.caracola.recordatory;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Created by asio on 3/25/2017.
 */

public class Alarm {
    private Context context;
    private static final int ALARM_REQUEST_CODE = 1;

    public Alarm(Context context) {
        this.context = context;
    }

    public void setAlarm() {
        long time = 5000;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ALARM_REQUEST_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
    }
}
