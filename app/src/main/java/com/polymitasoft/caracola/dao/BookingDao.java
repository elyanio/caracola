package com.polymitasoft.caracola.dao;

import android.util.Log;

import com.polymitasoft.caracola.datamodel.Booking;
import com.polymitasoft.caracola.datamodel.IBedroom;

import org.threeten.bp.LocalDate;

import java.util.List;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

import static com.polymitasoft.caracola.datamodel.Booking.BEDROOM;
import static com.polymitasoft.caracola.datamodel.Booking.CHECK_IN_DATE;
import static com.polymitasoft.caracola.datamodel.Booking.CHECK_OUT_DATE;

/**
 * Created by rainermf on 12/2/2017.
 */

public class BookingDao {

    private EntityDataStore<Persistable> dataStore;

    public BookingDao(EntityDataStore<Persistable> dataStore) {
        this.dataStore = dataStore;
    }

    public List<Booking> bookingsBetween(LocalDate firstDay, LocalDate lastDate) {
        LocalDate startDate = firstDay;
        LocalDate endDate = lastDate;

        if (firstDay.isAfter(lastDate)) {
            startDate = lastDate;
            endDate = firstDay;
        }

        return dataStore
                .select(Booking.class)
                .where(CHECK_IN_DATE.lessThanOrEqual(startDate).and(CHECK_OUT_DATE.greaterThanOrEqual(endDate)))
                .or(CHECK_IN_DATE.greaterThanOrEqual(startDate).and(CHECK_IN_DATE.lessThanOrEqual(endDate)))
                .or(CHECK_OUT_DATE.greaterThanOrEqual(startDate).and(CHECK_OUT_DATE.lessThanOrEqual(endDate)))
                .get().toList();
    }

    public LocalDate previousBookedDay(IBedroom bedroom, LocalDate date) {
        Booking booking = dataStore.select(Booking.class)
                .where(BEDROOM.eq(bedroom))
                .and(CHECK_OUT_DATE.lessThanOrEqual(date))
                .orderBy(CHECK_OUT_DATE.desc())
                .get()
                .firstOrNull();
        if(booking == null) {
            return LocalDate.MIN;
        }
        Log.e(BookingDao.class.toString(), date.toString() + "<- " + booking.getCheckOutDate());
        return booking.getCheckOutDate();
    }

    public LocalDate nextBookedDay(IBedroom bedroom, LocalDate date) {
        Booking booking = dataStore.select(Booking.class)
                .where(BEDROOM.eq(bedroom))
                .and(CHECK_IN_DATE.greaterThanOrEqual(date))
                .orderBy(CHECK_IN_DATE.asc())
                .get()
                .firstOrNull();
        if(booking == null) {
            return LocalDate.MAX;
        }
        Log.e(BookingDao.class.toString(), date.toString() + "-> " + booking.getCheckInDate());
        return booking.getCheckInDate();
    }
}
