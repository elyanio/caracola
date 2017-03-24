package com.polymitasoft.caracola.settings;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.common.base.Splitter;
import com.polymitasoft.caracola.CaracolaApplication;
import com.polymitasoft.caracola.drm.Drm;
import com.polymitasoft.caracola.util.FormatUtils;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeParseException;

/**
 * Created by rainermf on 13-Mar-17.
 */

public class Preferences {

    public static boolean isHighSeason() {
        return getPreferences().getBoolean("high_season", false);
    }

    public static boolean isSmsSyncEnabled() {
        return getPreferences().getBoolean("sms_sync", false);
    }

    public static boolean shouldAlertActivationCountdown() {
        String dateString = getPreferences().getString("show_activation_countdown", "");
        try {
            return !FormatUtils.parseDate(dateString).isEqual(LocalDate.now());
        } catch (DateTimeParseException e) {
            return true;
        }
    }

    public static void updateAlertActivationCountdown() {
        getPreferences().edit().putString("show_activation_countdown", FormatUtils.formatDate(LocalDate.now())).apply();
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
        if(results.length == 2 && results[1].equals(Drm.getDeviceId())) {
            return results[0];
        }
        return "";
    }

    private static SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(CaracolaApplication.instance());
    }
}
