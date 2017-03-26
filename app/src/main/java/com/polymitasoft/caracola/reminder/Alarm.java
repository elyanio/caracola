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
        // tomadas de las preferencias
        int preftime = 10;
        int prefDayBefore = 0;

        int prefTimeMilli = 1000 * 60 * 60 * preftime;

        LocalDate checkInDate = booking.getCheckInDate();
        LocalDate minusDay = checkInDate.minusDays(prefDayBefore);
        long minusDayMilli = minusDay.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();

        long time = prefTimeMilli + minusDayMilli;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("idBooking", booking.getId());
        int id = booking.getId();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_ONE_SHOT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);

    }

    public void cancelAlarm(Booking booking) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        int id = booking.getId();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_ONE_SHOT);
        alarmManager.cancel(pendingIntent);
    }

}
