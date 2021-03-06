package com.polymitasoft.caracola.datamodel;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.math.BigDecimal;

import io.requery.Column;
import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.ManyToOne;
import io.requery.Persistable;

import static io.requery.PropertyNameStyle.FLUENT_BEAN;

/**
 * @author rainermf
 * @since 11/2/2017
 */
@Entity(propertyNameStyle = FLUENT_BEAN)
public interface IBedroom extends Persistable {
    @Key
    @Generated
    int getId();

    @NonNull
    @Column(nullable = false)
    String getName();

    @NonNull
    @Column(nullable = false)
    int getCapacity();

    @NonNull
    @Column(nullable = false)
    BigDecimal getPriceInLowSeason();

    @NonNull
    @Column(nullable = false)
    BigDecimal getPriceInHighSeason();

    @Nullable
    @ManyToOne
    @Column(nullable = true)
    Hostel getHostel();

    @NonNull
    @Column(nullable = false)
    int getCode();
}
