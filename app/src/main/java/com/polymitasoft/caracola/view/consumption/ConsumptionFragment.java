package com.polymitasoft.caracola.view.consumption;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.components.SimpleListAdapter;
import com.polymitasoft.caracola.components.SimpleViewHolder;
import com.polymitasoft.caracola.dataaccess.BookingDao;
import com.polymitasoft.caracola.dataaccess.Bookings;
import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.datamodel.Booking;
import com.polymitasoft.caracola.datamodel.Consumption;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.requery.Persistable;
import io.requery.query.Result;
import io.requery.sql.EntityDataStore;

import static butterknife.ButterKnife.findById;
import static com.polymitasoft.caracola.dataaccess.Consumptions.cost;
import static com.polymitasoft.caracola.util.FormatUtils.formatDate;
import static com.polymitasoft.caracola.util.FormatUtils.formatMoneyWithCurrency;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListInteractionListener}
 * interface.
 */
public class ConsumptionFragment extends Fragment {

    private static final String ARG_BOOKING_ID = "bookingId";
    @BindView(R.id.totalPriceText) TextView totalPriceText;
    @BindView(R.id.consumptionPriceText) TextView consumptionPriceText;
    @BindView(R.id.lodgingPriceText) TextView lodgingPriceText;
    private OnListInteractionListener mListener;
    private ConsumptionRecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ConsumptionFragment() {
    }

    public static ConsumptionFragment newInstance(int bookingId) {
        ConsumptionFragment fragment = new ConsumptionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_BOOKING_ID, bookingId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_consumption_list, container, false);

        // Set the adapter
        if (view instanceof FrameLayout) {
            ButterKnife.bind(this, view);
            Context context = view.getContext();
            RecyclerView recyclerView = findById(view, R.id.list);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            EntityDataStore<Persistable> dataStore = DataStoreHolder.INSTANCE.getDataStore();
            int idBooking = getArguments().getInt(ARG_BOOKING_ID);
            Booking booking = dataStore.findByKey(Booking.class, idBooking);
            adapter = new ConsumptionRecyclerViewAdapter(booking);
            recyclerView.setAdapter(adapter);
        }
        return view;
    }

    @Override
    public void onResume() {
        adapter.queryAsync();
        adapter.updateTotals();
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListInteractionListener) {
            mListener = (OnListInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListInteractionListener {
        void onConsumptionListInteraction(Consumption consumption);
    }

    class ConsumptionRecyclerViewAdapter extends SimpleListAdapter<Consumption> {

        private final BookingDao bookingDao;
        private final Booking booking;

        ConsumptionRecyclerViewAdapter(Booking booking) {
            super(getContext(), Consumption.$TYPE);
            this.booking = booking;

            bookingDao = new BookingDao(this.dataStore);
        }

        @Override
        public Result<Consumption> performQuery() {
            return bookingDao.getConsumptions(booking);
        }

        @Override
        public void onBindViewHolder(final Consumption consumption, SimpleViewHolder holder, int position) {
            super.onBindViewHolder(consumption, holder, position);
            holder.primaryText.setText(consumption.getInternalService().getName());
            holder.secondaryText.setText(formatDate(consumption.getDate()));
            holder.tertiaryText.setText(formatMoneyWithCurrency(cost(consumption)));
            updateTotals();
        }

        @Override
        protected void viewItem(Consumption item) {
            editItem(item);
        }

        protected void editItem(Consumption consumption) {
            mListener.onConsumptionListInteraction(consumption);
            updateTotals();
        }

        @Override
        protected void deleteItem(Consumption item) {
            super.deleteItem(item);
            updateTotals();
        }

        private void updateTotals() {
            BigDecimal consumptionCost = bookingDao.getConsumptionCost(booking);
            BigDecimal lodgingCost = Bookings.lodgingCost(booking);

            consumptionPriceText.setText(formatMoneyWithCurrency(consumptionCost));
            lodgingPriceText.setText(formatMoneyWithCurrency(lodgingCost));
            totalPriceText.setText(formatMoneyWithCurrency(consumptionCost.add(lodgingCost)));
        }
    }
}
