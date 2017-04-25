package com.polymitasoft.caracola.datamodel;

import android.support.annotation.NonNull;

import io.requery.Column;
import io.requery.Entity;
import io.requery.Key;

import static io.requery.PropertyNameStyle.FLUENT_BEAN;

/**
 * @author Rainer Martínez Fraga <rmf@polymitasoft.com>
 * @since 4/23/2017.
 */
@Entity(propertyNameStyle = FLUENT_BEAN)
public interface IDbPreference {
    @Key
    String getKey();

    @NonNull
    @Column(nullable = false)
    String getValue();
}
