package com.polymitasoft.caracola.datamodel;

import android.support.annotation.NonNull;

import java.math.BigDecimal;

import io.requery.Column;
import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Persistable;

/**
 * @author rainermf
 * @since 11/2/2017
 */
@Entity
public interface Bedroom extends Persistable {
    @Key
    @Generated
    int getId();

    @NonNull
    @Column(nullable = false)
    String getName();
    void setName(String name);

    @NonNull
    @Column(nullable = false)
    int getCapacity();
    void setCapacity(int capacity);

    @NonNull
    @Column(nullable = false)
    BigDecimal getPriceInLowSeason();
    void setPriceInLowSeason(BigDecimal price);

    @NonNull
    @Column(nullable = false)
    BigDecimal getPriceInHighSeason();
    void setPriceInHighSeason(BigDecimal price);
}
