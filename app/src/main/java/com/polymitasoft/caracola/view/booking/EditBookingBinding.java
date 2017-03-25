package com.polymitasoft.caracola.view.booking;

import android.view.View;
import android.widget.EditText;

import com.google.common.primitives.Ints;
import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.datamodel.Booking;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author rainermf
 * @since 16/2/2017
 */
class EditBookingBinding {

    @BindView(R.id.booking_number) EditText bookingNumberView;
    private Booking booking;

    EditBookingBinding(View view, Booking booking) {
        ButterKnife.bind(this, view);
        setBooking(booking);
    }

    public Booking getBooking() {
        Integer bookingNumber = Ints.tryParse(bookingNumberView.getText().toString().trim());

        booking.setBookingNumber(bookingNumber != null ? bookingNumber : -1);
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
        bookingNumberView.setText(String.format("%03d", booking.getBookingNumber()));
    }
}
