package com.polymitasoft.caracola.view.supplier;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.dataaccess.SupplierDao;
import com.polymitasoft.caracola.datamodel.ExternalService;
import com.polymitasoft.caracola.datamodel.Supplier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.requery.Persistable;
import io.requery.query.Result;
import io.requery.sql.EntityDataStore;

import static com.polymitasoft.caracola.datamodel.ExternalService.NAME;

/**
 * @author rainermf
 * @since 1/3/2017
 */

public class SupplierServicesSelector extends Button {

    private EntityDataStore<Persistable> dataStore;
    private SupplierDao dao;
    private boolean[] selection;
    private List<ExternalService> allServices;

    public SupplierServicesSelector(Context context) {
        super(context);
        init();
    }

    public SupplierServicesSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SupplierServicesSelector(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (isInEditMode()) return;
        dataStore = DataStoreHolder.INSTANCE.getDataStore();
        dao = new SupplierDao();
    }

    private Set<Integer> getServiceLookup(Supplier supplier) {
        List<ExternalService> supplierServices = dao.services(supplier).toList();
        Set<Integer> set = new HashSet<>(supplierServices.size());
        for (ExternalService service : supplierServices) {
            set.add(service.getId());
        }
        return set;
    }

    public void setSupplier(Supplier supplier) {
        Result<ExternalService> serviceResult = dataStore.select(ExternalService.class).orderBy(NAME).get();
        allServices = serviceResult.toList();
        Set<Integer> lookup = getServiceLookup(supplier);

        int size = allServices.size();
        final String[] servicesArray = new String[size];
        selection = new boolean[size];

        for (int i = 0; i < size; i++) {
            ExternalService service = allServices.get(i);
            servicesArray[i] = service.getName();
            selection[i] = lookup.contains(service.getId());
        }

        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Servicios")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setMultiChoiceItems(servicesArray, selection, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            }
                        }).show();
            }
        });
    }

    public List<ExternalService> getServices() {
        List<ExternalService> services = new ArrayList<>();
        int size = selection.length;
        for(int i = 0; i < size; i++) {
            if(selection[i]) {
                services.add(allServices.get(i));
            }
        }
        return services;
    }
}
