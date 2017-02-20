package com.polymitasoft.caracola.dataaccess;

import android.support.annotation.NonNull;
import android.util.Log;

import com.polymitasoft.caracola.datamodel.Client;
import com.polymitasoft.caracola.datamodel.ClientStay;
import com.polymitasoft.caracola.datamodel.Bedroom;
import com.polymitasoft.caracola.datamodel.Booking;
import com.polymitasoft.caracola.datamodel.ClientStayEntity;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

import static com.polymitasoft.caracola.datamodel.BookingEntity.BEDROOM;
import static com.polymitasoft.caracola.datamodel.BookingEntity.CHECK_IN_DATE;
import static com.polymitasoft.caracola.datamodel.BookingEntity.CHECK_OUT_DATE;

/**
 * @author rainermf
 * @since 12/2/2017
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

    public LocalDate previousBookedDay(Bedroom bedroom, LocalDate date) {
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

    public LocalDate nextBookedDay(Bedroom bedroom, LocalDate date) {
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

    public List<Client> getClients(@NonNull Booking booking) {
        List<ClientStay> clientStays = dataStore.select(ClientStay.class)
                .where(ClientStayEntity.BOOKING.eq(booking)).get().toList();
        List<Client> clients = new ArrayList<>(clientStays.size());
        for (ClientStay stay: clientStays) {
            clients.add(stay.getClient());
        }
        return clients;
    }
}
