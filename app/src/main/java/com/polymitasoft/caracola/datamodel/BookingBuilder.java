package com.polymitasoft.caracola.datamodel;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;

/**
 * @author rainermf
 * @since 13/2/2017
 */
public class BookingBuilder {

    private LocalDate checkInDate = LocalDate.now();
    private LocalDate checkOutDate = LocalDate.now();
    private Bedroom bedroom;
    private BigDecimal price = BigDecimal.ZERO;
    private BookingState state = BookingState.CONFIRMED;
    private String note = "";
    private int bookingNumber = -1;
    private int bookNumber = -1;

    public BookingBuilder with(Booking booking) {
        checkInDate = booking.getCheckInDate();
        checkOutDate = booking.getCheckOutDate();
        bedroom = booking.getBedroom();
        price = booking.getPrice();
        state = booking.getState();
        note = booking.getNote();
        bookingNumber = booking.getBookingNumber();
        bookNumber = booking.getBookNumber();
        return this;
    }

    public BookingBuilder checkInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
        return this;
    }

    public BookingBuilder checkOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
        return this;
    }

    public BookingBuilder bedroom(Bedroom bedroom) {
        this.bedroom = bedroom;
        return this;
    }

    public BookingBuilder price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public BookingBuilder state(BookingState state) {
        this.state = state;
        return this;
    }

    public BookingBuilder note(String note) {
        this.note = note;
        return this;
    }

    public BookingBuilder bookingNumber(int bookingNumber) {
        this.bookingNumber = bookingNumber;
        return this;
    }

    public BookingBuilder bookNumber(int bookNumber) {
        this.bookNumber = bookNumber;
        return this;
    }

    public Booking build() {
        return new Booking()
                .setState(state)
                .setPrice(price)
                .setBookingNumber(bookingNumber)
                .setBookNumber(bookNumber)
                .setCheckInDate(checkInDate)
                .setCheckOutDate(checkOutDate)
                .setNote(note)
                .setBedroom(bedroom);
    }
}
