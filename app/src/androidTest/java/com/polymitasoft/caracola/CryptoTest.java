package com.polymitasoft.caracola;

import android.support.test.runner.AndroidJUnit4;
import android.support.v4.util.SimpleArrayMap;
import android.util.Log;

import com.google.common.io.BaseEncoding;
import com.google.common.primitives.Longs;
import com.polymitasoft.caracola.drm.Drm;

import org.junit.Test;
import org.junit.runner.RunWith;

import static com.google.common.io.BaseEncoding.base16;
import static com.google.common.io.BaseEncoding.base64;
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
        String plainString = base64().encode(Longs.toByteArray(Drm.getDeviceIdAsLong()));
        plainString += "-" + base64().encode(Longs.toByteArray(System.currentTimeMillis()));
        String encryptionKey = "Toawtef_*blyWef2";
        String encryptedString = Drm.encryptTo32String(plainString, encryptionKey);

        assertNotEquals(plainString, encryptedString);
        assertEquals(plainString, Drm.decryptFrom32String(encryptedString, encryptionKey));
    }

    @Test
    public void hashTest() throws Exception {
        String plainString = base64().encode(Longs.toByteArray(Drm.getDeviceIdAsLong()));
        Log.e(CryptoTest.class.getName(), "Code " + plainString);
    }

}
