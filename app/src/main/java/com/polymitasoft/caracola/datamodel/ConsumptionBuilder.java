package com.polymitasoft.caracola.datamodel;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;

public class ConsumptionBuilder {
    private InternalService service;
    private LocalDate date = LocalDate.now();
    private BigDecimal price = BigDecimal.ZERO;
    private int amount = 1;
    private Booking booking;

    public ConsumptionBuilder name(InternalService service) {
        this.service = service;
        return this;
    }

    public ConsumptionBuilder date(LocalDate date) {
        this.date = date;
        return this;
    }

    public ConsumptionBuilder price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public ConsumptionBuilder amount(int amount) {
        this.amount = amount;
        return this;
    }

    public ConsumptionBuilder booking(Booking booking) {
        this.booking = booking;
        return this;
    }

    public Consumption build() {
        Consumption consumption = new ConsumptionEntity();
        consumption.setInternalService(service);
        consumption.setDate(date);
        consumption.setAmount(amount);
        consumption.setBooking(booking);
        consumption.setPrice(price);
        return consumption;
    }
}