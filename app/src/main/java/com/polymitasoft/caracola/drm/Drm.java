package com.polymitasoft.caracola.drm;

import android.annotation.SuppressLint;
import android.provider.Settings;
import android.util.Base64;

import com.polymitasoft.caracola.CaracolaApplication;

import java.math.BigInteger;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author rainermf
 * @since 15/3/2017
 */

public enum Drm {
    ; // no instance

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

    public static byte[] toByteArray(long value) {
        // Note that this code needs to stay compatible with GWT, which has known
        // bugs when narrowing byte casts of long values occur.
        byte[] result = new byte[8];
        for (int i = 7; i >= 0; i--) {
            result[i] = (byte) (value & 0xffL);
            value >>= 8;
        }
        return result;
    }

    public static byte[] encrypt(String plainText, String encryptionKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("US-ASCII"), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(plainText.getBytes("US-ASCII"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decrypt(byte[] cipherText, String encryptionKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("US-ASCII"), "AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(cipherText), "US-ASCII");
        } catch (Exception e) {
            return "";
        }
    }

    public static String encryptTo64String(String plainText, String encryptionKey) {
        return Base64.encodeToString(encrypt(plainText, encryptionKey), Base64.DEFAULT).trim();
    }

    public static String decryptFrom64String(String cipherText, String encryptionKey) {
        return decrypt(Base64.decode(cipherText, Base64.DEFAULT), encryptionKey);
    }

    public static String encryptTo64String(String plainText) {
        return encryptTo64String(plainText, "Toawtef_*blyWef2");
    }

    public static String decryptFrom64String(String cipherText) {
        return decryptFrom64String(cipherText, "Toawtef_*blyWef2");
    }

    public static String encryptTo64StringMessage(String plainText) {
        return encryptTo64String(plainText, "Ghbcveq!*uxcVui7");
    }
    public static String decryptFrom64StringMessage(String cipherText) {
        return decryptFrom64String(cipherText, "Ghbcveq!*uxcVui7");
    }

    public static String reduceToHalf(String text) {
        StringBuilder builder = new StringBuilder(text.length() >> 1);
        int length = text.length();
        for (int i = 0; i < length; i += 2) {
            builder.append(text.charAt(i));
        }
        return builder.toString();
    }
}
