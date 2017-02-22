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

package com.polymitasoft.caracola.view.service;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.datamodel.InternalService;
import com.polymitasoft.caracola.datamodel.InternalService;

import java.math.BigDecimal;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

/**
 * Simple activity allowing you to edit a Person entity using data binding.
 */
public class InternalServiceEditActivity extends AppCompatActivity {

    public static final String EXTRA_SERVICE_ID = "serviceId";

    private EntityDataStore<Persistable> data;
    private InternalService service;
    private InternalServiceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_internal_service);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_edit_client);
        }
        data = DataStoreHolder.getInstance().getDataStore(this);
        int clientId = getIntent().getIntExtra(EXTRA_SERVICE_ID, -1);
        if (clientId == -1) {
            service = new InternalService().setName("").setDefaultPrice(BigDecimal.ZERO);
        } else {
            service = data.findByKey(InternalService.class, clientId);
        }
        binding = new InternalServiceBinding(this, service);
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
