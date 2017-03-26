
package com.polymitasoft.caracola.dataaccess;

import com.polymitasoft.caracola.datamodel.*;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

import static java.util.Arrays.asList;

/* This class is automatically generated. Do not modify it. */
public class DatabaseSetup {

    private final EntityDataStore<Persistable> data;

    public DatabaseSetup() {
        data = DataStoreHolder.INSTANCE.getDataStore();
    }

    private void cleanDatabase() {
        File dbFile = DataStoreHolder.INSTANCE.getDbFile();
        File directory = dbFile.getParentFile();
        directory.mkdirs();
        if (dbFile.exists()) {
            dbFile.delete();
        }
    }

    public void mockDatabase() {
        cleanDatabase();
        List<Bedroom> bedrooms = getBedrooms();
        List<InternalService> internalServices = getInternalServices();
        List<ExternalService> externalServices = getExternalServices();
        List<SupplierService> supplierServices = new ArrayList<>();
        List<Supplier> suppliers = getSuppliers(externalServices, supplierServices);

        insertList(bedrooms);
        insertList(internalServices);
        insertList(externalServices);
        insertList(suppliers);
        insertList(supplierServices);
    }

    private void insertList(List<? extends Persistable> list) {
        for (Persistable p : list) {
            data.insert(p);
        }
    }


    private List<Bedroom> getBedrooms() {
    List<Bedroom> bedrooms = new ArrayList<>();

bedrooms.add(new Bedroom().setName("Habitación 1").setCapacity(4).setPriceInLowSeason(new BigDecimal("25")).setPriceInHighSeason(new BigDecimal("30")));
return bedrooms;}

    private List<InternalService> getInternalServices() {
    List<InternalService> internalServices = new ArrayList<>();

internalServices.add(new InternalService().setName("Desayuno").setDefaultPrice(new BigDecimal("5")));
internalServices.add(new InternalService().setName("Almuerzo").setDefaultPrice(new BigDecimal("10")));
internalServices.add(new InternalService().setName("Copa de vino").setDefaultPrice(new BigDecimal("2.50")));
return internalServices;}

    private List<ExternalService> getExternalServices() {
    List<ExternalService> externalServices = new ArrayList<>();

externalServices.add(new ExternalService().setName("Buceo").setIcon("ic_scuba_diving"));
externalServices.add(new ExternalService().setName("Caminatas").setIcon(""));
externalServices.add(new ExternalService().setName("Taxi").setIcon("ic_taxi_stand"));
return externalServices;}

    private List<Supplier> getSuppliers(List<ExternalService> services, List<SupplierService> supplierServices) {
    List<Supplier> suppliers = new ArrayList<>();
    HashMap<String, ExternalService> serviceMap = new HashMap<>();
    for (ExternalService service: services) {
        serviceMap.put(service.getName(), service);
    }
    String[] serviceArray;
    Supplier supplier;
    
supplier = new Supplier().setName("Yanier Alfonso Rodríguez").setAddress("Aguacate #25").setEmailAddress("yanio@polymitasoft.com").setPhoneNumbers(asList("54150751","41554460"));
suppliers.add(supplier);

            serviceArray = new String[] {"Buceo|Buceo Azul","Caminatas|-"};
            for(String serviceInfo: serviceArray) {
                String[] serviceParts = serviceInfo.split("\\|");
                String serviceName = serviceParts[0].trim();
                String serviceDescription = serviceParts[1].trim();
                if(serviceDescription.equals("-")) {
                    serviceDescription = "";
                }
                supplierServices.add(new SupplierService().setSupplier(supplier).setService(serviceMap.get(serviceName)).setDescription(serviceDescription));
            }
            
supplier = new Supplier().setName("Rainer Martínez Fraga").setAddress("Carretera Central km 304 Banda Placetas").setEmailAddress("rmf@polymitasoft.com").setPhoneNumbers(asList("53746802"));
suppliers.add(supplier);

            serviceArray = new String[] {"Taxi|-"};
            for(String serviceInfo: serviceArray) {
                String[] serviceParts = serviceInfo.split("\\|");
                String serviceName = serviceParts[0].trim();
                String serviceDescription = serviceParts[1].trim();
                if(serviceDescription.equals("-")) {
                    serviceDescription = "";
                }
                supplierServices.add(new SupplierService().setSupplier(supplier).setService(serviceMap.get(serviceName)).setDescription(serviceDescription));
            }
            
return suppliers;}

}

