package com.polymitasoft.caracola.datamodel;

import android.support.annotation.NonNull;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;
import java.util.List;

import io.requery.Column;
import io.requery.Convert;
import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.ManyToOne;
import io.requery.Nullable;

/**
 * @author rainermf
 * @since 11/2/2017
 */
@Entity
public interface IBooking {
    @Key
    @Generated
    int getId();

    @NonNull
    @Column(nullable = false)
    @Convert(LocalDateConverter.class)
    LocalDate getCheckInDate();

    @NonNull
    @Column(nullable = false)
    @Convert(LocalDateConverter.class)
    LocalDate getCheckOutDate();

    @NonNull
    @Column(nullable = false)
    @ManyToOne
    IBedroom getBedroom();

    @NonNull
    @Column(nullable = false)
    BigDecimal getPrice();

    @NonNull
    @Column(nullable = false)
    @Convert(BookingStateConverter.class)
    BookingState getState();

    @NonNull
    @Column(nullable = false, value = "")
    String getNote();

    @Nullable
    String getBookingNumber();

    @Nullable
    String getBookNumber();
}
