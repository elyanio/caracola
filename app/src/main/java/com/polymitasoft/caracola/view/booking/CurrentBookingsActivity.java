package com.polymitasoft.caracola.view.booking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.components.Colors;
import com.polymitasoft.caracola.components.RecyclerListActivity;
import com.polymitasoft.caracola.components.SimpleListAdapter;
import com.polymitasoft.caracola.components.SimpleViewHolder;
import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.datamodel.Booking;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import io.requery.Persistable;
import io.requery.android.QueryRecyclerAdapter;
import io.requery.query.Result;
import io.requery.sql.EntityDataStore;

import static com.polymitasoft.caracola.datamodel.Booking.CHECK_IN_DATE;
import static com.polymitasoft.caracola.datamodel.Booking.CHECK_OUT_DATE;

public class CurrentBookingsActivity extends RecyclerListActivity<Booking> implements EditBookingDialogFragment.OnBookingEditListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBarTitle(R.string.title_current_bookings);
    }

    @Override
    protected QueryRecyclerAdapter<Booking, ? extends RecyclerView.ViewHolder> createAdapter() {
        return new BookingAdapter();
    }

    @Override
    public void onBookingEdit(Booking oldBooking, Booking newBooking) {
        getAdapter().queryAsync();
    }

    @Override
    public void onBookingCreate(Booking newBooking) {

    }

    @Override
    public boolean isAddMenuVisible() {
        return false;
    }

    /**
     * Created by rainermf on 15/2/2017.
     */
    class BookingAdapter extends SimpleListAdapter<Booking> {

        private final DateTimeFormatter dateFormatter;
        private EntityDataStore<Persistable> data;

        BookingAdapter() {
            super(CurrentBookingsActivity.this, Booking.$TYPE);
            data = DataStoreHolder.INSTANCE.getDataStore();

            dateFormatter = DateTimeFormatter.ofPattern("d MMM");
        }

        @Override
        public Result<Booking> performQuery() {
            LocalDate today = LocalDate.now();
            return data.select(Booking.class)
                    .where(CHECK_IN_DATE.lessThanOrEqual(today))
                    .and(CHECK_OUT_DATE.greaterThanOrEqual(today.minusDays(1)))
                    .get();
        }

        @Override
        public void onBindViewHolder(Booking item, SimpleViewHolder holder, int position) {
            super.onBindViewHolder(item, holder, position);
            holder.primaryText.setText(item.getBedroom().getName());
            String date = dateFormatter.format(item.getCheckInDate()) + " / "
                    + dateFormatter.format(item.getCheckOutDate());
            holder.secondaryText.setText(date);
            holder.colorStrip.setBackgroundColor(Colors.INSTANCE.getColor(item.getId()));
        }

        @Override
        protected void viewItem(Booking item) {
            Intent intent = new Intent(CurrentBookingsActivity.this, BookingEditActivity.class);
            intent.putExtra(BookingEditActivity.EXTRA_BOOKING_ID, item.getId());
            startActivity(intent);
        }

        @Override
        protected void editItem(Booking item) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment prev = getSupportFragmentManager().findFragmentByTag("edit_booking_dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            // Create and show the dialog.
            EditBookingDialogFragment newFragment = EditBookingDialogFragment.newInstance(item);
            newFragment.show(ft, "edit_booking_dialog");
        }
    }
}
