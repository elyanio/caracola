package com.polymitasoft.caracola.communication;

import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.datamodel.Bedroom;
import com.polymitasoft.caracola.datamodel.Booking;
import com.polymitasoft.caracola.datamodel.BookingState;
import com.polymitasoft.caracola.datamodel.LocalDateConverter;
import com.polymitasoft.caracola.datamodel.Manager;
import com.polymitasoft.caracola.util.FormatUtils;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;
import java.util.List;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

/**
 * Created by asio on 2/25/2017.
 */

public class ManageSmsBooking {

    private static final int PENDING = 1;
    private static final int CONFIRMED = 2;
    private static final int CHECKED_IN = 3;

    private LocalDate oldStartDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private BookingState state;
    private String note;
    private int roomCode;
    private String price;

    private Bedroom bedroom;
    private List<Manager> managers;

    private EntityDataStore<Persistable> dataStore;

    private ManageSmsBooking(LocalDate startDate, LocalDate endDate, BookingState state, String note, int roomCode, BigDecimal price) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.state = state;
        this.note = note;
        this.price = FormatUtils.formatMoney(price);
        this.roomCode = roomCode;

        dataStore = DataStoreHolder.INSTANCE.getDataStore();
    }

    public ManageSmsBooking(Booking booking) {
        this(booking.getCheckInDate(), booking.getCheckOutDate(), booking.getState(),
                booking.getNote(), booking.getBedroom().getCode(), booking.getPrice());
    }

    public ManageSmsBooking(Booking oldBooking, Booking newBooking) {
        this(newBooking.getCheckInDate(), newBooking.getCheckOutDate(), newBooking.getState(),
                newBooking.getNote(), oldBooking.getBedroom().getCode(), newBooking.getPrice());
        oldStartDate = oldBooking.getCheckInDate();
    }

    public void sendCreateMessage() {
        int state = 0;
        switch (this.state) {
            case PENDING:
                state = PENDING;
                break;
            case CONFIRMED:
                state = CONFIRMED;
                break;
            case CHECKED_IN:
                state = CHECKED_IN;
                break;
        }
        LocalDateConverter localDateConverter = new LocalDateConverter();
        String message = "<$#" + localDateConverter.convertToPersisted(startDate) + "#" + localDateConverter.convertToPersisted(endDate) + "#" + price + "#" + state + "#" + roomCode + "#" + note;
        sendMessage(message);
    }

    public void sendUpdateMessage() {
        int state = 0;
        switch (this.state) {
            case PENDING:
                state = PENDING;
                break;
            case CONFIRMED:
                state = CONFIRMED;
                break;
            case CHECKED_IN:
                state = CHECKED_IN;
                break;
        }
        LocalDateConverter localDateConverter = new LocalDateConverter();
        String message = ">$#" + localDateConverter.convertToPersisted(oldStartDate) + "#" + localDateConverter.convertToPersisted(startDate) + "#" + localDateConverter.convertToPersisted(endDate) + "#" + price + "#" + state + "#" + roomCode + "#" + note;
        sendMessage(message);
    }

    public void sendDeleteMessage() {
        LocalDateConverter localDateConverter = new LocalDateConverter();
        String message = "$$#" + localDateConverter.convertToPersisted(startDate) + "#" + roomCode;
        sendMessage(message);
    }

    private void sendMessage(String message) {
        Mensajero.enviar_mensaje(getManagers(), message);
    }


    private Bedroom getBedroom() {
        if(bedroom == null) {
            bedroom = dataStore.select(Bedroom.class).where(Bedroom.CODE.eq(roomCode)).get().first();
        }
        return bedroom;
    }

    private List<Manager> getManagers() {
        if(managers == null) {
            managers = dataStore.select(Manager.class)
                    .where(Manager.HOSTEL.eq(getBedroom().getHostel()))
                    .get().toList();
        }
        return managers;
    }
}
