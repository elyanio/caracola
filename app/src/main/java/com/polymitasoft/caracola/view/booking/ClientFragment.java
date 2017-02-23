package com.polymitasoft.caracola.view.booking;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.datamodel.Booking;
import com.polymitasoft.caracola.datamodel.Client;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

import static butterknife.ButterKnife.findById;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListInteractionListener}
 * interface.
 */
public class ClientFragment extends Fragment {

    private static final String ARG_BOOKING_ID = "booking-id";
    private OnListInteractionListener mListener;
    private ClientRecyclerViewAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ClientFragment() {
    }

    public static ClientFragment newInstance(int bookingId) {
        ClientFragment fragment = new ClientFragment();
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
        View view = inflater.inflate(R.layout.fragment_client_list, container, false);

        // Set the adapter
        if (view instanceof FrameLayout) {
            Context context = view.getContext();
            RecyclerView recyclerView = findById(view, R.id.list);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            EntityDataStore<Persistable> dataStore = DataStoreHolder.getInstance().getDataStore(context);
            int idBooking = getArguments().getInt(ARG_BOOKING_ID);
            Booking booking = dataStore.findByKey(Booking.class, idBooking);
            adapter = new ClientRecyclerViewAdapter(dataStore, booking, mListener);
            recyclerView.setAdapter(adapter);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListInteractionListener {
        void onClientListInteraction(Client client);
    }
}
