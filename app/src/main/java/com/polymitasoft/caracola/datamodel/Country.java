package com.polymitasoft.caracola.datamodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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

    public static List<Country> getCountries() {
        String[] countryCodes = Locale.getISOCountries();
        List<Country> countries = new ArrayList<>();
        for(String code : countryCodes) {
            countries.add(fromCode(code));
        }
        return countries;
    }

    public static String[] getCountryCodes() {
        return Locale.getISOCountries();
    }

    public String getCode() {
        return locale.getCountry();
    }

    public String getName() {
        return locale.getDisplayCountry();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Country)) return false;
        Country country = (Country) o;

        return getCode().equals(country.getCode());
    }

    @Override
    public int hashCode() {
        return getCode().hashCode();
    }

    @Override
    public String toString() {
        return getName();
    }
}