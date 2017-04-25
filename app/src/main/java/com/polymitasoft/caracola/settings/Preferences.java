package com.polymitasoft.caracola.settings;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.polymitasoft.caracola.CaracolaApplication;
import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.datamodel.DbPreference;
import com.polymitasoft.caracola.drm.Drm;
import com.polymitasoft.caracola.util.FormatUtils;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeParseException;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

/**
 * @author rainermf
 * @since 13-Mar-17
 */
public class Preferences {

    private static final String PREF_BOOKING_NUMBER = "bookingNumber";
    private static final String PREF_SHOW_ACTIVATION_COUNTDOWN = "show_activation_countdown";
    private static final EntityDataStore<Persistable> dataStore = DataStoreHolder.INSTANCE.getDataStore();

    public static boolean isHighSeason() {
        return getPreferences().getBoolean("high_season", false);
    }

    public static boolean isSmsSyncEnabled() {
        return getPreferences().getBoolean("sms_sync", false);
    }

    public static int getBookingNumber() {
        return getPreferences().getInt(PREF_BOOKING_NUMBER, 0);
    }

    public static void setBookingNumber(int bookingNumber) {
        getPreferences().edit().putInt(PREF_BOOKING_NUMBER, bookingNumber).apply();
    }

    public static boolean shouldAlertActivationCountdown() {
        String dateString = getPreferences().getString(PREF_SHOW_ACTIVATION_COUNTDOWN, "");
        try {
            return !FormatUtils.parseDate(dateString).isEqual(LocalDate.now());
        } catch (DateTimeParseException e) {
            return true;
        }
    }

    public static void updateAlertActivationCountdown() {
        getPreferences().edit().putString(PREF_SHOW_ACTIVATION_COUNTDOWN, FormatUtils.formatDate(LocalDate.now())).apply();
    }

    public static void setEncryptedPreference(String key, String value) {
        Editor editor = getPreferences().edit();
        editor.putString(Drm.encryptTo64String(key), Drm.encryptTo64String(value + "|" + Drm.getDeviceId()));
        editor.apply();
    }

    public static String getEncryptedPreference(String key) {
        String value = getPreferences().getString(Drm.encryptTo64String(key), "");
        String plainValue = Drm.decryptFrom64String(value);
        String[] results = plainValue.split("\\|");
        if (results.length == 2 && results[1].equals(Drm.getDeviceId())) {
            return results[0];
        }
        return "";
    }

    public static boolean existsDbPreference(String key) {
        return dataStore.findByKey(DbPreference.class, key) == null;
    }

    public static String getDbPreference(String key, String defaultValue) {
        DbPreference pref = dataStore.findByKey(DbPreference.class, key);
        if(pref == null) {
            return defaultValue;
        }
        return pref.getValue();
    }

    public static void setDbPreference(String key, String value) {
        EntityDataStore<Persistable> dataStore = DataStoreHolder.INSTANCE.getDataStore();
        DbPreference pref = new DbPreference().setKey(key).setValue(value);
        dataStore.upsert(pref);
    }

    private static SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(CaracolaApplication.instance());
    }

    public static boolean isEnableReminder() {
        return getPreferences().getBoolean("enable_remider", true);
    }

    public static int getDayBeforeReminder() {
        return Integer.parseInt(getPreferences().getString("before_day", "-1"));
    }

    public static boolean isEnableReminderBirthday() {
        return getPreferences().getBoolean("enable_remider_birthday", true);
    }

    public static int getDayBeforeReminderBirthday() {
        return Integer.parseInt(getPreferences().getString("before_day_birthday", "-1"));
    }
    public static boolean isEnableConfirmSms() {
        return getPreferences().getBoolean("sms_sync_confirm", false);
    }



//    public static String getHourReminder() {
//        return getPreferences().getString("hour", "10");
//    }
//    public static String getMinuteReminder() {
//        return getPreferences().getString("minute", "00");
//    }

//    public static LocalTime getTimeReminder() {
//        String time_reminder = getPreferences().getString("time_reminder", "10:00");
//        Log.e("hora",time_reminder);
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
//        LocalTime time = LocalTime.parse(time_reminder, formatter);
//        return time;
//    }
}
