package com.polymitasoft.caracola.datamodel;

import android.support.annotation.NonNull;

import java.math.BigDecimal;

import io.requery.Column;
import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;

/**
 * @author rainermf
 * @since 11/2/2017
 */
@Entity
public interface IBedroom {
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
}
