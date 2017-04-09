package com.polymitasoft.caracola.view.booking;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.codetroopers.betterpickers.numberpicker.NumberPickerBuilder;
import com.codetroopers.betterpickers.numberpicker.NumberPickerDialogFragment;
import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.communication.ManageSmsBooking;
import com.polymitasoft.caracola.components.DateSpinner;
import com.polymitasoft.caracola.dataaccess.BookingDao;
import com.polymitasoft.caracola.dataaccess.Bookings;
import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.dataaccess.HostelDao;
import com.polymitasoft.caracola.datamodel.Bedroom;
import com.polymitasoft.caracola.datamodel.Booking;
import com.polymitasoft.caracola.datamodel.BookingBuilder;
import com.polymitasoft.caracola.datamodel.BookingState;
import com.polymitasoft.caracola.reminder.Alarm;
import com.polymitasoft.caracola.util.FormatUtils;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;
import java.math.BigInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

import static android.Manifest.permission.SEND_SMS;
import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;
import static com.polymitasoft.caracola.settings.Preferences.isSmsSyncEnabled;
import static com.polymitasoft.caracola.util.FormatUtils.parseDate;

public class EditBookingDialogFragment extends DialogFragment {

    private static final String ARG_BOOKING_ID = "bookingId";
    private static final String ARG_BEDROOM_ID = "bedroomId";
    private static final String ARG_CHECK_IN_DATE = "checkInDate";
    private static final String ARG_CHECK_OUT_DATE = "checkOutDate";

    @BindView(R.id.booking_confirmed)
    SwitchCompat confirmedSwitch;
    @BindView(R.id.booking_note)
    TextView text_nota;
    @BindView(R.id.booking_price)
    Button textPrice;
    @BindView(R.id.booking_check_in)
    DateSpinner checkInDateSpinner;
    @BindView(R.id.booking_check_out)
    DateSpinner checkOutSpinner;
    @BindView(R.id.layout_dates)
    PercentRelativeLayout datesContainer;

    private OnBookingEditListener mCallback;
    private Booking booking;
    private Booking oldBooking;
    private EntityDataStore<Persistable> dataStore;
    private BookingDao bookingDao;
    private HostelDao hostelDao;
    private boolean createMode = false;

