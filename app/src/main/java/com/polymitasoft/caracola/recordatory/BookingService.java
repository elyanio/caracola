package com.polymitasoft.caracola.recordatory;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.datamodel.Booking;
import com.polymitasoft.caracola.notification.StateBar;

import org.threeten.bp.LocalDate;
import org.threeten.bp.ZoneId;

import java.util.Timer;
import java.util.TimerTask;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

/**
 * Created by asio on 3/19/2017.
 */

public class BookingService extends Service {

    static final int UPDATE_INTERVAL = 60000 * 60;
    private Timer timer = new Timer();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        checkBooking();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (timer != null) {
            timer.cancel();
        }
        Toast.makeText(this, "Service Stoped", Toast.LENGTH_LONG).show();
    }

    private void checkBooking() {
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {

                if (findBooking()) {
                    StateBar stateBar = new StateBar();
                    stateBar.BookingNotificationService(getBaseContext(), 3, "Recordatorio de Reserva", "Nueva Reserva programada", "Usted tiene una reserva programada para el día de hoy.");

                    long nextDay = LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();

                }
            }
        }, 0, UPDATE_INTERVAL);
    }

    private boolean findBooking() {

        LocalDate now = LocalDate.now();
        EntityDataStore<Persistable> dataStore = DataStoreHolder.INSTANCE.getDataStore();
        Booking booking = dataStore.select(Booking.class).where(Booking.CHECK_IN_DATE.eq(now)).get().firstOrNull();

        if (booking == null) {
            return false;
        }
        return true;
    }


}
