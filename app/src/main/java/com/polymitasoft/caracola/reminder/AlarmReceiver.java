package com.polymitasoft.caracola.reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.polymitasoft.caracola.dataaccess.BookingDao;
import com.polymitasoft.caracola.datamodel.Booking;
import com.polymitasoft.caracola.notification.StateBar;
import com.polymitasoft.caracola.settings.Preferences;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.List;

/**
 * Created by asio on 3/25/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    private static DateTimeFormatter format = DateTimeFormatter.ofPattern("y/M/d");
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Preferences.isEnableReminder()) {
            int id = intent.getIntExtra(Alarm.ID_BOOKING_ALARM,0);
            BookingDao bookingDao = new BookingDao();
            Booking booking = bookingDao.findBooking(id);
            StateBar stateBar = new StateBar();
            String mensaj =  "Reserva en " + booking.getBedroom().getName() + " desde " + booking.getCheckInDate().format(format) + " hasta " + booking.getCheckOutDate().format(DateTimeFormatter.BASIC_ISO_DATE) ;
            stateBar.BookingNotificationService(context, id, "Recordatorio de Reserva", "Recordatorio de Reserva", mensaj);
        }
    }
}
