package com.polymitasoft.caracola.datamodel;

import android.support.annotation.NonNull;

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
public interface ISupplierService extends Persistable {
    @Key
    @Generated
    int getId();

    @NonNull
    @Column(nullable = false)
    @ManyToOne
    Supplier getSupplier();

    @NonNull
    @Column(nullable = false)
    @ManyToOne
    ExternalService getService();

    @NonNull
    @Column(nullable = false)
    String getDescription();
}
