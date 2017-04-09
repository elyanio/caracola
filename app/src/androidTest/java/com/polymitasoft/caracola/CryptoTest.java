package com.polymitasoft.caracola;

import android.support.test.runner.AndroidJUnit4;

import com.polymitasoft.caracola.drm.Drm;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * @author rainermf
 * @since 15/3/2017
 */

@RunWith(AndroidJUnit4.class)
public class CryptoTest {

    @Test
    public void cryptoTest() throws Exception {
        String plainString = "ZdMEXiILF2Y=";
        String encryptedString = Drm.reduceToHalf(Drm.encryptTo64String(plainString));
        String checkedEncryptedString = "MLFRUVDbGGM=";

        assertEquals(encryptedString.length(), 12);
        assertEquals(checkedEncryptedString.length(), 12);
        assertEquals(encryptedString, checkedEncryptedString);
    }

    @Test
    public void decryptionTest() throws Exception {
        String plainString = "Hell this is shit";
        String encryptedString = Drm.encryptTo64String(plainString);

        assertEquals(Drm.decryptFrom64String(encryptedString), plainString);
    }

}
