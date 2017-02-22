package com.polymitasoft.caracola.view.client;

import android.app.Activity;
import android.widget.EditText;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.datamodel.Client;
import com.polymitasoft.caracola.datamodel.IClient;
import com.polymitasoft.caracola.datamodel.Country;
import com.polymitasoft.caracola.datamodel.Gender;
import com.polymitasoft.caracola.util.FormatUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author rainermf
 * @since 16/2/2017
 */
class ClientBinding {

    @BindView(R.id.passportText) EditText passport;
    @BindView(R.id.firstNameText) EditText firstName;
    @BindView(R.id.lastNameText) EditText lastName;
    @BindView(R.id.birthdayText) EditText birthday;
    @BindView(R.id.genderText) EditText gender;
    @BindView(R.id.countryText) EditText country;
    private Client client;

    ClientBinding(Activity activity, Client client) {
        ButterKnife.bind(this, activity);
        setClient(client);
    }

    public Client getClient() {
        client.setPassport(passport.getText().toString());
        client.setFirstName(firstName.getText().toString());
        client.setLastName(lastName.getText().toString());
        client.setBirthday(FormatUtils.parseDate(birthday.getText().toString()));
        client.setGender(Gender.valueOf(gender.getText().toString()));
        client.setCountry(Country.fromCode(country.getText().toString()));
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
        passport.setText(client.getPassport());
        firstName.setText(client.getFirstName());
        lastName.setText(client.getLastName());
        birthday.setText(FormatUtils.formatDate(client.getBirthday()));
        gender.setText(client.getGender().toString());
        country.setText(client.getCountry().getCode());
    }
}
