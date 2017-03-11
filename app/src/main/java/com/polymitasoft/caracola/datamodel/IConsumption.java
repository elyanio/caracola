package com.polymitasoft.caracola.datamodel;

import android.support.annotation.NonNull;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;

import io.requery.Column;
import io.requery.Convert;
import io.requery.Entity;
import io.requery.ForeignKey;
import io.requery.Generated;
import io.requery.Key;
import io.requery.ManyToOne;
import io.requery.Persistable;
import io.requery.ReferentialAction;

import static io.requery.PropertyNameStyle.FLUENT_BEAN;
import static io.requery.ReferentialAction.RESTRICT;

/**
 * @author rainermf
 * @since 11/2/2017
 */
@Entity(propertyNameStyle = FLUENT_BEAN)
public interface IConsumption extends Persistable {
    @Key
    @Generated
    int getId();

    @NonNull
    @Column(nullable = false)
    @ManyToOne
    @ForeignKey(delete = RESTRICT)
    InternalService getInternalService();

    @NonNull
    @Column(nullable = false)
    @ManyToOne
    Booking getBooking();

    @NonNull
    @Column(nullable = false)
    BigDecimal getPrice();

    @NonNull
    @Column(nullable = false)
    int getAmount();

    @NonNull
    @Column(nullable = false, name = "consumptionDate")
    @Convert(LocalDateConverter.class)
    LocalDate getDate();
}
