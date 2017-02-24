package com.polymitasoft.caracola.dataaccess;

import android.support.annotation.NonNull;

import com.polymitasoft.caracola.datamodel.Consumption;

import java.math.BigDecimal;

/**
 * @author rainermf
 * @since 22/2/2017
 */

public class Consumptions {

    private Consumptions() {
        throw new AssertionError();
    }

    public static BigDecimal cost(@NonNull Iterable<Consumption> consumptions) {
        BigDecimal sum = BigDecimal.ZERO;
        for (Consumption c: consumptions) {
            sum = sum.add(c.getPrice().multiply(BigDecimal.valueOf(c.getAmount())));
        }
        return sum;
    }

    public static BigDecimal cost(Consumption consumption) {
        return consumption.getPrice().multiply(BigDecimal.valueOf(consumption.getAmount()));
    }
}
