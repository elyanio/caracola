package com.polymitasoft.caracola.datamodel;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import io.requery.Converter;
import io.requery.Nullable;

/**
 * Created by rainermf on 11/2/2017.
 */

public class LocalDateConverter implements Converter<LocalDate, String> {
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("y-MM-dd");

    @Override
    public Class<LocalDate> getMappedType() {
        return LocalDate.class;
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
    public String convertToPersisted(LocalDate value) {
        return value.format(formatter);
    }

    @Override
    public LocalDate convertToMapped(Class<? extends LocalDate> type, String value) {
        return LocalDate.parse(value, formatter);
    }
//    @Override
//    public Class<LocalDate> getMappedType() {
//        return LocalDate.class;
//    }
//
//    @Override
//    public Class<Date> getPersistedType() {
//        return Date.class;
//    }
//
//    @Nullable
//    @Override
//    public Integer getPersistedSize() {
//        return null;
//    }
//
//    @SuppressWarnings("deprecation")
//    @Override
//    public Date convertToPersisted(LocalDate value) {
//        return DateTimeUtils.toDate(Instant.from(value));
//        return new Date(value.getYear() - 1900, value.getMonthValue() - 1, value.getDayOfMonth());
//    }
//
//    @SuppressWarnings("deprecation")
//    @Override
//    public LocalDate convertToMapped(Class<? extends LocalDate> type, Date date) {
//        return LocalDate.from(DateTimeUtils.toInstant(date));
//        return LocalDate.of(date.getYear() + 1900, date.getMonth() + 1, date.getDate());
//    }
}
