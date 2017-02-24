package com.polymitasoft.caracola.view.booking;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.components.DateSpinner;
import com.polymitasoft.caracola.dataaccess.BookingDao;
import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.datamodel.Booking;
import com.polymitasoft.caracola.datamodel.BookingBuilder;
import com.polymitasoft.caracola.datamodel.BookingState;
import com.polymitasoft.caracola.util.FormatUtils;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

public class EditBookingDialogFragment extends DialogFragment {

    private static final String ARG_BOOKING_ID = "bookingId";
    private Booking preReserva;
    @BindView(R.id.reserva_bt_hacer_pre_R) Button bt_preReservar;
    @BindView(R.id.reserva_rb_confirmada) RadioButton rb_confirmado;
    @BindView(R.id.reserva_rb_pendiente) RadioButton rb_pendiente;
    @BindView(R.id.booking_note) TextView text_nota;
    @BindView(R.id.booking_price) TextView textPrice;
    @BindView(R.id.booking_check_in) DateSpinner checkInDateSpinner;
    @BindView(R.id.booking_check_out) DateSpinner checkOutSpinner;
    private EntityDataStore<Persistable> dataStore;
    private BookingDao bookingDao;

    public static EditBookingDialogFragment newInstance(int bookingId) {
        EditBookingDialogFragment fragment = new EditBookingDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_BOOKING_ID, bookingId);
        fragment.setArguments(args);
        return fragment;
    }

//    @NonNull
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        // Use the Builder class for convenient dialog construction
//        View view = getActivity().getLayoutInflater().inflate(R.layout.reserva_dialog_editar_reserva, null);
//        ButterKnife.bind(this, view);
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setView(view)
//                .setTitle(R.string.reserva_titulo_hacer_reserva)
//                .setPositiveButton("fire", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // FIRE ZE MISSILES!
//                    }
//                })
//                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // User cancelled the dialog
//                    }
//                });
//        // Create the AlertDialog object and return it
//
//        dataStore = DataStoreHolder.getInstance().getDataStore(getActivity().getApplicationContext());
//        bookingDao = new BookingDao(dataStore);
//
//        int idBooking = getArguments().getInt(ARG_BOOKING_ID);
//        preReserva = dataStore.findByKey(Booking.class, idBooking);
//
//        configurarControles();
//        eventos();
//
//        return builder.create();
//
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.reserva_dialog_editar_reserva, container);
        ButterKnife.bind(this, view);
        dataStore = DataStoreHolder.getInstance().getDataStore(getActivity().getApplicationContext());
        bookingDao = new BookingDao(dataStore);

        int idBooking = getArguments().getInt(ARG_BOOKING_ID);
        preReserva = dataStore.findByKey(Booking.class, idBooking);

        getDialog().setTitle(R.string.reserva_titulo_hacer_reserva);
        configurarControles();
        eventos();

        return view;
    }

    public void setBooking(Booking booking) {
        preReserva = booking;
    }

    private void configurarControles() {
        text_nota.setText(preReserva.getNote());
        textPrice.setText(FormatUtils.formatMoney(preReserva.getPrice()));
        if ((preReserva.getState() == BookingState.CONFIRMED)) {
            rb_confirmado.setChecked(true);
        } else {
            rb_confirmado.setChecked(false);
            rb_pendiente.setChecked(true);
        }
        checkInDateSpinner.bindForRange(checkOutSpinner);
        checkInDateSpinner.setDate(preReserva.getCheckInDate());
        checkOutSpinner.setDate(preReserva.getCheckOutDate());
        LocalDate minDate = bookingDao.previousBookedDay(preReserva.getBedroom(), preReserva.getCheckInDate()).plusDays(1);
        LocalDate maxDate = bookingDao.nextBookedDay(preReserva.getBedroom(), preReserva.getCheckOutDate()).minusDays(1);
        checkInDateSpinner.setMinDate(minDate);
        checkOutSpinner.setMaxDate(maxDate);
    }

    private void eventos() {
        bt_preReservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickeditR();
            }
        });
    }

    private void clickeditR() {
        Booking oldBooking = new BookingBuilder().with(preReserva).build();
        String nota = text_nota.getText().toString();
        BookingState estado = rb_confirmado.isChecked() ? BookingState.CONFIRMED : BookingState.PENDING;
        BigDecimal price = FormatUtils.parseMoney(textPrice.getText().toString());

        preReserva.setCheckInDate(checkInDateSpinner.getDate())
                .setCheckOutDate(checkOutSpinner.getDate())
                .setPrice(price)
                .setState(estado)
                .setNote(nota);

        dataStore.update(preReserva);
        mCallback.onBookingEdit(oldBooking, preReserva);

        dismiss();
    }

    // Container Activity must implement this interface
    public interface OnBookingEditListener {
        public void onBookingEdit(Booking oldBooking, Booking newBooking);
    }

    OnBookingEditListener mCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnBookingEditListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

}
