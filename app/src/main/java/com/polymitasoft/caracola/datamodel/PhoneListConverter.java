package com.polymitasoft.caracola.datamodel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.requery.Converter;
import io.requery.Nullable;

/**
 * @author rainermf
 * @since 11/2/2017
 */
public class PhoneListConverter implements Converter<List<String>, String> {

    @Override
    public Class<List<String>> getMappedType() {
        return (Class<List<String>>) Collections.<String>emptyList().getClass();
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
    public String convertToPersisted(List<String> list) {
        if(list.size() == 0) {
            return "";
        }
        String result = list.get(0);
        for(int i = 1; i < list.size(); i++) {
            result += '\u00A0' + list.get(i);
        }
        return result;
    }

    @Override
    public List<String> convertToMapped(Class<? extends List<String>> type, String value) {
        return Arrays.asList(value.split("\u00A0"));
    }
}
