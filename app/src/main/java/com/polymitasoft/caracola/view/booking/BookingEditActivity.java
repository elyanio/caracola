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

package com.polymitasoft.caracola.view.booking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.datamodel.Booking;
import com.polymitasoft.caracola.datamodel.Client;
import com.polymitasoft.caracola.datamodel.Consumption;
import com.polymitasoft.caracola.view.client.ConsumptionEditActivity;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

/**
 * Simple activity allowing you to edit a Person entity using data binding.
 */
public class BookingEditActivity extends AppCompatActivity implements ClientFragment.OnListInteractionListener, ConsumptionFragment.OnListInteractionListener {

    static final String EXTRA_BOOKING_ID = "bookingId";

    private EntityDataStore<Persistable> data;
    private Booking booking;
    private ActivityEditBookingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_booking);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_edit_booking);
        }
        data = DataStoreHolder.getInstance().getDataStore(this);
        int bookingId = getIntent().getIntExtra(EXTRA_BOOKING_ID, -1);
        if (bookingId == -1) {
            booking = new Booking(); // creating a new booking
        } else {
            booking = data.findByKey(Booking.class, bookingId);
        }
        binding = new ActivityEditBookingBinding(this, booking);

        if (findViewById(R.id.client_fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            ClientFragment firstFragment = ClientFragment.newInstance(booking);
            firstFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.client_fragment_container, firstFragment).commit();
        }

        if (findViewById(R.id.consumption_fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            ConsumptionFragment firstFragment = ConsumptionFragment.newInstance(booking);
            firstFragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.consumption_fragment_container, firstFragment).commit();
        }

        initTabs();

    }

    private void initTabs() {
        TabHost tabs = (TabHost) findViewById(android.R.id.tabhost);
        tabs.setup();
        TabHost.TabSpec spec = tabs.newTabSpec("booking_general_tab");
        spec.setContent(R.id.tab1);
        spec.setIndicator(getString(R.string.booking_general_tab_title));
        tabs.addTab(spec);

        spec = tabs.newTabSpec("booking_clients_tab");
        spec.setContent(R.id.tab2);
        spec.setIndicator(getString(R.string.booking_clients_tab_title));
        tabs.addTab(spec);

        spec = tabs.newTabSpec("booking_consumption_tab");
        spec.setContent(R.id.tab3);
        spec.setIndicator(getString(R.string.booking_consumption_tab_title));
        tabs.addTab(spec);

        tabs.setCurrentTab(0);
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
                saveBooking();
                return true;
        }
        return false;
    }

    private void saveBooking() {
        booking = binding.getBooking();
        data.update(booking);
        finish();
    }

    @Override
    public void onClientListInteraction(Client client) {

    }

    @Override
    public void onConsumptionListInteraction(Consumption consumption) {

    }

    public void addClient(View view) {
        Intent intent = new Intent(this, ConsumptionEditActivity.class);
        intent.putExtra(ConsumptionEditActivity.EXTRA_BOOKING_ID, booking.getId());
        startActivity(intent);
    }
}
