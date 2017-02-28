package com.polymitasoft.caracola.datamodel;

import java.util.Collections;
import java.util.List;

public class SupplierBuilder {

    private String name = "";
    private String description = "";
    private String emailAddress = "";
    private String address = "";
    private List<String> phoneNumbers = Collections.emptyList();

    public SupplierBuilder name(String name) {
        this.name = name;
        return this;
    }

    public SupplierBuilder description(String description) {
        this.description = description;
        return this;
    }

    public SupplierBuilder emailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    public SupplierBuilder address(String address) {
        this.address = address;
        return this;
    }

    public SupplierBuilder phoneNumbers(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
        return this;
    }

    public Supplier build() {
        return new Supplier()
                .setName(name)
                .setDescription(description)
                .setEmailAddress(emailAddress)
                .setAddress(address)
                .setPhoneNumbers(phoneNumbers);
    }
}