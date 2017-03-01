package com.polymitasoft.caracola.view.supplier;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.google.common.base.Function;
import com.polymitasoft.caracola.CaracolaApplication;
import com.polymitasoft.caracola.dataaccess.SupplierDao;
import com.polymitasoft.caracola.datamodel.ExternalService;
import com.polymitasoft.caracola.datamodel.Supplier;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.requery.Persistable;
import io.requery.query.Result;
import io.requery.sql.EntityDataStore;

import static com.google.common.collect.Collections2.transform;
import static com.polymitasoft.caracola.datamodel.ExternalService.NAME;

/**
 * @author rainermf
 * @since 1/3/2017
 */

public class SupplierServices extends Button {

    private EntityDataStore<Persistable> dataStore;
    private AlertDialog.Builder builder;
    private SupplierDao dao;

    public SupplierServices(Context context) {
        super(context);
        init();
    }

    public SupplierServices(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SupplierServices(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (isInEditMode()) return;
        dataStore = CaracolaApplication.instance().getDataStore();
        dao = new SupplierDao();

        builder = new AlertDialog.Builder(getContext())
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
                });
    }

    private Set<Integer> getServiceLookup(Supplier supplier) {
        List<ExternalService> supplierServices = dao.services(supplier).toList();
        return new HashSet<>(transform(supplierServices, new Function<ExternalService, Integer>() {
            @Override
            public Integer apply(ExternalService input) {
                return input.getId();
            }
        }));
    }

    public void setSupplier(Supplier supplier) {
        Result<ExternalService> serviceResult = dataStore.select(ExternalService.class).orderBy(NAME).get();
        List<ExternalService> allServices = serviceResult.toList();
        Set<Integer> lookup = getServiceLookup(supplier);

        int size = allServices.size();
        final String[] servicesArray = new String[size];
        final boolean[] selection = new boolean[size];

        for (int i = 0; i < size; i++) {
            ExternalService service = allServices.get(i);
            servicesArray[i] = service.getName();
            selection[i] = lookup.contains(service.getId());
        }

        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setMultiChoiceItems(servicesArray, selection, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    }
                }).show();
            }
        });
    }
}
