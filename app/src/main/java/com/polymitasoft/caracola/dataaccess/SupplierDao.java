package com.polymitasoft.caracola.dataaccess;

import com.polymitasoft.caracola.CaracolaApplication;
import com.polymitasoft.caracola.datamodel.ExternalService;
import com.polymitasoft.caracola.datamodel.Supplier;
import com.polymitasoft.caracola.datamodel.SupplierService;

import io.requery.Persistable;
import io.requery.query.Result;
import io.requery.sql.EntityDataStore;

import static com.polymitasoft.caracola.datamodel.SupplierService.SERVICE_ID;
import static com.polymitasoft.caracola.datamodel.SupplierService.SUPPLIER_ID;

/**
 * @author rainermf
 * @since 1/3/2017
 */

public class SupplierDao {

    private EntityDataStore<Persistable> dataStore;

    public SupplierDao() {
        dataStore = CaracolaApplication.instance().getDataStore();
    }

    public Result<Supplier> withService(ExternalService service) {
        return dataStore.select(Supplier.class)
                .join(SupplierService.class).on(Supplier.ID.equal(SUPPLIER_ID))
                .join(ExternalService.class).on(SERVICE_ID.equal(ExternalService.ID))
                .where(ExternalService.ID.equal(service.getId()))
                .orderBy(Supplier.NAME.lower())
                .get();
    }

    public Result<ExternalService> services(Supplier supplier) {
        return dataStore.select(ExternalService.class)
                .join(SupplierService.class).on(ExternalService.ID.equal(SERVICE_ID))
                .join(Supplier.class).on(SUPPLIER_ID.equal(Supplier.ID))
                .where(Supplier.ID.equal(supplier.getId()))
                .orderBy(ExternalService.NAME.lower())
                .get();
    }
}
