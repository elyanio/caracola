package com.polymitasoft.caracola.dataaccess;

import android.widget.Toast;

import com.polymitasoft.caracola.CaracolaApplication;
import com.polymitasoft.caracola.datamodel.ExternalService;
import com.polymitasoft.caracola.datamodel.Supplier;
import com.polymitasoft.caracola.datamodel.SupplierService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        dataStore = DataStoreHolder.INSTANCE.getDataStore();
    }

    public Result<Supplier> withService(ExternalService service) {
        return dataStore.select(Supplier.class)
                .join(SupplierService.class).on(Supplier.ID.equal(SUPPLIER_ID))
                .join(ExternalService.class).on(SERVICE_ID.equal(ExternalService.ID))
                .where(ExternalService.ID.equal(service.getId()))
                .orderBy(Supplier.NAME.lower())
                .get();
    }

    public Result<Supplier> withoutService(ExternalService service) {
        List<Supplier> withServices = withService(service).toList();
        List<Integer> excludeList = new ArrayList<>(withServices.size());
        for(Supplier supplier : withServices) {
            excludeList.add(supplier.getId());
        }
        return dataStore.select(Supplier.class)
                .where(Supplier.ID.notIn(excludeList))
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

    public Result<ExternalService> notRenderedServices(Supplier supplier) {
        List<ExternalService> renderedServices = services(supplier).toList();
        List<Integer> excludeList = new ArrayList<>(renderedServices.size());
        for(ExternalService service : renderedServices) {
            excludeList.add(service.getId());
        }
        return dataStore.select(ExternalService.class)
                .where(ExternalService.ID.notIn(excludeList))
                .orderBy(ExternalService.NAME.lower())
                .get();
    }

    /**
     * Los servicios que están que no están en currentServices pero están en services hay que insertarlos
     * Los servicios que están en currentServices pero no están en services hay que eliminarlos
     *
     * @param supplier
     * @param services
     */
    public void updateServices(Supplier supplier, final Set<ExternalService> services) {
        HashSet<ExternalService> currentServices = new HashSet<>(services(supplier).toList());

        HashSet<ExternalService> toInsert = new HashSet<>(services);
        toInsert.removeAll(currentServices);
        HashSet<ExternalService> toRemove = new HashSet<>(currentServices);
        toRemove.removeAll(services);

        insertServices(supplier, toInsert);
        deleteServices(supplier, toRemove);
    }

    public void insertServices(Supplier supplier, Iterable<ExternalService> services) {
        List<SupplierService> sps = new ArrayList<>();
        for (ExternalService service : services) {
            SupplierService sp = new SupplierService()
                    .setService(service)
                    .setSupplier(supplier)
                    .setDescription("");
            sps.add(sp);
        }
        dataStore.insert(sps);
    }

    public void deleteServices(Supplier supplier, Iterable<ExternalService> services) {
        for (ExternalService service : services) {
            try {
                dataStore.delete(SupplierService.class)
                        .where(SERVICE_ID.eq(service.getId()))
                        .and(SUPPLIER_ID.eq(supplier.getId()))
                        .get()
                        .call();
            } catch (Exception e) {
                Toast.makeText(CaracolaApplication.instance(), "Error al eliminar los servicios", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
