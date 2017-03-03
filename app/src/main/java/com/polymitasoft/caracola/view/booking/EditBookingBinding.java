package com.polymitasoft.caracola.view.booking;

import android.view.View;
import android.widget.EditText;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.components.DateSpinner;
import com.polymitasoft.caracola.datamodel.Booking;
import com.polymitasoft.caracola.util.FormatUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author rainermf
 * @since 16/2/2017
 */
class EditBookingBinding {

    private Booking booking;
    @BindView(R.id.book_number) EditText bookingNumber;
    @BindView(R.id.booking_number) EditText bookNumber;

    EditBookingBinding(View view, Booking booking) {
        ButterKnife.bind(this, view);
        setBooking(booking);
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
        bookingNumber.setText(booking.getBookingNumber());
        bookNumber.setText(booking.getBookNumber());
    }

    public Booking getBooking() {
        booking.setBookingNumber(bookingNumber.getText().toString());
        booking.setBookNumber(bookNumber.getText().toString());
        return booking;
    }
}
