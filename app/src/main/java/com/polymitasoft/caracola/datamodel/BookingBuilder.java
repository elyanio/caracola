package com.polymitasoft.caracola.datamodel;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;

/**
 * Created by rainermf on 13/2/2017.
 */

public class BookingBuilder {

    private int id;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Bedroom bedroom;
    private BigDecimal price;
    private BookingState state;
    private String note;
    private String bookingNumber;
    private String bookNumber;

    public BookingBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public BookingBuilder setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
        return this;
    }

    public BookingBuilder setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
        return this;
    }

    public BookingBuilder setBedroom(Bedroom bedroom) {
        this.bedroom = bedroom;
        return this;
    }

    public BookingBuilder setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public BookingBuilder setState(BookingState state) {
        this.state = state;
        return this;
    }

    public BookingBuilder setNote(String note) {
        this.note = note;
        return this;
    }

    public BookingBuilder setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
        return this;
    }

    public BookingBuilder setBookNumber(String bookNumber) {
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
