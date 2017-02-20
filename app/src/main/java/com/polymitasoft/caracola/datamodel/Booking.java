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

/**
 * @author rainermf
 * @since 11/2/2017
 */
@Entity
public interface Booking extends Persistable {
    @Key
    @Generated
    int getId();

    @NonNull
    @Column(nullable = false)
    @Convert(LocalDateConverter.class)
    LocalDate getCheckInDate();
    void setCheckInDate(LocalDate checkInDate);

    @NonNull
    @Column(nullable = false)
    @Convert(LocalDateConverter.class)
    LocalDate getCheckOutDate();
    void setCheckOutDate(LocalDate checkOutDate);

    @NonNull
    @Column(nullable = false)
    @ManyToOne
    Bedroom getBedroom();
    void setBedroom(Bedroom bedroom);

    @NonNull
    @Column(nullable = false)
    BigDecimal getPrice();
    void setPrice(BigDecimal price);

    @NonNull
    @Column(nullable = false)
    @Convert(BookingStateConverter.class)
    BookingState getState();
    void setState(BookingState state);

    @NonNull
    @Column(nullable = false, value = "")
    String getNote();
    void setNote(String note);

    @Nullable
    String getBookingNumber();
    void setBookingNumber(String bookingNumber);

    @Nullable
    String getBookNumber();
    void setBookNumber(String bookNumber);
}
