package com.polymitasoft.caracola.dataaccess;

import android.content.Context;

import com.polymitasoft.caracola.CaracolaApplication;
import com.polymitasoft.caracola.datamodel.Models;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;

import io.requery.Persistable;
import io.requery.android.sqlcipher.SqlCipherDatabaseSource;
import io.requery.android.sqlite.DatabaseSource;
import io.requery.sql.Configuration;
import io.requery.sql.EntityDataStore;

import static android.os.Environment.getExternalStorageDirectory;

/**
 * @author rainermf
 * @since 12/2/2017
 */
public enum DataStoreHolder {
    INSTANCE;

    private static final boolean ENCRYPTION_ENABLED = true;
    private EntityDataStore<Persistable> entityDataStore;

    @Deprecated
    public static DataStoreHolder getInstance() {
        return INSTANCE;
    }

    @Deprecated
    public EntityDataStore<Persistable> getDataStore(Context context) {
        return getDataStore();
    }

    public EntityDataStore<Persistable> getDataStore() {
        if (entityDataStore == null) {
            File directory = new File(getExternalStorageDirectory().getAbsolutePath() + "/Hostel");
            File dbFile = new File(directory.getAbsolutePath() + "/hostels.db");
            entityDataStore = createDataStore(CaracolaApplication.instance(), dbFile);
        }
        return entityDataStore;
    }

    private EntityDataStore<Persistable> createDataStore(Context context, File dbFile) {
        String dbName = dbFile.getAbsolutePath();
        Configuration configuration;
        if (ENCRYPTION_ENABLED) {
            SqlCipherDatabaseSource source =
                    new SqlCipherDatabaseSource(context, Models.DEFAULT, dbName, "PasswordParaPruebas", 1) {
                        @Override
                        public void onOpen(SQLiteDatabase db) {
                            super.onOpen(db);
                            db.execSQL("PRAGMA foreign_keys=ON;");
                        }
                    };
            configuration = source.getConfiguration();
        } else {
            DatabaseSource source =
                    new DatabaseSource(context, Models.DEFAULT, dbName, 1);
            configuration = source.getConfiguration();
        }

        return new EntityDataStore<>(configuration);
    }
}