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
    private final File directory = new File(getExternalStorageDirectory().getAbsolutePath() + "/Caracola");
    private final File dbFile = new File(directory.getAbsolutePath() + "/caracola.db");
    private static final int DB_VERSION = 2;

    public EntityDataStore<Persistable> getDataStore() {
        if (entityDataStore == null) {
            entityDataStore = createDataStore(CaracolaApplication.instance(), dbFile);
        }
        return entityDataStore;
    }

    public File getDbFile() {
        return dbFile;
    }

    public boolean existsDbFile() {
        return dbFile.exists();
    }

    private EntityDataStore<Persistable> createDataStore(Context context, File dbFile) {
        String dbName = dbFile.getAbsolutePath();
        Configuration configuration;
        if (ENCRYPTION_ENABLED) {
            SqlCipherDatabaseSource source =
                    new SqlCipherDatabaseSource(context, Models.DEFAULT, dbName, "PasswordParaPruebas", DB_VERSION) {
                        @Override
                        public void onOpen(SQLiteDatabase db) {
                            super.onOpen(db);
                            db.execSQL("PRAGMA foreign_keys=ON;");
                        }
                    };
            configuration = source.getConfiguration();
        } else {
            DatabaseSource source =
                    new DatabaseSource(context, Models.DEFAULT, dbName, DB_VERSION);
            configuration = source.getConfiguration();
        }

        return new EntityDataStore<>(configuration);
    }
}