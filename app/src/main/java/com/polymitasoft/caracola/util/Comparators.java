package com.polymitasoft.caracola.util;

import java.util.Comparator;

/**
 * @author rainermf
 * @since 6/3/2017
 */

public class Comparators {

    public static <T> Comparator<T> ignoreCaseToStringComparator() {
        return new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                return o1.toString().compareToIgnoreCase(o2.toString());
            }
        };
    }
}
