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

package com.polymitasoft.caracola.view.bedroom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.polymitasoft.caracola.DataStoreHolder;
import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.datamodel.Bedroom;
import com.polymitasoft.caracola.datamodel.BedroomBuilder;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

/**
 * Simple activity allowing you to edit a Person entity using data binding.
 */
public class BedroomEditActivity extends AppCompatActivity {

    static final String EXTRA_BEDROOM_ID = "bedroomId";

    private EntityDataStore<Persistable> data;
    private Bedroom bedroom;
    private BedroomBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bedroom);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_edit_bedroom);
        }
        data = DataStoreHolder.getInstance().getDataStore(this);
        int bedroomId = getIntent().getIntExtra(EXTRA_BEDROOM_ID, -1);
        if (bedroomId == -1) {
            bedroom = new BedroomBuilder().build(); // creating a new bedroom
        } else {
            bedroom = data.findByKey(Bedroom.class, bedroomId);
        }
        binding = new BedroomBinding(this, bedroom);
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
                saveBedroom();
                return true;
        }
        return false;
    }

    private void saveBedroom() {
        bedroom = binding.getBedroom();
        data.upsert(bedroom);
        finish();
    }
}
