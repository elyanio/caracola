package com.polymitasoft.caracola.view.booking;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.polymitasoft.caracola.CaracolaApplication;
import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.datamodel.Booking;
import com.polymitasoft.caracola.datamodel.BookingBuilder;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnBookingEdited}
 * interface.
 */
public class BookingEditFragment extends Fragment {

    private static final String ARG_BOOKING_ID = "bookingId";
    private static final String PREF_BOOK_NUMBER = "bookNumber";
    private static final String PREF_BOOKING_NUMBER = "bookingNumber";
    private Booking booking = null;
    private Booking oldBooking = null;
    private OnBookingEdited mListener;
    private EditBookingBinding binding;
    private EntityDataStore<Persistable> dataStore;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BookingEditFragment() {
    }

    public static BookingEditFragment newInstance(int bookingId) {
        BookingEditFragment fragment = new BookingEditFragment();
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
        View view = inflater.inflate(R.layout.fragment_edit_booking, container, false);
        int idBooking = getArguments().getInt(ARG_BOOKING_ID);
        dataStore = DataStoreHolder.INSTANCE.getDataStore();
        booking = dataStore.findByKey(Booking.class, idBooking);
        oldBooking = new BookingBuilder().with(booking).build();
        Log.e("shit", booking.getBookNumber() + " : " + booking.getBookingNumber());
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(CaracolaApplication.instance());
        if(booking.getBookNumber() == -1) {
            booking.setBookNumber(preferences.getInt(PREF_BOOK_NUMBER, 0));
        }
        if(booking.getBookingNumber() == -1) {
            booking.setBookingNumber(preferences.getInt(PREF_BOOKING_NUMBER, 0));
        }
        binding = new EditBookingBinding(view, booking);

        return view;
    }

    void saveBooking() {
        if (binding != null) {
            booking = binding.getBooking();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(CaracolaApplication.instance());
            SharedPreferences.Editor editor = preferences.edit();
            if(booking.getBookNumber() != oldBooking.getBookNumber()) {
                editor.putInt(PREF_BOOK_NUMBER, booking.getBookNumber());
            }
            if(booking.getBookingNumber() != oldBooking.getBookingNumber()) {
                editor.putInt(PREF_BOOKING_NUMBER, booking.getBookingNumber() + 1);
            }
            editor.apply();
            dataStore.update(booking);
            mListener.onBookingEdited(booking);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnBookingEdited) {
            mListener = (OnBookingEdited) context;
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

    public interface OnBookingEdited {
        void onBookingEdited(Booking booking);
    }
}
