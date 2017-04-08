package com.polymitasoft.caracola.dataaccess;

import com.polymitasoft.caracola.datamodel.Bedroom;
import com.polymitasoft.caracola.datamodel.Booking;

import org.threeten.bp.LocalDate;

import io.requery.Persistable;
import io.requery.query.Result;
import io.requery.sql.EntityDataStore;

/**
 * @author rainermf
 * @since 8/4/2017
 */

public class BedroomDao {

    private EntityDataStore<Persistable> dataStore;

    public BedroomDao() {
        this.dataStore = DataStoreHolder.INSTANCE.getDataStore();
    }

    public Result<Bedroom> getBedrooms() {
        return dataStore.select(Bedroom.class).orderBy(Bedroom.NAME).get();
    }

    public Result<Booking> getBookings(Bedroom bedroom, LocalDate startDate, LocalDate endDate) {
        return dataStore.select(Booking.class)
                .where(Booking.BEDROOM_ID.equal(bedroom.getId()))
                .and(Booking.CHECK_OUT_DATE.greaterThanOrEqual(startDate))
                .and(Booking.CHECK_IN_DATE.lessThanOrEqual(endDate))
                .orderBy(Booking.CHECK_IN_DATE)
                .get();
    }
}
