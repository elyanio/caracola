package com.polymitasoft.caracola.dataaccess;

import android.content.Context;

import com.polymitasoft.caracola.CaracolaApplication;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

/**
 * @author rainermf
 * @since 12/2/2017
 * @deprecated Esta clase está obsoleta, debe obtener el DataStore de la clase aplicación
 */
@Deprecated
public class DataStoreHolder {

    private static DataStoreHolder instance;

    private DataStoreHolder() {}

    public static DataStoreHolder getInstance() {
        if(instance == null) {
            instance = new DataStoreHolder();
        }
        return instance;
    }

    public EntityDataStore<Persistable> getDataStore(Context context) {
        return CaracolaApplication.instance().getDataStore();
    }
}