package com.polymitasoft.caracola;

import android.support.test.runner.AndroidJUnit4;

import com.polymitasoft.caracola.drm.Drm;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * @author rainermf
 * @since 15/3/2017
 */

@RunWith(AndroidJUnit4.class)
public class CryptoTest {

    @Test
    public void cryptoTest() throws Exception {
        String plainString = Drm.getDeviceId();
        String encryptionKey = "Toawtef_*blyWef2";
        String encryptedString = Drm.encryptTo32String(plainString, encryptionKey);

        assertNotEquals(plainString, encryptedString);
        assertEquals(plainString, Drm.decryptFrom32String(encryptedString, encryptionKey));
    }

}
