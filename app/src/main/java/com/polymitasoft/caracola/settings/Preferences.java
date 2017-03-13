package com.polymitasoft.caracola.settings;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.polymitasoft.caracola.CaracolaApplication;

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

    private static SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(CaracolaApplication.instance());
    }
}
