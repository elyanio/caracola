package com.polymitasoft.caracola;

import android.app.Application;
import android.os.StrictMode;

import com.jakewharton.threetenabp.AndroidThreeTen;
import com.polymitasoft.caracola.dataaccess.DataStoreHolder;

import java.util.Locale;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

/**
 * @author rainermf
 * @since 20/2/2017
 */
public class CaracolaApplication extends Application {

    private static CaracolaApplication instance;

    public static CaracolaApplication instance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }
        instance = this;
        AndroidThreeTen.init(this);
        Locale.setDefault(new Locale("es"));
    }
}