    public static EditBookingDialogFragment newInstance(Bedroom bedroom, LocalDate checkInDate, LocalDate checkOutDate) {
        EditBookingDialogFragment fragment = new EditBookingDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_BEDROOM_ID, bedroom.getId());
        args.putString(ARG_CHECK_IN_DATE, FormatUtils.formatDate(checkInDate));
        args.putString(ARG_CHECK_OUT_DATE, FormatUtils.formatDate(checkOutDate));
        fragment.setArguments(args);
        return fragment;
    }

    public static EditBookingDialogFragment newInstance(Booking booking) {
        EditBookingDialogFragment fragment = new EditBookingDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_BOOKING_ID, booking.getId());
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.reserva_dialog_editar_reserva, null);
        ButterKnife.bind(this, view);
        dataStore = DataStoreHolder.INSTANCE.getDataStore();
        bookingDao = new BookingDao();
        hostelDao = new HostelDao();
        int idBooking = getArguments().getInt(ARG_BOOKING_ID, -1);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setTitle((idBooking == -1) ? R.string.reserva_titulo_hacer_reserva : R.string.title_edit_booking)
                .setPositiveButton(R.string.ok_action_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        clickeditR();
                    }
                })
                .setNegativeButton(R.string.cancel_action_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        if (idBooking == -1) {
            createMode = true;
            int idBedroom = getArguments().getInt(ARG_BEDROOM_ID);
            LocalDate firstNight = parseDate(getArguments().getString(ARG_CHECK_IN_DATE));
            LocalDate lastNight = parseDate(getArguments().getString(ARG_CHECK_OUT_DATE));
            Bedroom bedroom = dataStore.findByKey(Bedroom.class, idBedroom);
            booking = new BookingBuilder()
                    .price(Bookings.perNightPrice(bedroom))
                    .checkInDate(firstNight)
                    .checkOutDate(lastNight)
                    .bedroom(bedroom)
                    .build();
        } else {
            booking = dataStore.findByKey(Booking.class, idBooking);
        }
        oldBooking = new BookingBuilder().with(booking).build();

        configurarControles();

        return builder.create();

    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    private void configurarControles() {
        if (booking.getState() == BookingState.CHECKED_IN) {
            confirmedSwitch.setVisibility(View.GONE);
        } else {
            boolean confirmed = booking.getState() == BookingState.CONFIRMED;
            confirmedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    confirmedSwitch.setText(isChecked ? R.string.booking_confirmed : R.string.booking_pending);
                }
            });
            confirmedSwitch.setChecked(confirmed);
        }

        text_nota.setText(booking.getNote());
        textPrice.setText(getString(R.string.booking_price, booking.getPrice()));
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
                                booking.setPrice(fullNumber);
                                textPrice.setText(getString(R.string.booking_price, booking.getPrice()));
                            }
                        })
                        .show();
            }
        });
        if (createMode) {
            datesContainer.setVisibility(View.GONE);
        } else {
            checkInDateSpinner.bindForRange(checkOutSpinner);
            checkInDateSpinner.setDate(booking.getCheckInDate());
            checkOutSpinner.setDate(booking.getCheckOutDate());
            LocalDate minDate = bookingDao.previousBookedDay(booking.getBedroom(), booking.getCheckInDate()).plusDays(1);
            LocalDate maxDate = bookingDao.nextBookedDay(booking.getBedroom(), booking.getCheckOutDate()).minusDays(1);
            checkInDateSpinner.setMinDate(minDate);
            checkOutSpinner.setMaxDate(maxDate);
        }
    }

    private void clickeditR() {
        String nota = text_nota.getText().toString();
        if (nota.length() <= 0) {
            nota = " ";
        }
        BookingState estado;
        if (booking.getState() == BookingState.CHECKED_IN) {
            estado = BookingState.CHECKED_IN;
        } else {
            estado = confirmedSwitch.isChecked() ? BookingState.CONFIRMED : BookingState.PENDING;
        }

        if (!createMode) {
            booking.setCheckInDate(checkInDateSpinner.getDate())
                    .setCheckOutDate(checkOutSpinner.getDate());
        }
        booking.setState(estado).setNote(nota);
        dataStore.upsert(booking);

        if (createMode) {
            mCallback.onBookingCreate(booking);
            //        todo enviar menssaje
            sendMessage(booking);

            //set Alarm
            Alarm alarm = new Alarm(getContext());
            alarm.setAlarm(booking);

        } else {
            mCallback.onBookingEdit(oldBooking, booking);
            if(noNoteChange(oldBooking,booking)){
                //todo enviar mensaje editadra
                sendMessage(oldBooking, booking);
            }
            if(!oldBooking.getCheckInDate().isEqual(booking.getCheckInDate())){
                //set Alarm
                Alarm alarm = new Alarm(getContext());
                alarm.cancelAlarm(oldBooking);
                alarm.setAlarm(booking);
            }

        }


        dismiss();
    }

    private boolean noNoteChange(Booking oldBooking, Booking booking) {
        return !oldBooking.getCheckInDate().isEqual(booking.getCheckInDate()) || !oldBooking.getCheckOutDate().isEqual(booking.getCheckOutDate())
                || !oldBooking.getBedroom().equals(booking.getBedroom()) || oldBooking.getBookingNumber() != booking.getBookingNumber()
                || !oldBooking.getPrice().equals(booking.getPrice()) || !oldBooking.getState().equals(booking.getState())
                || oldBooking.getId() != booking.getId();
    }

    private static final int REQUEST_SEND_CREATE_SMS = 123;
    private static final int REQUEST_SEND_UPDATE_SMS = 124;

    private void sendMessage(Booking oldBooking, Booking newBooking) {
        if (shouldSendMessage(newBooking)) {
            if (ContextCompat.checkSelfPermission(getContext(), SEND_SMS) == PERMISSION_GRANTED) {
                new ManageSmsBooking(oldBooking, newBooking).sendUpdateMessage();
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{SEND_SMS}, REQUEST_SEND_UPDATE_SMS);
            }
        }
    }

    private void sendMessage(Booking booking) {
        if (shouldSendMessage(booking)) {
            if (ContextCompat.checkSelfPermission(getContext(), SEND_SMS) == PERMISSION_GRANTED) {
                new ManageSmsBooking(booking).sendCreateMessage();
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{SEND_SMS}, REQUEST_SEND_CREATE_SMS);
            }
        }
    }

    private boolean shouldSendMessage(Booking booking) {
        return isSmsSyncEnabled()
                && (booking.getBedroom().getCode() != 0)
                && hostelDao.getManagerCount(booking.getBedroom().getHostel()) != 0;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_SEND_CREATE_SMS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new ManageSmsBooking(booking).sendCreateMessage();
            }
        } else if (requestCode == REQUEST_SEND_UPDATE_SMS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new ManageSmsBooking(oldBooking, booking).sendUpdateMessage();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
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

        void onBookingCreate(Booking newBooking);
    }
}
