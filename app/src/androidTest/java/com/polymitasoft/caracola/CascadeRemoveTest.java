package com.polymitasoft.caracola;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.polymitasoft.caracola.datamodel.Bedroom;
import com.polymitasoft.caracola.datamodel.BedroomBuilder;
import com.polymitasoft.caracola.datamodel.Booking;
import com.polymitasoft.caracola.datamodel.BookingBuilder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class CascadeRemoveTest {

    @Before
    public void beforeTests() {
        new DatabaseSetup().cleanDatabase();
    }

    @Test
    public void deleteBedroomWithBookings() throws Exception {
        EntityDataStore<Persistable> dataStore = CaracolaApplication.instance().getDataStore();
        Bedroom bedroom = new BedroomBuilder().build();
        dataStore.insert(bedroom);
        Booking booking = new BookingBuilder().bedroom(bedroom).build();
        dataStore.insert(booking);
        dataStore.delete(bedroom);

        assertEquals(0, dataStore.count(Booking.class).get().value().intValue());
    }
}
