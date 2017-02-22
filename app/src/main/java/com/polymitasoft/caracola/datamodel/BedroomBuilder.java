package com.polymitasoft.caracola.datamodel;

import java.math.BigDecimal;

public class BedroomBuilder {
    private String name = "";
    private int capacity = 4;
    private BigDecimal priceInLowSeason = BigDecimal.valueOf(25);
    private BigDecimal priceInHighSeason = BigDecimal.valueOf(30);

    public BedroomBuilder name(String name) {
        this.name = name;
        return this;
    }

    public BedroomBuilder capacity(int capacity) {
        this.capacity = capacity;
        return this;
    }

    public BedroomBuilder priceInLowSeason(BigDecimal priceInLowSeason) {
        this.priceInLowSeason = priceInLowSeason;
        return this;
    }

    public BedroomBuilder priceInHighSeason(BigDecimal priceInHighSeason) {
        this.priceInHighSeason = priceInHighSeason;
        return this;
    }

    public Bedroom build() {
        return new Bedroom()
                .setName(name)
                .setCapacity(capacity)
                .setPriceInHighSeason(priceInHighSeason)
                .setPriceInLowSeason(priceInLowSeason);
    }
}