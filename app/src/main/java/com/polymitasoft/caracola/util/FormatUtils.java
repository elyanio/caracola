package com.polymitasoft.caracola.util;

import android.util.Log;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * @author rainermf
 * @since 13/2/2017
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

    private FormatUtils() throws InstantiationException {
        throw new InstantiationException("FormatUtils is a utility class. It should not be instantiated.");
    }

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

    public static LocalDate parseDate(String date) {
        return LocalDate.from(dateFormatter.parse(date));
    }

}
