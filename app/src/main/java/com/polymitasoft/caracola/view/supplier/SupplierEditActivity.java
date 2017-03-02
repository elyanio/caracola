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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.polymitasoft.caracola.CaracolaApplication;
import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.dataaccess.SupplierDao;
import com.polymitasoft.caracola.datamodel.ExternalService;
import com.polymitasoft.caracola.datamodel.Supplier;
import com.polymitasoft.caracola.datamodel.SupplierBuilder;

import java.util.HashSet;
import java.util.List;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

/**
 * Simple activity allowing you to edit a Person entity using data binding.
 */
public class SupplierEditActivity extends AppCompatActivity {

    public static final String EXTRA_SUPPLIER_ID = "supplierId";

    private EntityDataStore<Persistable> data;
    private Supplier supplier;
    private SupplierBinding binding;
    private SupplierDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_supplier);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_edit_supplier);
        }
        data = CaracolaApplication.instance().getDataStore();
        dao = new SupplierDao();
        int supplierId = getIntent().getIntExtra(EXTRA_SUPPLIER_ID, -1);
        if (supplierId == -1) {
            supplier = new SupplierBuilder().build();
        } else {
            supplier = data.findByKey(Supplier.class, supplierId);
        }
        binding = new SupplierBinding(this, supplier);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                save();
                return true;
        }
        return false;
    }

    private void save() {
        supplier = binding.getSupplier();
        data.upsert(supplier);
        dao.updateServices(supplier, new HashSet<>(binding.getServices()));
        finish();
    }
}
