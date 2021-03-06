package com.polymitasoft.caracola.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.polymitasoft.caracola.datamodel.Booking;
import com.polymitasoft.caracola.datamodel.Client;
import com.polymitasoft.caracola.settings.Preferences;

import org.threeten.bp.LocalDate;
import org.threeten.bp.ZoneId;

import java.util.List;

/**
 * Created by asio on 3/25/2017.
 */

public class Alarm {
    private Context context;
    public static final String ID_BOOKING_ALARM = "idBooking";
    public static final String ID_CLIENT_ALARM = "idCliente";
    public static final String PRE_BOOKING_ALARM = "1";
    public static final String PRE_CLIENT_ALARM = "2";


    public Alarm(Context context) {
        this.context = context;
    }

    public void setAlarm(Booking booking) {
        // tomadas de las preferencias
//        LocalTime timeReminder = Preferences.getTimeReminder();
//        int hour = 1000 * 60 * 60 * timeReminder.getHour();
//        Log.e("hour",timeReminder.getHour() + "");
//        Log.e("minut",timeReminder.getMinute() + "");
//        Log.e("segun",timeReminder.getSecond() + "");
//        int minute = 1000 * 60 * timeReminder.getMinute();
//        int second = 1000 * timeReminder.getSecond();
//        int prefTimeMilli = hour + minute + second;

        //pref tiene la cantidad de milisegundos de las 12:00am hasta las 10:00am
        int hour = 1000 * 60 * 60 * 10;
        int minute = 0;
        int second = 0;
        int prefTimeMilli = hour + minute + second;

        int prefDayBefore = Preferences.getDayBeforeReminder();
        LocalDate checkInDate = booking.getCheckInDate();
        LocalDate minusDay = checkInDate.minusDays(prefDayBefore);
        long minusDayMilli = minusDay.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();

        // tiempo cuando debe sonar la alarma
        long time = prefTimeMilli + minusDayMilli;
        if (time >= System.currentTimeMillis()) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra(ID_BOOKING_ALARM, booking.getId());
            String idPrefijo = PRE_BOOKING_ALARM + booking.getId();
            int id = Integer.parseInt(idPrefijo);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_ONE_SHOT);
            alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        }
    }

    public void setAlarmBirthday(List<Client> clients) {
        //pref tiene la cantidad de milisegundos de las 12:00am hasta las 10:00am
        int hour = 1000 * 60 * 60 * 10;
        int minute = 0;
        int second = 0;
        int prefTimeMilli = hour + minute + second;

        for (Client client : clients) {

            int prefDayBefore = Preferences.getDayBeforeReminderBirthday();
            LocalDate checkInDate = client.getBirthday();
            LocalDate minusDay = checkInDate.minusDays(prefDayBefore);
            long minusDayMilli = minusDay.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();

            // tiempo cuando debe sonar la alarma
            long time = prefTimeMilli + minusDayMilli;
            if (time >= System.currentTimeMillis()) {
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(context, AlarmReceiver.class);
                intent.putExtra(ID_CLIENT_ALARM, client.getId());
                String idPrefijo = PRE_CLIENT_ALARM + client.getId();
                int id = Integer.parseInt(idPrefijo);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_ONE_SHOT);
                alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
            }
        }
    }

    public void cancelAlarm(Booking booking) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        int id = booking.getId();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_ONE_SHOT);
        alarmManager.cancel(pendingIntent);
    }

}
