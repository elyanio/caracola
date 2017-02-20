package com.polymitasoft.caracola.datamodel;

import android.support.annotation.NonNull;

import io.requery.Column;
import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.ManyToOne;

/**
 * @author rainermf
 * @since 11/2/2017
 */
@Entity
public interface IClientStay {
    @Key
    @Generated
    int getId();

    @NonNull
    @Column(nullable = false)
    @ManyToOne
    IClient getClient();

    @NonNull
    @Column(nullable = false)
    @ManyToOne
    IBooking getBooking();

    @NonNull
    @Column(nullable = false)
    boolean isHolder();
}
