package com.polymitasoft.caracola.datamodel;

import org.threeten.bp.LocalDate;

public class ClientBuilder {
    private String lastName = "";
    private String firstName = "";
    private String passport = "";
    private Gender gender = Gender.FEMININE;
    private Country country = Country.fromCode("CU");
    private LocalDate birthday = LocalDate.now().minusYears(25);

    public ClientBuilder lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public ClientBuilder firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public ClientBuilder passport(String passport) {
        this.passport = passport;
        return this;
    }

    public ClientBuilder gender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public ClientBuilder country(Country country) {
        this.country = country;
        return this;
    }

    public ClientBuilder birthday(LocalDate birthday) {
        this.birthday = birthday;
        return this;
    }

    public Client build() {
        Client client = new Client();
        client.setPassport(passport);
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setGender(gender);
        client.setCountry(country);
        client.setBirthday(birthday);

        return client;
    }
}