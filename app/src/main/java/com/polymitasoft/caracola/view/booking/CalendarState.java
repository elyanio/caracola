package com.polymitasoft.caracola.view.booking;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;

import com.polymitasoft.caracola.datamodel.BookingState;

/**
 * Created by rainermf on 13/2/2017.
 */

public enum CalendarState {
    // TODO quitar el blanco mas uno
    NO_DAY(Color.parseColor("#FFFFFE")),
    EMPTY(Color.WHITE),
    CONFIRMED(Color.parseColor("#FFFF1744")), // red
    CHECKED_IN(Color.parseColor("#FFCFD8DC")),  // grey
    OCUPPIED(Color.parseColor("#FFFF1744")), // red
    PARTIALLY_OCCUPIED(Color.parseColor("#90caf9")), // blue
    SELECTED(Color.parseColor("#009688")), // green
    UNSELECTED(Color.TRANSPARENT),
    PENDING (Color.parseColor("#FFFFD740")); // yellow

    @ColorInt private int color;

    CalendarState(@ColorInt int color) {
        this.color = color;
    }

    @ColorInt public int color() {
        return color;
    }

    @Nullable
    public BookingState toBookingState() {
        switch (this) {
            case CONFIRMED: return BookingState.CONFIRMED;
            case PENDING: return BookingState.PENDING;
            case CHECKED_IN: return BookingState.CHECKED_IN;
            default: return null;
        }
    }

    public static CalendarState toCalendarState(BookingState state) {
        switch (state) {
            case CONFIRMED: return CalendarState.CONFIRMED;
            case PENDING: return CalendarState.PENDING;
            case CHECKED_IN: return CalendarState.CHECKED_IN;
            default: return null;
        }
    }
}
