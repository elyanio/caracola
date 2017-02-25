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
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.datamodel.IBooking;
import com.polymitasoft.caracola.datamodel.Booking;

import org.threeten.bp.LocalDate;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.requery.Persistable;
import io.requery.android.QueryRecyclerAdapter;
import io.requery.query.Result;
import io.requery.sql.EntityDataStore;

/**
 * Activity displaying a list of random people. You can tap on a person to edit their record.
 * Shows how to use a query with a {@link RecyclerView} and {@link QueryRecyclerAdapter} and RxJava
 */
public class CurrentBookingsActivity extends AppCompatActivity {

    @BindView(R.id.bookingsRecyclerView) RecyclerView recyclerView;
    private EntityDataStore<Persistable> data;
    private ExecutorService executor;
    private BookingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_current_bookings);
        }
        setContentView(R.layout.activity_current_bookings);
        ButterKnife.bind(this);
        data = DataStoreHolder.getInstance().getDataStore(this);
        executor = Executors.newSingleThreadExecutor();
        adapter = new BookingAdapter(data);
        adapter.setExecutor(executor);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_plus:
                Intent intent = new Intent(this, BookingEditActivity.class);
                startActivity(intent);
                return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        adapter.queryAsync();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        executor.shutdown();
        adapter.close();
        super.onDestroy();
    }

    static class BookingHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.primary_text) TextView name;
        @BindView(R.id.color_strip) View image;

        BookingHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * Created by rainermf on 15/2/2017.
     */
    class BookingAdapter extends QueryRecyclerAdapter<IBooking, BookingHolder> implements View.OnClickListener {

        private final Random random = new Random();
        private final int[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA};
        private EntityDataStore<Persistable> data;

        BookingAdapter(EntityDataStore<Persistable> dataStore) {
//            super(BookingEntity.$TYPE);
            data = dataStore;
        }

        @Override
        public Result<IBooking> performQuery() {
            // this is all persons in the db sorted by their name
            // note this method in executed in a background thread.
            // (Alternatively RxJava w/ RxBinding could be used)
            LocalDate today = LocalDate.now();
            return data.select(IBooking.class)
                    .where(Booking.CHECK_IN_DATE.lessThanOrEqual(today))
                    .and(Booking.CHECK_OUT_DATE.greaterThanOrEqual(today))
                    .get();
        }

        @Override
        public void onBindViewHolder(IBooking item, BookingHolder holder,
                                     int position) {
            holder.name.setText(item.getBedroom().getName());
            holder.image.setBackgroundColor(colors[random.nextInt(colors.length)]);
            holder.itemView.setTag(item);
        }

        @Override
        public BookingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.simple_list_item, null);
            BookingHolder holder = new BookingHolder(view);
            view.setOnClickListener(this);
            return holder;
        }

        @Override
        public void onClick(View v) {
            IBooking booking = (IBooking) v.getTag();
            if (booking != null) {
                Intent intent = new Intent(CurrentBookingsActivity.this, BookingEditActivity.class);
                intent.putExtra(BookingEditActivity.EXTRA_BOOKING_ID, booking.getId());
                startActivity(intent);
            }
        }
    }
}
