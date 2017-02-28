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
import com.polymitasoft.caracola.datamodel.ExternalService;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

/**
 * Simple activity allowing you to edit a Person entity using data binding.
 */
public class ExternalServiceEditActivity extends AppCompatActivity {

    public static final String EXTRA_SERVICE_ID = "serviceId";

    private EntityDataStore<Persistable> data;
    private ExternalService service;
    private ExternalServiceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_external_service);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_edit_external_service);
        }
        data = CaracolaApplication.instance().getDataStore();
        int serviceId = getIntent().getIntExtra(EXTRA_SERVICE_ID, -1);
        if (serviceId == -1) {
            service = new ExternalService();
            service.setName("");
        } else {
            service = data.findByKey(ExternalService.class, serviceId);
        }
        binding = new ExternalServiceBinding(this, service);
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
                saveService();
                return true;
        }
        return false;
    }

    private void saveService() {
        service = binding.getService();
        data.upsert(service);
        finish();
    }
}
