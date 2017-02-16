package com.polymitasoft.caracola.datamodel;

import io.requery.Converter;
import io.requery.Nullable;

/**
 * Created by rainermf on 11/2/2017.
 */
public class CountryConverter implements Converter<Country, String> {

    @Override
    public Class<Country> getMappedType() {
        return Country.class;
    }

    @Override
    public Class<String> getPersistedType() {
        return String.class;
    }

    @Nullable
    @Override
    public Integer getPersistedSize() {
        return null;
    }

    @Override
    public String convertToPersisted(Country value) {
        return value.getCode();
    }

    @Override
    public Country convertToMapped(Class<? extends Country> type, String value) {
        return new Country(value);
    }
}
