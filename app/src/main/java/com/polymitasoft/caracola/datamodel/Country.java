package com.polymitasoft.caracola.datamodel;

import java.util.Locale;

/**
 * Created by rainermf on 11/2/2017.
 */

public class Country {

    private Locale locale;

    public Country(String code) {
        locale = new Locale("es", code);
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