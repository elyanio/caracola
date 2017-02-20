package com.polymitasoft.caracola.datamodel;

import android.support.annotation.NonNull;

import org.threeten.bp.LocalDate;

import io.requery.Column;
import io.requery.Convert;
import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.Persistable;

/**
 * @author rainermf
 * @since 11/2/2017
 */
@Entity
public interface Client extends Persistable {
    @Key
    @Generated
    int getId();

    @NonNull
    @Column(nullable = false)
    String getPassport();
    void setPassport(String passport);

    @NonNull
    @Column(nullable = false)
    String getFirstName();
    void setFirstName(String firstName);

    @NonNull
    @Column(nullable = false)
    String getLastName();
    void setLastName(String lastName);

    @NonNull
    @Column(nullable = false)
    @Convert(LocalDateConverter.class)
    LocalDate getBirthday();
    void setBirthday(LocalDate birthday);

    @NonNull
    @Column(nullable = false)
    @Convert(CountryConverter.class)
    Country getCountry();
    void setCountry(Country country);

    @NonNull
    @Column(nullable = false)
    @Convert(GenderConverter.class)
    Gender getGender();
    void setGender(Gender gender);
}
