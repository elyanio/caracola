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
import io.requery.Persistable;

/**
 * @author rainermf
 * @since 11/2/2017
 */
@Entity
public interface Consumption extends Persistable {
    @Key
    @Generated
    int getId();

    @NonNull
    @Column(nullable = false)
    @ManyToOne
    InternalService getInternalService();
    void setInternalService(InternalService service);

    @NonNull
    @Column(nullable = false)
    @ManyToOne
    Booking getBooking();
    void setBooking(Booking booking);

    @NonNull
    @Column(nullable = false)
    BigDecimal getDefaultPrice();
    void setDefaultPrice(BigDecimal price);

    @NonNull
    @Column(nullable = false)
    int getAmount();
    void setAmount(int amount);

    @NonNull
    @Column(nullable = false, name = "consumptionDate")
    @Convert(LocalDateConverter.class)
    LocalDate getDate();
    void setDate(LocalDate date);
}
