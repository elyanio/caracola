package com.polymitasoft.caracola.datamodel;

import android.support.annotation.NonNull;

import java.util.List;

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
public interface Supplier extends Persistable {
    @Key
    @Generated
    int getId();

    @NonNull
    @Column(nullable = false)
    String getName();
    void setName(String name);

    @NonNull
    @Column(nullable = false, value = "")
    String getAddress();
    void setAddress(String address);

    @NonNull
    @Column(nullable = false, value = "")
    @Convert(PhoneListConverter.class)
    List<String> getPhoneNumbers();
    void setPhoneNumbers(List<String> numbers);

    @NonNull
    @Column(nullable = false, value = "")
    String getEmailAddress();
    void setEmailAddress(String emailAddress);

    @Column(nullable = false, value = "")
    @NonNull
    String getDescription();
    void setDescription(String description);
}
