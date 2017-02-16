package com.polymitasoft.caracola.datamodel;

import io.requery.Converter;
import io.requery.converter.EnumOrdinalConverter;

/**
 * Created by rainermf on 11/2/2017.
 */
public class GenderConverter extends EnumOrdinalConverter<Gender> {
    public GenderConverter() {
        super(Gender.class);
    }
}
