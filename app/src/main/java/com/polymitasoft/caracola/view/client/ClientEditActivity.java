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

package com.polymitasoft.caracola.view.client;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.datamodel.Booking;
import com.polymitasoft.caracola.datamodel.Client;
import com.polymitasoft.caracola.datamodel.ClientBuilder;
import com.polymitasoft.caracola.datamodel.ClientStay;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

/**
 * Simple activity allowing you to edit a Person entity using data binding.
 */
public class ClientEditActivity extends AppCompatActivity {

    public static final String EXTRA_CLIENT_ID = "clientId";
    public static final String EXTRA_BOOKING_ID = "bookingId";

    private EntityDataStore<Persistable> data;
    private Client client;
    private ClientBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_client);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_edit_client);
        }
        data = DataStoreHolder.INSTANCE.getDataStore();
        int clientId = getIntent().getIntExtra(EXTRA_CLIENT_ID, -1);
        if (clientId == -1) {
            client = new ClientBuilder().build(); // creating a new client
        } else {
            client = data.findByKey(Client.class, clientId);
        }
        binding = new ClientBinding(this, client);
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
                saveClient();
                return true;
        }
        return false;
    }

    private void saveClient() {
        client = binding.getClient();
        data.upsert(client);

        int idBooking = getIntent().getIntExtra(EXTRA_BOOKING_ID, -1);
        if(idBooking != -1) {
            ClientStay stay = data.select(ClientStay.class)
                    .where(ClientStay.CLIENT_ID.eq(client.getId()))
                    .and(ClientStay.BOOKING_ID.eq(idBooking))
                    .get().firstOrNull();
            Booking booking = data.findByKey(Booking.class, idBooking);
            if(stay == null && booking != null) {
                stay = new ClientStay();
                stay.setClient(client);
                stay.setBooking(booking);
                data.insert(stay);
            }
        }
        finish();
    }
}
