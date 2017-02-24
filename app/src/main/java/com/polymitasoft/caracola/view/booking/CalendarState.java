package com.polymitasoft.caracola.view.booking;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.polymitasoft.caracola.datamodel.BookingState;

/**
 * @author rainermf
 * @since 13/2/2017
 */
enum CalendarState {
    // TODO quitar el blanco mas uno
    NO_DAY(Color.parseColor("#FFFFFE")),
    EMPTY(Color.WHITE),
    CONFIRMED(Color.parseColor("#90caf9")), // blue"#FFCFD8DC"
    CHECKED_IN(Color.parseColor("#FFFF80AB")),  // pink
    OCUPPIED(Color.parseColor("#FFBF360C")), // brown
    PARTIALLY_OCCUPIED(Color.parseColor("#FFFF9E80")), // light brown
    SELECTED(Color.parseColor("#009688")), // green
    UNSELECTED(Color.TRANSPARENT),
    PENDING (Color.parseColor("#FFFFD740")); // yellow

    @ColorInt private final int color;

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

    @NonNull
    public static CalendarState toCalendarState(@NonNull BookingState state) {
        switch (state) {
            case CONFIRMED: return CalendarState.CONFIRMED;
            case PENDING: return CalendarState.PENDING;
            case CHECKED_IN:
            default:
                return CalendarState.CHECKED_IN;
        }
    }
}
