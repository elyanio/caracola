package com.polymitasoft.caracola.datamodel;

import android.support.annotation.NonNull;

import io.requery.Column;
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
public interface ClientStay extends Persistable {
    @Key
    @Generated
    int getId();

    @NonNull
    @Column(nullable = false)
    @ManyToOne
    Client getClient();
    void setClient(Client client);

    @NonNull
    @Column(nullable = false)
    @ManyToOne
    Booking getBooking();
    void setBooking(Booking booking);

    @NonNull
    @Column(nullable = false)
    boolean isHolder();
    void setHolder(boolean isHolder);
}
