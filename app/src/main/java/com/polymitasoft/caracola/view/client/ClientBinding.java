package com.polymitasoft.caracola.view.client;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.datamodel.Client;
import com.polymitasoft.caracola.datamodel.Country;
import com.polymitasoft.caracola.datamodel.Gender;
import com.polymitasoft.caracola.util.FormatUtils;

import org.threeten.bp.LocalDate;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author rainermf
 * @since 16/2/2017
 */
class ClientBinding {

    private final AppCompatActivity activity;
    @BindView(R.id.passportText) EditText passport;
    @BindView(R.id.firstNameText) EditText firstName;
    @BindView(R.id.lastNameText) EditText lastName;
    @BindView(R.id.birthdayText) EditText birthday;
    @BindView(R.id.genderSpinner) Spinner gender;
    @BindView(R.id.countryText) EditText country;
    private CalendarDatePickerDialogFragment birthdayPicker;
    private Client client;

    ClientBinding(AppCompatActivity activity, Client client) {
        ButterKnife.bind(this, activity);
        this.activity = activity;
        initPickers();
        setClient(client);
    }

    private void initPickers() {
        birthdayPicker = new CalendarDatePickerDialogFragment()
                .setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                    @Override
                    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                        LocalDate date = LocalDate.of(year, monthOfYear + 1, dayOfMonth);
                        birthday.setText(FormatUtils.formatDate(date));
                        client.setBirthday(date);
                    }
                });
        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                birthdayPicker.show(activity.getSupportFragmentManager(), "birthdayPicker");
            }
        });

        ArrayAdapter<Gender> adapter = new ArrayAdapter<>(activity,
                android.R.layout.simple_spinner_item, Gender.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapter);
    }

    public Client getClient() {
        client.setPassport(passport.getText().toString());
        client.setFirstName(firstName.getText().toString());
        client.setLastName(lastName.getText().toString());
        client.setGender((Gender) gender.getSelectedItem());
        client.setCountry(Country.fromCode(country.getText().toString()));
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
        passport.setText(client.getPassport());
        firstName.setText(client.getFirstName());
        lastName.setText(client.getLastName());
        LocalDate date = client.getBirthday();
        birthday.setText(FormatUtils.formatDate(date));
        birthdayPicker.setPreselectedDate(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth());
        gender.setSelection(client.getGender().ordinal());
        country.setText(client.getCountry().getCode());
    }
}
