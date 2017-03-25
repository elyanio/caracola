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
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.datamodel.Booking;
import com.polymitasoft.caracola.datamodel.BookingState;
import com.polymitasoft.caracola.datamodel.Client;
import com.polymitasoft.caracola.datamodel.Consumption;
import com.polymitasoft.caracola.view.client.ClientEditActivity;
import com.polymitasoft.caracola.view.client.ClientFragment;
import com.polymitasoft.caracola.view.consumption.ConsumptionEditActivity;
import com.polymitasoft.caracola.view.consumption.ConsumptionFragment;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

/**
 * Simple activity allowing you to edit a Person entity using data binding.
 */
public class BookingEditActivity extends AppCompatActivity implements ClientFragment.OnListInteractionListener, ConsumptionFragment.OnListInteractionListener, BookingEditFragment.OnBookingEdited {

    static final String EXTRA_BOOKING_ID = "bookingId";
    private Booking booking;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_booking);

        EntityDataStore<Persistable> data = DataStoreHolder.INSTANCE.getDataStore();
        int bookingId = getIntent().getIntExtra(EXTRA_BOOKING_ID, -1);
        if (bookingId == -1) {
            booking = new Booking(); // creating a new booking
        } else {
            booking = data.findByKey(Booking.class, bookingId);
        }
        booking.setState(BookingState.CHECKED_IN);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onBookingEdited(Booking booking) {
    }

    @Override
    protected void onPause() {
        super.onPause();
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof BookingEditFragment) {
                ((BookingEditFragment) fragment).saveBooking();
            }
        }
    }

    @Override
    public void onClientListInteraction(Client client) {
        upsertClient(client);
    }

    @Override
    public void onConsumptionListInteraction(Consumption consumption) {
        upsertConsumption(consumption);
    }

    public void addClient(View view) {
        upsertClient(null);
    }

    public void addConsumption(View view) {
        upsertConsumption(null);
    }

    private void upsertClient(@Nullable Client client) {
        Intent intent = new Intent(this, ClientEditActivity.class);
        if (client != null) {
            intent.putExtra(ClientEditActivity.EXTRA_CLIENT_ID, client.getId());
        }
        intent.putExtra(ClientEditActivity.EXTRA_BOOKING_ID, booking.getId());

        startActivity(intent);
    }

    private void upsertConsumption(@Nullable Consumption consumption) {
        Intent intent = new Intent(this, ConsumptionEditActivity.class);
        if (consumption != null) {
            intent.putExtra(ConsumptionEditActivity.EXTRA_CONSUMPTION_ID, consumption.getId());
        }
        intent.putExtra(ConsumptionEditActivity.EXTRA_BOOKING_ID, booking.getId());
        startActivity(intent);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return BookingEditFragment.newInstance(booking.getId());
                case 1:
                    return ClientFragment.newInstance(booking.getId());
                case 2:
                    return ConsumptionFragment.newInstance(booking.getId());
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.booking_general_tab_title);
                case 1:
                    return getString(R.string.booking_clients_tab_title);
                case 2:
                    return getString(R.string.booking_consumption_tab_title);
            }
            return null;
        }
    }
}
