package com.polymitasoft.caracola.datamodel;

import java.util.Locale;

/**
 * @author rainermf
 * @since 11/2/2017
 */
public class Country {

    private final Locale locale;

    public Country(String code) {
        locale = new Locale("es", code);
    }

    public static Country fromCode(String code) {
        // Here we can cache common countries
        return new Country(code);
    }

    public String getCode() {
        return locale.getCountry();
    }

    public String getName() {
        return locale.getDisplayCountry();
    }

    @Override
    public String toString() {
        return getName();
    }
}