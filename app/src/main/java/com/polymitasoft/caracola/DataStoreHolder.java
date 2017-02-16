package com.polymitasoft.caracola;

import android.app.Application;
import android.content.Context;

import com.polymitasoft.caracola.datamodel.Models;

import java.io.File;

import io.requery.Persistable;
import io.requery.android.sqlite.DatabaseSource;
import io.requery.sql.Configuration;
import io.requery.sql.EntityDataStore;

import static android.os.Environment.getExternalStorageDirectory;

/**
 * Created by rainermf on 12/2/2017.
 */

public class DataStoreHolder {

    private EntityDataStore<Persistable> entityDataStore;
    private File directory = new File(getExternalStorageDirectory().getAbsolutePath() + "/Hostel");
    private File dbFile = new File(directory.getAbsolutePath() + "/hostels.db");
    private static DataStoreHolder instance;

    private DataStoreHolder() {}

    public static DataStoreHolder getInstance() {
        if(instance == null) {
            instance = new DataStoreHolder();
        }
        return instance;
    }

    public EntityDataStore<Persistable> getDataStore(Context context) {
        if(entityDataStore == null) {
            String dbName = dbFile.getAbsolutePath().toString();
            DatabaseSource source = new DatabaseSource(context, Models.DEFAULT, dbName, 1);
            Configuration configuration = source.getConfiguration();
            entityDataStore = new EntityDataStore<>(configuration);
        }
        return entityDataStore;
    }
}