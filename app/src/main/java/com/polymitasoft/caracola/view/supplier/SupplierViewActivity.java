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

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.polymitasoft.caracola.CaracolaApplication;
import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.components.RecyclerListActivity;
import com.polymitasoft.caracola.datamodel.Supplier;
import com.polymitasoft.caracola.datamodel.SupplierService;

import io.requery.Persistable;
import io.requery.android.QueryRecyclerAdapter;
import io.requery.sql.EntityDataStore;

public class SupplierViewActivity extends RecyclerListActivity<SupplierService> {

    public static final String EXTRA_SUPPLIER_ID = "supplierId";
    private Supplier supplier;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(supplier == null) {
            setBarTitle(R.string.title_external_services);
        } else {
            setBarTitle(supplier.getName());
        }
    }

    @Override
    protected QueryRecyclerAdapter<SupplierService, ? extends RecyclerView.ViewHolder> createAdapter() {
        Intent intent = getIntent();
        int serviceId = intent.getIntExtra(EXTRA_SUPPLIER_ID, -1);
        if(serviceId == -1) {
            throw new RuntimeException("You should pass a supplier to this activity");
        }
        EntityDataStore<Persistable> dataStore = CaracolaApplication.instance().getDataStore();
        supplier = dataStore.findByKey(Supplier.class, serviceId);
        return new ServiceSupplierAdapter(this, supplier);
    }
}