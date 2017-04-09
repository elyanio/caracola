package com.polymitasoft.caracola.reminder;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.datamodel.Client;
import com.polymitasoft.caracola.settings.Preferences;

import org.threeten.bp.LocalDate;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

/**
 * Created by asio on 3/19/2017.
 */

public class BirthdayService extends Service {

    static final int UPDATE_INTERVAL = 1000 * 60 * 60 * 24;
    private Timer timer = new Timer();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        checkBirthday();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (timer != null) {
            timer.cancel();
        }
    }

    private void checkBirthday() {

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int reminderBirthday = Preferences.getDayBeforeReminderBirthday();
                LocalDate now = LocalDate.now();
                LocalDate time = now.plusDays(reminderBirthday + 1);
                List<Client> clientes = findClients(time);
                Alarm alarm = new Alarm(getBaseContext());
                alarm.setAlarmBirthday(clientes);
                Log.e("eje","eje");
            }
        }, 0, UPDATE_INTERVAL);
    }

    private List<Client> findClients(LocalDate birthDay) {
        EntityDataStore<Persistable> dataStore = DataStoreHolder.INSTANCE.getDataStore();
        return dataStore.select(Client.class).where(Client.BIRTHDAY.eq(birthDay)).get().toList();
    }


}
