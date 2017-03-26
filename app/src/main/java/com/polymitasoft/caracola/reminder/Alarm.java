package com.polymitasoft.caracola.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.polymitasoft.caracola.datamodel.Booking;

import org.threeten.bp.LocalDate;
import org.threeten.bp.ZoneId;

/**
 * Created by asio on 3/25/2017.
 */

public class Alarm {
    private Context context;
    private static final int ALARM_REQUEST_CODE = 1;

    public Alarm(Context context) {
        this.context = context;
    }

    public void setAlarm(Booking booking) {
        int preftime = 10;
        int prefTimeMilli = 1000 * 60 * 60 * preftime;

        int prefDayBefore = 0;
        LocalDate checkInDate = booking.getCheckInDate();
        LocalDate minusDay = checkInDate.minusDays(prefDayBefore);
        long minusDayMilli = minusDay.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();


        long time = prefTimeMilli + minusDayMilli;
//        long time = 60000;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ALARM_REQUEST_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
    }


}
