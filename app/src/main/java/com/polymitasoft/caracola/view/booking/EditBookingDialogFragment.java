package com.polymitasoft.caracola.view.booking;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.codetroopers.betterpickers.numberpicker.NumberPickerBuilder;
import com.codetroopers.betterpickers.numberpicker.NumberPickerDialogFragment;
import com.polymitasoft.caracola.CaracolaApplication;
import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.communication.ManageSmsBooking;
import com.polymitasoft.caracola.components.DateSpinner;
import com.polymitasoft.caracola.dataaccess.BookingDao;
import com.polymitasoft.caracola.datamodel.Booking;
import com.polymitasoft.caracola.datamodel.BookingBuilder;
import com.polymitasoft.caracola.datamodel.BookingState;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;
import java.math.BigInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

public class EditBookingDialogFragment extends DialogFragment {

    private static final String ARG_BOOKING_ID = "bookingId";

    @BindView(R.id.booking_confirmed) SwitchCompat confirmedSwitch;
    @BindView(R.id.booking_note) TextView text_nota;
    @BindView(R.id.booking_price) Button textPrice;
    @BindView(R.id.booking_check_in) DateSpinner checkInDateSpinner;
    @BindView(R.id.booking_check_out) DateSpinner checkOutSpinner;
    OnBookingEditListener mCallback;
    private Booking preReserva;
    private EntityDataStore<Persistable> dataStore;
    private BookingDao bookingDao;

    public static EditBookingDialogFragment newInstance(Booking booking) {
        return newInstance(booking.getId());
    }

    /**
     * @deprecated This is not type safe, use newInstance({@link Booking}) instead.
     */
    @Deprecated
    public static EditBookingDialogFragment newInstance(int bookingId) {
        EditBookingDialogFragment fragment = new EditBookingDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_BOOKING_ID, bookingId);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.reserva_dialog_editar_reserva, null);
        ButterKnife.bind(this, view);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setTitle(R.string.title_edit_booking)
                .setPositiveButton(R.string.ok_action_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        clickeditR();
                    }
                })
                .setNegativeButton(R.string.cancel_action_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        dataStore = CaracolaApplication.instance().getDataStore();
        bookingDao = new BookingDao();
        int idBooking = getArguments().getInt(ARG_BOOKING_ID);
        preReserva = dataStore.findByKey(Booking.class, idBooking);

        configurarControles();

        return builder.create();

    }

    public void setBooking(Booking booking) {
        preReserva = booking;
    }

    private void configurarControles() {
        if(preReserva.getState() == BookingState.CHECKED_IN) {
            confirmedSwitch.setVisibility(View.GONE);
        } else {
            boolean confirmed = preReserva.getState() == BookingState.CONFIRMED;
            confirmedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    confirmedSwitch.setText(isChecked ? R.string.booking_confirmed : R.string.booking_pending);
                }
            });
            confirmedSwitch.setChecked(confirmed);
        }

        text_nota.setText(preReserva.getNote());
        textPrice.setText(getString(R.string.booking_price, preReserva.getPrice()));
        textPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new NumberPickerBuilder()
                        .setFragmentManager(getActivity().getSupportFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment_Light)
                        .setPlusMinusVisibility(View.INVISIBLE)
                        .addNumberPickerDialogHandler(new NumberPickerDialogFragment.NumberPickerDialogHandlerV2() {
                            @Override
                            public void onDialogNumberSet(int reference, BigInteger number, double decimal, boolean isNegative, BigDecimal fullNumber) {
                                preReserva.setPrice(fullNumber);
                                textPrice.setText(getString(R.string.booking_price, preReserva.getPrice()));
                            }
                        })
                        .show();
            }
        });
        checkInDateSpinner.bindForRange(checkOutSpinner);
        checkInDateSpinner.setDate(preReserva.getCheckInDate());
        checkOutSpinner.setDate(preReserva.getCheckOutDate());
        LocalDate minDate = bookingDao.previousBookedDay(preReserva.getBedroom(), preReserva.getCheckInDate()).plusDays(1);
        LocalDate maxDate = bookingDao.nextBookedDay(preReserva.getBedroom(), preReserva.getCheckOutDate()).minusDays(1);
        checkInDateSpinner.setMinDate(minDate);
        checkOutSpinner.setMaxDate(maxDate);
    }

    private void clickeditR() {
        Booking oldBooking = new BookingBuilder().with(preReserva).build();
        String nota = text_nota.getText().toString();
        BookingState estado;
        if(preReserva.getState() == BookingState.CHECKED_IN) {
            estado = BookingState.CHECKED_IN;
        } else {
            estado = confirmedSwitch.isChecked() ? BookingState.CONFIRMED : BookingState.PENDING;
        }

        preReserva.setCheckInDate(checkInDateSpinner.getDate())
                .setCheckOutDate(checkOutSpinner.getDate())
                .setState(estado)
                .setNote(nota);

        dataStore.update(preReserva);

        //todo enviar mensaje editadra
        if (preReserva.getBedroom().getCode() != 0) {
            sendMessage(oldBooking, preReserva);
        }
        mCallback.onBookingEdit(oldBooking, preReserva);
        dismiss();
    }

    private void sendMessage(Booking oldBooking, Booking newBooking) {

        ManageSmsBooking manageSmsBooking = new ManageSmsBooking(oldBooking, newBooking, getContext());
        manageSmsBooking.findBedroom();
        manageSmsBooking.findManager();
        manageSmsBooking.buildUpdateMessage();
        manageSmsBooking.enviar_mensaje();
    }

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

    // Container Activity must implement this interface
    public interface OnBookingEditListener {
        void onBookingEdit(Booking oldBooking, Booking newBooking);
    }
}
