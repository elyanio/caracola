package com.polymitasoft.caracola.datamodel;

import android.support.annotation.NonNull;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;

import io.requery.Column;
import io.requery.Convert;
import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.ManyToOne;
import io.requery.Nullable;
import io.requery.Persistable;

import static io.requery.PropertyNameStyle.FLUENT_BEAN;

/**
 * @author rainermf
 * @since 11/2/2017
 */
@Entity(propertyNameStyle = FLUENT_BEAN)
public interface IBooking extends Persistable {
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
    Bedroom getBedroom();

    @NonNull
    @Column(nullable = false)
    BigDecimal getPrice();

    @NonNull
    @Column(nullable = false)
    @Convert(BookingStateConverter.class)
    BookingState getState();

    @NonNull
    @Column(nullable = false)
    String getNote();

    @Column(value = "-1")
    int getBookingNumber();

    @Column(value = "-1")
    int getBookNumber();
}
