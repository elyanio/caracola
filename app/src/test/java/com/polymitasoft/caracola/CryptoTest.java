package com.polymitasoft.caracola;

import com.polymitasoft.caracola.drm.Drm;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by rainermf on 5/13/2017.
 */
public class CryptoTest {

    @Test
    public void cryptoTest() throws Exception {
        String plainString = "ZdMEXiILF2Y=";
        String encryptedString = Drm.generateCode(plainString);
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
