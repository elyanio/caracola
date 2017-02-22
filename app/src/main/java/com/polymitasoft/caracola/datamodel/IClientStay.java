package com.polymitasoft.caracola.datamodel;

import android.support.annotation.NonNull;

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
public interface IClientStay extends Persistable {
    @Key
    @Generated
    int getId();

    @NonNull
    @Column(nullable = false)
    @ManyToOne
    Client getClient();

    @NonNull
    @Column(nullable = false)
    @ManyToOne
    Booking getBooking();

    @NonNull
    @Column(nullable = false)
    boolean isHolder();
}
