package com.polymitasoft.caracola.datamodel;

import android.support.annotation.NonNull;

import io.requery.Column;
import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;

/**
 * Created by rainermf on 11/2/2017.
 */
@Entity
public interface IExternalService {
    @Key
    @Generated
    int getId();

    @NonNull
    @Column(nullable = false, collate = "LOCALIZED")
    String getName();
}
