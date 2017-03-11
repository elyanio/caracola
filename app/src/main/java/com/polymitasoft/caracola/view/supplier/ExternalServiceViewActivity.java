/*
 * Copyright 2016 requery.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.polymitasoft.caracola.view.supplier;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.components.RecyclerListActivity;
import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.dataaccess.SupplierDao;
import com.polymitasoft.caracola.datamodel.ExternalService;
import com.polymitasoft.caracola.datamodel.Supplier;
import com.polymitasoft.caracola.datamodel.SupplierService;

import java.util.List;

import io.requery.Persistable;
import io.requery.android.QueryRecyclerAdapter;
import io.requery.sql.EntityDataStore;

public class ExternalServiceViewActivity extends RecyclerListActivity<SupplierService> {

    private ExternalService service;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (service == null) {
            setBarTitle(R.string.title_suppliers);
        } else {
            setBarTitle(service.getName());
        }
    }

    @Override
    protected QueryRecyclerAdapter<SupplierService, ? extends RecyclerView.ViewHolder> createAdapter() {
        Intent intent = getIntent();
        int serviceId = intent.getIntExtra(SupplierListFragment.ARG_SERVICE_ID, -1);
        if (serviceId == -1) {
            throw new RuntimeException("You should pass a service to this activity");
        }
        EntityDataStore<Persistable> dataStore = DataStoreHolder.INSTANCE.getDataStore();
        service = dataStore.findByKey(ExternalService.class, serviceId);
        return new SupplierServiceAdapter(this, service);
    }

    @Override
    protected void onActionAddMenu() {
        SupplierDao dao = new SupplierDao();
        final List<Supplier> suppliers = dao.withoutService(service).toList();
        int size = suppliers.size();

        if(size == 0) {
            new AlertDialog.Builder(this)
                    .setMessage("Todos los proveedores existentes ya han sido agregados a este servicio.")
                    .show();
            return;
        }

        CharSequence[] supplierLabels = new CharSequence[size];
        for (int i = 0; i < size; i++) {
            supplierLabels[i] = suppliers.get(i).getName();
        }
        new AlertDialog.Builder(this)
                .setItems(supplierLabels, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        addSupplierService(suppliers.get(which));
                    }
                })
                .show();
    }

    private void addSupplierService(Supplier supplier) {
        SupplierService item = new SupplierService()
                .setService(service)
                .setSupplier(supplier)
                .setDescription("");
        new SupplierServiceEditDialog(this, item)
                .setOkListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getAdapter().queryAsync();
                    }
                })
                .show();
    }
}