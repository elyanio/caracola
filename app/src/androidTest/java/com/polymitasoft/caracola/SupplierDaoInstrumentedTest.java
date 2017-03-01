package com.polymitasoft.caracola;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.polymitasoft.caracola.dataaccess.SupplierDao;
import com.polymitasoft.caracola.datamodel.ExternalService;
import com.polymitasoft.caracola.datamodel.Supplier;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import io.requery.Persistable;
import io.requery.query.Result;
import io.requery.sql.EntityDataStore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author rainermf
 * @since 1/3/2017
 */

public class SupplierDaoInstrumentedTest {

    @Before
    public void beforeTests() {
        new DatabaseSetup().start();
    }

    @Test
    public void testServices() {
        SupplierDao dao = new SupplierDao();
        EntityDataStore<Persistable> dataStore = CaracolaApplication.instance().getDataStore();
        Supplier supplier = dataStore.findByKey(Supplier.class, 1);
        List<ExternalService> services = dao.services(supplier).toList();

        assertEquals(services.size(), 1);
    }
}
