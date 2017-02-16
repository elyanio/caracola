package com.polymitasoft.caracola.util;

import android.util.Log;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * Created by rainermf on 13/2/2017.
 */

public class FormatUtils {

    private static DecimalFormat decimalFormat;
    private static DateTimeFormatter dateFormatter;

    static {
        decimalFormat = (DecimalFormat) DecimalFormat.getInstance();
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);
        decimalFormat.setParseBigDecimal(true);

        dateFormatter = DateTimeFormatter.ofPattern("d-MM-y");
    }

    private FormatUtils() { };

    public static String formatMoney(BigDecimal decimal) {
        return decimalFormat.format(decimal);
    }

    public static BigDecimal parseMoney(String money) {
        try {
            return (BigDecimal) decimalFormat.parse(money);
        } catch (ParseException e) {
            Log.e(FormatUtils.class.toString(), "Could not parse " + money + " as a BigDecimal. Using 0 as a fallback.");
        }
        return BigDecimal.ZERO;
    }

    public static String formatDate(LocalDate date) {
        return dateFormatter.format(date);
    }

}
