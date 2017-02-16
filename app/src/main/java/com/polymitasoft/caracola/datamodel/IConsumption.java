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

/**
 * Created by rainermf on 11/2/2017.
 */

@Entity
public interface IConsumption {
    @Key
    @Generated
    int getId();

    @NonNull
    @Column(nullable = false)
    @ManyToOne
    IInternalService getInternalService();

    @NonNull
    @Column(nullable = false)
    @ManyToOne
    IBooking getBooking();

    @NonNull
    @Column(nullable = false)
    BigDecimal getDefaultPrice();

    @NonNull
    @Column(nullable = false)
    int getAmount();

    @NonNull
    @Column(nullable = false)
    @Convert(LocalDateConverter.class)
    LocalDate getDate();
}
