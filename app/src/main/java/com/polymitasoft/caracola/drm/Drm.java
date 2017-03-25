package com.polymitasoft.caracola.drm;

import android.annotation.SuppressLint;
import android.provider.Settings;

import com.google.common.io.BaseEncoding;
import com.google.common.primitives.Longs;
import com.polymitasoft.caracola.CaracolaApplication;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
        return BaseEncoding.base64().encode(Longs.toByteArray(getDeviceIdAsLong()));
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

    public static String encryptTo32String(String plainText, String encryptionKey) throws Exception {
        return BaseEncoding.base32Hex().encode(encrypt(plainText, encryptionKey));
    }

    public static String decryptFrom32String(String cipherText, String encryptionKey) throws Exception {
        return decrypt(BaseEncoding.base32Hex().decode(cipherText), encryptionKey);
    }

    public static String encryptTo64String(String plainText, String encryptionKey) {
        return BaseEncoding.base64().encode(encrypt(plainText, encryptionKey));
    }

    public static String decryptFrom64String(String cipherText, String encryptionKey) {
        return decrypt(BaseEncoding.base64().decode(cipherText), encryptionKey);
    }

    public static String encryptTo64String(String plainText) {
        return encryptTo64String(plainText, "Toawtef_*blyWef2");
    }

    public static String decryptFrom64String(String cipherText) {
        return decryptFrom64String(cipherText, "Toawtef_*blyWef2");
    }

    public static String hashString(String plainText) {
        byte[] hashedBytes = hashTemplate(plainText.getBytes(), "SHA1");
        return new String(hashedBytes);
    }

    private static byte[] hashTemplate(byte[] data, String algorithm) {
        if (data == null || data.length <= 0) return null;
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(data);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
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
