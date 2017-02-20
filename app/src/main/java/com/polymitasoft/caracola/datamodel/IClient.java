package com.polymitasoft.caracola.datamodel;

import android.support.annotation.NonNull;

import org.threeten.bp.LocalDate;

import io.requery.Column;
import io.requery.Convert;
import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;

/**
 * @author rainermf
 * @since 11/2/2017
 */
@Entity
public interface IClient {
    @Key
    @Generated
    int getId();

    @NonNull
    @Column(nullable = false)
    String getPassport();

    @NonNull
    @Column(nullable = false)
    String getFirstName();

    @NonNull
    @Column(nullable = false)
    String getLastName();

    @NonNull
    @Column(nullable = false)
    @Convert(LocalDateConverter.class)
    LocalDate getBirthday();

    @NonNull
    @Column(nullable = false)
    @Convert(CountryConverter.class)
    Country getCountry();

    @NonNull
    @Column(nullable = false)
    @Convert(GenderConverter.class)
    Gender getGender();
}
