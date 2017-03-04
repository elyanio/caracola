package com.polymitasoft.caracola.dataaccess;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.polymitasoft.caracola.CaracolaApplication;
import com.polymitasoft.caracola.datamodel.Bedroom;
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

    public static BigDecimal perNightPrice(Bedroom bedroom) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(CaracolaApplication.instance());
        boolean highSeason = preferences.getBoolean("high_season", false);
        return (highSeason)? bedroom.getPriceInHighSeason() : bedroom.getPriceInLowSeason();
    }
}
