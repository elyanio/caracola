package com.polymitasoft.caracola.datamodel;

import android.support.annotation.NonNull;

import java.math.BigDecimal;

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
public interface ISupplierService {
    @Key
    @Generated
    int getId();

    @NonNull
    @Column(nullable = false)
    @ManyToOne
    ISupplier getSupplier();

    @NonNull
    @Column(nullable = false)
    @ManyToOne
    IExternalService getService();

    @NonNull
    @Column(nullable = false)
    BigDecimal getPrice();

    @NonNull
    @Column(nullable = false)
    BigDecimal getComission();
}
