package com.polymitasoft.caracola.datamodel;

import android.support.annotation.NonNull;

import java.math.BigDecimal;

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
public interface SupplierService extends Persistable {
    @Key
    @Generated
    int getId();

    @NonNull
    @Column(nullable = false)
    @ManyToOne
    Supplier getSupplier();
    void setSupplier(Supplier supplier);

    @NonNull
    @Column(nullable = false)
    @ManyToOne
    ExternalService getService();
    void setService(ExternalService service);

    @NonNull
    @Column(nullable = false)
    BigDecimal getPrice();
    void setPrice(BigDecimal price);

    @NonNull
    @Column(nullable = false)
    BigDecimal getComission();
    void setComission(BigDecimal comission);
}
