package com.polymitasoft.caracola.reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.polymitasoft.caracola.notification.StateBar;
import com.polymitasoft.caracola.settings.Preferences;

/**
 * Created by asio on 3/25/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Preferences.isEnableReminder()) {
            StateBar stateBar = new StateBar();
            stateBar.BookingNotificationService(context, 3, "Recordatorio de Reserva", "Recordatorio de Reserva", "Usted tiene una reserva programada para el día de hoy.");
        }

    }
}
