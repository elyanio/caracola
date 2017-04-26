package com.polymitasoft.caracola.drm;

import com.polymitasoft.caracola.settings.Preferences;
import com.polymitasoft.caracola.util.FormatUtils;

import org.threeten.bp.LocalDate;

import static com.polymitasoft.caracola.util.FormatUtils.parseDate;
import static java.lang.Long.parseLong;
import static org.threeten.bp.temporal.ChronoUnit.DAYS;

/**
 * @author Rainer Martínez Fraga <rmf@polymitasoft.com>
 * @since 4/25/2017.
 */
public final class ActivationData {

    private final LocalDate startDate;
    private final long days;
    private final long remainingDays;
    private final LocalDate endDate;

    private static final String EVALUATION_DATE_KEY = "evaluation_date";
    private static final String ACTIVATION_DATE_KEY = "evaluation_date";
    private static final String ACTIVATION_DAYS_KEY = "evaluation_days";
    private static final String EVALUATION_DAYS_VALUE = "2";

    private ActivationData(LocalDate startDate, long days) {
        this.startDate = startDate;
        this.days = days;
        this.endDate = startDate.plusDays(days);
        LocalDate todayDate = LocalDate.now();
        long today = todayDate.toEpochDay();

        if(startDate.toEpochDay() <= today && endDate.toEpochDay() >= today) {
            remainingDays = DAYS.between(todayDate, endDate);
        } else {
            remainingDays = -1;
        }
    }

    public static ActivationData of(LocalDate startDate, long days) {
        return new ActivationData(startDate, days);
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public long getDays() {
        return days;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public long getRemainingDays() {
        return remainingDays;
    }

    public boolean isActivated() {
        return getRemainingDays() >= 0;
    }

    public static ActivationData current() {
        String evalDateString = Preferences.getEncryptedPreference(ACTIVATION_DATE_KEY);
        String evalDaysString = Preferences.getEncryptedPreference(ACTIVATION_DAYS_KEY);
        if (evalDateString.trim().isEmpty() || evalDaysString.trim().isEmpty()) {
            String defaultValue = FormatUtils.formatDate(LocalDate.now());
            if(!Preferences.existsDbPreference(EVALUATION_DATE_KEY)) {
                Preferences.setDbPreference(EVALUATION_DATE_KEY, defaultValue);
            }
            evalDateString = Preferences.getDbPreference(EVALUATION_DATE_KEY, defaultValue);
            evalDaysString = EVALUATION_DAYS_VALUE;
        }

        return ActivationData.of(parseDate(evalDateString), parseLong(evalDaysString));
    }
}
