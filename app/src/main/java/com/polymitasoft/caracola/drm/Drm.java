package com.polymitasoft.caracola.drm;

import android.provider.Settings;
import android.util.Base64;

import com.google.common.io.BaseEncoding;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author rainermf
 * @since 15/3/2017
 */

public enum Drm {
    ; // no instance

    public static String getDeviceId() {
        return Settings.Secure.ANDROID_ID;
    }

    public static byte[] encrypt(String plainText, String encryptionKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(plainText.getBytes("UTF-8"));
    }

    public static String decrypt(byte[] cipherText, String encryptionKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return new String(cipher.doFinal(cipherText), "UTF-8");
    }

    public static String encryptTo32String(String plainText, String encryptionKey) throws Exception {
        return BaseEncoding.base32Hex().encode(encrypt(plainText, encryptionKey));
    }

    public static String decryptFrom32String(String cipherText, String encryptionKey) throws Exception {
        return decrypt(BaseEncoding.base32Hex().decode(cipherText), encryptionKey);
    }

    public static String encryptTo64String(String plainText, String encryptionKey) throws Exception {
        return BaseEncoding.base64().encode(encrypt(plainText, encryptionKey));
    }

    public static String decryptFrom64String(String cipherText, String encryptionKey) throws Exception {
        return decrypt(BaseEncoding.base64().decode(cipherText), encryptionKey);
    }
}
