package com.polymitasoft.caracola;

import android.support.test.runner.AndroidJUnit4;

import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.datamodel.Bedroom;
import com.polymitasoft.caracola.datamodel.BedroomBuilder;
import com.polymitasoft.caracola.datamodel.Booking;
import com.polymitasoft.caracola.datamodel.BookingBuilder;
import com.polymitasoft.caracola.datamodel.Consumption;
import com.polymitasoft.caracola.datamodel.ConsumptionBuilder;
import com.polymitasoft.caracola.datamodel.InternalService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

import static java.math.BigDecimal.ZERO;
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
        EntityDataStore<Persistable> dataStore = DataStoreHolder.INSTANCE.getDataStore();
        Bedroom bedroom = new BedroomBuilder().build();
        dataStore.insert(bedroom);
        Booking booking = new BookingBuilder().bedroom(bedroom).build();
        dataStore.insert(booking);
        dataStore.delete(bedroom);

        assertEquals(0, dataStore.count(Booking.class).get().value().intValue());
    }

    @Test
    public void deleteServiceWithConsumptions() throws Exception {
        EntityDataStore<Persistable> dataStore = DataStoreHolder.INSTANCE.getDataStore();
        InternalService service = new InternalService().setName("").setDefaultPrice(ZERO);
        InternalService service2 = new InternalService().setName("").setDefaultPrice(ZERO);
        dataStore.insert(service);
        dataStore.insert(service2);
        Bedroom bedroom = new BedroomBuilder().build();
        dataStore.insert(bedroom);
        Booking booking = new BookingBuilder().bedroom(bedroom).build();
        dataStore.insert(booking);
        Consumption c = new ConsumptionBuilder().booking(booking).service(service).build();
        dataStore.insert(c);

        boolean exceptionThrown = false;
        try {
            dataStore.delete(service);
        } catch (Exception e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);

        exceptionThrown = false;
        try {
            dataStore.delete(service2);
        } catch (Exception e) {
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }

    @Test
    public void deleteBookingWithConsumptions() throws Exception {
        EntityDataStore<Persistable> dataStore = DataStoreHolder.INSTANCE.getDataStore();
        InternalService service = new InternalService().setName("").setDefaultPrice(ZERO);
        dataStore.insert(service);
        Bedroom bedroom = new BedroomBuilder().build();
        dataStore.insert(bedroom);
        Booking booking = new BookingBuilder().bedroom(bedroom).build();
        dataStore.insert(booking);
        Consumption c = new ConsumptionBuilder().booking(booking).service(service).build();
        dataStore.insert(c);

        dataStore.delete(booking);
        assertEquals(0, dataStore.count(Consumption.class).get().value().intValue());

        boolean exceptionThrown = false;
        try {
            dataStore.delete(service);
        } catch (Exception e) {
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }
}
