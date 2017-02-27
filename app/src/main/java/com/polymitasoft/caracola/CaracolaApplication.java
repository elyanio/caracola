package com.polymitasoft.caracola;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;

import com.jakewharton.threetenabp.AndroidThreeTen;
import com.polymitasoft.caracola.components.Colors;
import com.polymitasoft.caracola.datamodel.Models;

import java.io.File;
import java.util.Locale;

import io.requery.Persistable;
import io.requery.android.sqlcipher.SqlCipherDatabaseSource;
import io.requery.sql.Configuration;
import io.requery.sql.EntityDataStore;

import static android.os.Environment.getExternalStorageDirectory;

/**
 * @author rainermf
 * @since 20/2/2017
 */
public class CaracolaApplication extends Application {

    private static CaracolaApplication instance;
    private EntityDataStore<Persistable> entityDataStore;

    public static CaracolaApplication instance() {
        return instance;
    }

    @Override
    public void onCreate() {
        instance = this;
        AndroidThreeTen.init(this);
        Locale.setDefault(new Locale("es"));
        Colors.INSTANCE.setColors(getRibbonColors());

        File directory = new File(getExternalStorageDirectory().getAbsolutePath() + "/Hostel");
        File dbFile = new File(directory.getAbsolutePath() + "/hostels.db");
        createDataStore(this, dbFile);
    }

    private int[] getRibbonColors() {
        Resources res = getResources();
        TypedArray colorTypedArray = res.obtainTypedArray(R.array.account_colors);
        int[] colorOptions = new int[colorTypedArray.length()];
        for (int i = 0; i < colorTypedArray.length(); i++) {
            int color = colorTypedArray.getColor(i, getResources().getColor(R.color.colorPrimary));
            colorOptions[i] = color;
        }
        colorTypedArray.recycle();
        return colorOptions;
    }

    void createDataStore(Context context, File dbFile) {
        String dbName = dbFile.getAbsolutePath();
        SqlCipherDatabaseSource source =
                new SqlCipherDatabaseSource(context, Models.DEFAULT, dbName, "PasswordParaPruebas", 1);
        Configuration configuration = source.getConfiguration();

        entityDataStore = new EntityDataStore<>(configuration);
    }

    public EntityDataStore<Persistable> getDataStore() {
        return entityDataStore;
    }
}
