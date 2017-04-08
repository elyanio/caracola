package com.polymitasoft.caracola;

import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;

/**
 * @author rainermf
 * @since 24/3/2017
 */
public class DeviceIdTest {

    @Test
    public void testDeviceId() {
        BigInteger bigInt = new BigInteger("924c46dff1449660", 16);
        long number = bigInt.longValue();
        assertEquals(number, 0x924c46dff1449660L);
    }
}
