package com.polymitasoft.caracola.dataaccess;

import com.google.common.primitives.Ints;
import com.polymitasoft.caracola.datamodel.Hostel;
import com.polymitasoft.caracola.datamodel.Manager;

import io.requery.Persistable;
import io.requery.query.Result;
import io.requery.sql.EntityDataStore;

/**
 * Created by rainermf on 13-Mar-17.
 */

public class HostelDao {

    private EntityDataStore<Persistable> dataStore;

    public HostelDao() {
        this.dataStore = DataStoreHolder.INSTANCE.getDataStore();
    }

    public Result<Manager> getManagers(Hostel hostel) {
        return dataStore.select(Manager.class)
                .where(Manager.HOSTEL.eq(hostel))
                .orderBy(Manager.NAME.lower())
                .get();
    }

    public int getManagerCount(Hostel hostel) {
        return dataStore.count(Manager.class)
                .where(Manager.HOSTEL.equal(hostel))
                .get()
                .value();
    }
}
