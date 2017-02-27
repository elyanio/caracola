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

package com.polymitasoft.caracola.view.consumption;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.polymitasoft.caracola.CaracolaApplication;
import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.datamodel.Booking;
import com.polymitasoft.caracola.datamodel.Consumption;
import com.polymitasoft.caracola.datamodel.ConsumptionBuilder;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

/**
 * Simple activity allowing you to edit a Person entity using data binding.
 */
public class ConsumptionEditActivity extends AppCompatActivity {

    public static final String EXTRA_CONSUMPTION_ID = "consumptionId";
    public static final String EXTRA_BOOKING_ID = "bookingId";

    private EntityDataStore<Persistable> data;
    private Consumption consumption;
    private ConsumptionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_consumption);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_edit_consumption);
        }
        data = CaracolaApplication.instance().getDataStore();
        int consumptionId = getIntent().getIntExtra(EXTRA_CONSUMPTION_ID, -1);
        if (consumptionId == -1) {
            int idBooking = getIntent().getIntExtra(EXTRA_BOOKING_ID, -1);
            Booking booking = null;
            if(idBooking != -1) {
                booking = data.findByKey(Booking.class, idBooking);
            }
            consumption = new ConsumptionBuilder()
                    .booking(booking)
                    .build();
        } else {
            consumption = data.findByKey(Consumption.class, consumptionId);
        }
        binding = new ConsumptionBinding(this, consumption);
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
        consumption = binding.getConsumption();
        if(consumption.getInternalService() == null) {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.error_select_service)
                    .setPositiveButton(R.string.ok_action_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        } else {
            data.upsert(consumption);
            finish();
        }
    }
}
