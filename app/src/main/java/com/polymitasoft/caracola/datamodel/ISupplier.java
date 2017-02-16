package com.polymitasoft.caracola.datamodel;

import android.support.annotation.NonNull;

import java.util.List;

import io.requery.Column;
import io.requery.Convert;
import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;

/**
 * Created by rainermf on 11/2/2017.
 */

@Entity
public interface ISupplier {
    @Key
    @Generated
    int getId();

    @NonNull
    @Column(nullable = false, collate = "LOCALIZED")
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
