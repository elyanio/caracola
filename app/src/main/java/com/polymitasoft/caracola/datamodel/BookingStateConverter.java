package com.polymitasoft.caracola.datamodel;

import io.requery.converter.EnumOrdinalConverter;

/**
 * Created by rainermf on 11/2/2017.
 */
public class BookingStateConverter extends EnumOrdinalConverter<BookingState> {
    public BookingStateConverter() {
        super(BookingState.class);
    }
}
