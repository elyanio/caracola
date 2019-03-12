package com.polymitasoft.caracola.encoding;

import android.annotation.SuppressLint;
import android.provider.Settings;
import android.util.Base64;

import com.polymitasoft.activation.Coder;
import com.polymitasoft.caracola.CaracolaApplication;

import java.math.BigInteger;

import static com.polymitasoft.activation.Coder.toByteArray;

/**
 * @author rainermf
 * @since 15/3/2017
 */

public enum CoderUtils {
    ; // no instance

    private static final String ENCRYPTION_KEY = "Toawtef_*blyWef2";

    @SuppressLint("HardwareIds")
    public static String getDeviceId() {
        return Settings.Secure.getString(CaracolaApplication.instance().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static long getDeviceIdAsLong() {
        return new BigInteger(getDeviceId(), 16).longValue();
    }

    public static String getRequestCode() {
        return Base64.encodeToString(toByteArray(getDeviceIdAsLong()), Base64.DEFAULT).trim();
    }

    public static String encryptTo64String(String plainText) {
        return Coder.encryptTo64String(plainText, ENCRYPTION_KEY);
    }

    public static String decryptFrom64String(String cipherText) {
        return Coder.decryptFrom64String(cipherText, ENCRYPTION_KEY);
    }

    public static String generateCode(String requestCode) {
        return Coder.reduceToHalf(Coder.encryptTo64String(requestCode, ENCRYPTION_KEY));
    }
}
