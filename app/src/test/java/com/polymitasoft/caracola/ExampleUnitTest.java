package com.polymitasoft.caracola;

import com.google.common.io.BaseEncoding;
import com.google.common.primitives.Longs;
import com.polymitasoft.caracola.drm.Drm;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        String plainString = BaseEncoding.base64().encode(Longs.toByteArray(Long.MAX_VALUE));
//        plainString = plainString.substring(0, plainString.length() / 2 - 1);
        plainString += "-" + BaseEncoding.base64().encode(Longs.toByteArray(System.currentTimeMillis()));
        String encryptionKey = "Toawtef_*blyWef2";
        String encryptedString = Drm.encryptTo32String(plainString, encryptionKey);

        System.out.println(plainString);
        System.out.println(encryptedString);
        System.out.println(Drm.encryptTo64String(plainString, encryptionKey));

        assertEquals(encryptedString.length(), 56);
        assertNotEquals(plainString, encryptedString);
        assertEquals(plainString, Drm.decryptFrom32String(encryptedString, encryptionKey));
    }

    @Test
    public void hash_isCorrect() throws Exception {
        // ZdMEXiILF2Y=
        String plainString = BaseEncoding.base64().encode(Longs.toByteArray(Long.MAX_VALUE));
        String encryptionKey = "Toawtef_*blyWef2";
        String encryptedString = Drm.encryptTo64String(plainString, encryptionKey);
        String reducedEncryptedString = Drm.reduceToHalf(encryptedString);

        System.out.println(plainString);
        System.out.println(encryptedString);
        System.out.println(reducedEncryptedString);

        assertEquals(plainString.length(), 12);
        assertEquals(encryptedString.length(), 24);
        assertEquals(reducedEncryptedString.length(), 12);
        assertNotEquals(plainString, encryptedString);
        assertEquals(plainString, Drm.decryptFrom64String(encryptedString, encryptionKey));
    }
}