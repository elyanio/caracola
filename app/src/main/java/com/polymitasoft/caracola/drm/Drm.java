package com.polymitasoft.caracola.drm;

import android.provider.Settings;

/**
 * @author rainermf
 * @since 15/3/2017
 */

public class Drm {

    public static String getDeviceId() {
        return Settings.Secure.ANDROID_ID;
    }
}
