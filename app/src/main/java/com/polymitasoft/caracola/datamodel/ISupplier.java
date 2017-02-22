package com.polymitasoft.caracola.datamodel;

import android.support.annotation.NonNull;

import java.util.List;

import io.requery.Column;
import io.requery.Convert;
import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Persistable;

import static io.requery.PropertyNameStyle.FLUENT_BEAN;

/**
 * @author rainermf
 * @since 11/2/2017
 */
@Entity(propertyNameStyle = FLUENT_BEAN)
public interface ISupplier extends Persistable {
    @Key
    @Generated
    int getId();

    @NonNull
    @Column(nullable = false)
    String getName();

    @NonNull
    @Column(nullable = false, value = "")
    String getAddress();

    @NonNull
    @Column(nullable = false, value = "")
    @Convert(PhoneListConverter.class)
    List<String> getPhoneNumbers();

    @NonNull
    @Column(nullable = false, value = "")
    String getEmailAddress();

    @Column(nullable = false, value = "")
    @NonNull
    String getDescription();
}
