package com.polymitasoft.caracola.dataaccess;

import android.support.annotation.NonNull;

import com.polymitasoft.caracola.datamodel.Booking;

import org.threeten.bp.Period;

import java.math.BigDecimal;

/**
 * @author rainermf
 * @since 22/2/2017
 */

public class Bookings {

    private Bookings() {
        throw new AssertionError();
    }

    public static int nights(@NonNull Booking booking) {
        return Period.between(booking.getCheckInDate(), booking.getCheckOutDate()).getDays() + 1;
    }

    public static BigDecimal lodgingCost(@NonNull Booking booking) {
        return booking.getPrice().multiply(BigDecimal.valueOf(nights(booking)));
    }
}
