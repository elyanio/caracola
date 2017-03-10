package com.polymitasoft.caracola.datamodel;

import android.support.annotation.NonNull;

import io.requery.Column;
import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.ManyToOne;

import static io.requery.PropertyNameStyle.FLUENT_BEAN;

/**
 * Created by asio on 2/24/2017.
 */

@Entity(propertyNameStyle = FLUENT_BEAN)
public interface IManager {

    @Key
    @Generated
    int getId();

    @NonNull
    @Column(nullable = false)
    String getPhoneNumber();

    @NonNull
    @Column(nullable = false)
    String getName();

    @NonNull
    @Column(nullable = false)
    @ManyToOne
    Hostel getHostel();
}
