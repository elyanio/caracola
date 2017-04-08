package com.polymitasoft.caracola.reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.polymitasoft.caracola.dataaccess.BookingDao;
import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.datamodel.Booking;
import com.polymitasoft.caracola.datamodel.Client;
import com.polymitasoft.caracola.notification.StateBar;
import com.polymitasoft.caracola.settings.Preferences;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.List;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

/**
 * Created by asio on 3/25/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    private static DateTimeFormatter format = DateTimeFormatter.ofPattern("y/M/d");

    @Override
    public void onReceive(Context context, Intent intent) {
        int typeAlarm = intent.getIntExtra(Alarm.ID_BOOKING_ALARM, -1);
        if (typeAlarm != -1) {
            if (Preferences.isEnableReminder()) {
                int id = intent.getIntExtra(Alarm.ID_BOOKING_ALARM, 0);
                BookingDao bookingDao = new BookingDao();
                Booking booking = bookingDao.findBooking(id);
                StateBar stateBar = new StateBar();
                String mensaj = "Reserva en " + booking.getBedroom().getName() + " desde " + booking.getCheckInDate().format(format) + " hasta " + booking.getCheckOutDate().format(DateTimeFormatter.BASIC_ISO_DATE);
                stateBar.BookingNotificationService(context, id, "Recordatorio de Reserva", "Recordatorio de Reserva", mensaj);
            }
        } else {
            if (Preferences.isEnableReminderBirthday()) {
                int id = intent.getIntExtra(Alarm.ID_CLIENT_ALARM, 0);
                EntityDataStore<Persistable> dataStore = DataStoreHolder.INSTANCE.getDataStore();
                Client client = dataStore.select(Client.class).where(Client.ID.eq(id)).get().first();

                StateBar stateBar = new StateBar();
                String mensaj = "";
                if (Preferences.getDayBeforeReminderBirthday() == 0) {
                    mensaj = "Hoy es el cumpleaños de " + client.getFirstName() + " " + client.getLastName();
                } else if (Preferences.getDayBeforeReminderBirthday() == 1) {
                    mensaj = "Falta un día para el cumpleaños de " + client.getFirstName() + " " + client.getLastName();
                } else {
                    mensaj = "Faltan" + Preferences.getDayBeforeReminderBirthday() + "días para el cumpleaños de " + client.getFirstName() + " " + client.getLastName();
                }
                stateBar.BookingNotificationService(context, id, "Recordatorio de cumpleaños", "Recordatorio de cumpleaños", mensaj);
            }
        }
    }
}
