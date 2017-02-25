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
import com.polymitasoft.caracola.dataaccess.Bookings;
import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.datamodel.Booking;
import com.polymitasoft.caracola.datamodel.Consumption;
import com.polymitasoft.caracola.util.FormatUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

import static butterknife.ButterKnife.findById;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListInteractionListener}
 * interface.
 */
public class ConsumptionFragment extends Fragment {

    private static final String ARG_BOOKING_ID = "bookingId";
    private OnListInteractionListener mListener;
    private ConsumptionRecyclerViewAdapter adapter;
    @BindView(R.id.consumptionPriceText) TextView consumptionPriceText;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

            EntityDataStore<Persistable> dataStore = DataStoreHolder.getInstance().getDataStore(getContext());
            int idBooking = getArguments().getInt(ARG_BOOKING_ID);
            Booking booking = dataStore.findByKey(Booking.class, idBooking);
            adapter = new ConsumptionRecyclerViewAdapter(dataStore, booking, mListener);
            recyclerView.setAdapter(adapter);

            consumptionPriceText.setText(FormatUtils.formatMoney(Bookings.lodgingPrice(booking)));
        }
        return view;
    }

    @Override
    public void onResume() {
        adapter.queryAsync();
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
}