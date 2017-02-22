package com.polymitasoft.caracola.view.booking;

import android.app.Activity;
import android.widget.EditText;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.components.DateSpinner;
import com.polymitasoft.caracola.datamodel.Booking;
import com.polymitasoft.caracola.datamodel.IBooking;
import com.polymitasoft.caracola.util.FormatUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rainermf on 16/2/2017.
 */

public class ActivityEditBookingBinding {

    private Booking booking;
    @BindView(R.id.booking_check_in) DateSpinner checkInDate;
    @BindView(R.id.booking_check_out) DateSpinner checkOutDate;
    @BindView(R.id.book_number) EditText bookingNumber;
    @BindView(R.id.booking_number) EditText bookNumber;
    @BindView(R.id.booking_price) EditText price;
    @BindView(R.id.booking_note) EditText note;

    public ActivityEditBookingBinding(Activity activity, Booking booking) {
        ButterKnife.bind(this, activity);
        setBooking(booking);
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
        checkInDate.setDate(booking.getCheckInDate());
        checkOutDate.setDate(booking.getCheckOutDate());
        bookingNumber.setText(booking.getBookingNumber());
        bookNumber.setText(booking.getBookNumber());
        price.setText(FormatUtils.formatMoney(booking.getPrice()));
        note.setText(booking.getNote());
    }

    public Booking getBooking() {
        booking.setCheckInDate(checkInDate.getDate());
        booking.setCheckOutDate(checkOutDate.getDate());
        booking.setBookingNumber(bookingNumber.getText().toString());
        booking.setBookNumber(bookNumber.getText().toString());
        booking.setPrice(FormatUtils.parseMoney(price.getText().toString()));
        booking.setNote(note.getText().toString());
        return booking;
    }
}
