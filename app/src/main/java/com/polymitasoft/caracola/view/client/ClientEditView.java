package com.polymitasoft.caracola.view.client;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.polymitasoft.caracola.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rainermf on 13/2/2017.
 */

public class ClientEditView extends LinearLayout {

    @BindView(R.id.passportText) TextView passport;
    @BindView(R.id.firstNameText) TextView firstName;
    @BindView(R.id.lastNameText) TextView lastName;
    @BindView(R.id.birthdayText) TextView birthday;
    @BindView(R.id.countryText) TextView country;
    @BindView(R.id.genderText) TextView gender;

    public ClientEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        inflate(getContext(), R.layout.client_edit_view, this);
        ButterKnife.bind(this);
    }

    public String getPassport() {
        return passport.getText().toString();
    }

    public void setPassport(String passport) {
        this.passport.setText(passport);
    }

    public String getFirstName() {
        return firstName.getText().toString();
    }

    public void setFirstName(String firstName) {
        this.firstName.setText(firstName);
    }

    public String getLastName() {
        return lastName.getText().toString();
    }

    public void setLastName(String lastName) {
        this.lastName.setText(lastName);
    }

    public String getBirthday() {
        return birthday.getText().toString();
    }

    public void setBirthday(String birthday) {
        this.birthday.setText(birthday);
    }

    public String getCountry() {
        return country.getText().toString();
    }

    public void setCountry(String country) {
        this.country.setText(country);
    }

    public String getGender() {
        return gender.getText().toString();
    }

    public void setGender(String gender) {
        this.gender.setText(gender);
    }
}
