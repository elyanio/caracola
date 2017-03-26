package com.polymitasoft.caracola.view.client;

import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.codetroopers.betterpickers.datepicker.DatePickerBuilder;
import com.codetroopers.betterpickers.datepicker.DatePickerDialogFragment;
import com.mukesh.countrypicker.fragments.CountryPicker;
import com.mukesh.countrypicker.interfaces.CountryPickerListener;
import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.datamodel.Client;
import com.polymitasoft.caracola.datamodel.Country;
import com.polymitasoft.caracola.datamodel.Gender;
import com.polymitasoft.caracola.util.FormatUtils;

import org.threeten.bp.LocalDate;

import butterknife.BindView;
import butterknife.ButterKnife;

import static butterknife.ButterKnife.findById;

/**
 * @author rainermf
 * @since 16/2/2017
 */
class ClientBinding {

    private final AppCompatActivity activity;
    private final DatePickerBuilder birthdayPicker;
    private final CountryPicker countryPicker;
    @BindView(R.id.passportText) EditText passportText;
    @BindView(R.id.firstNameText) EditText firstNameText;
    @BindView(R.id.lastNameText) EditText lastNameText;
    @BindView(R.id.birthdayText) EditText birthday;
    @BindView(R.id.genderSpinner) Spinner gender;
    @BindView(R.id.countryText) EditText country;
    private Client client;

    ClientBinding(AppCompatActivity activity, Client client) {
        ButterKnife.bind(this, activity);
        this.activity = activity;
        birthdayPicker = createDatePicker();
        countryPicker = createCountryPicker();
        initComponents();
        setClient(client);
    }

    private DatePickerBuilder createDatePicker() {
        return new DatePickerBuilder()
                .setFragmentManager(activity.getSupportFragmentManager())
                .setStyleResId(R.style.BetterPickersDialogFragment_Light)
                .addDatePickerDialogHandler(new DatePickerDialogFragment.DatePickerDialogHandler() {
                    @Override
                    public void onDialogDateSet(int reference, int year, int monthOfYear, int dayOfMonth) {
                        LocalDate date = LocalDate.of(year, monthOfYear + 1, dayOfMonth);
                        birthday.setText(FormatUtils.formatDate(date));
                        client.setBirthday(date);
                    }
                });
    }

    private CountryPicker createCountryPicker() {
        final CountryPicker picker = CountryPicker.newInstance("Seleccione un país");
        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                country.setText(name);
                client.setCountry(Country.fromCode(code));
                picker.dismiss();
            }
        });
        return picker;
    }

    private void initComponents() {
        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                birthdayPicker.show();
            }
        });
        country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countryPicker.show(activity.getSupportFragmentManager(), "COUNTRY_PICKER");
            }
        });

        ArrayAdapter<Gender> adapter = new ArrayAdapter<>(activity,
                android.R.layout.simple_spinner_item, Gender.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapter);
    }

    @Nullable
    public Client getClient() {
        if(!validate()) {
            return null;
        }
        client.setPassport(passportText.getText().toString());
        client.setFirstName(firstNameText.getText().toString());
        client.setLastName(lastNameText.getText().toString());
        client.setGender((Gender) gender.getSelectedItem());
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
        passportText.setText(client.getPassport());
        firstNameText.setText(client.getFirstName());
        lastNameText.setText(client.getLastName());
        birthday.setText(FormatUtils.formatDate(client.getBirthday()));
        gender.setSelection(client.getGender().ordinal());
        country.setText(client.getCountry().getName());
    }

    private boolean validate() {
        boolean valid = true;
        String passport = passportText.getText().toString();
        if (passport.trim().isEmpty()) {
            setError(R.id.layout_passport, "El pasaporte no puede estar vacío");
            valid = false;
        } else {
            clearError(R.id.layout_passport);
        }
        String firstName = firstNameText.getText().toString();
        if (firstName.trim().isEmpty()) {
            setError(R.id.layout_first_name, "El nombre no puede estar vacío");
            valid = false;
        } else {
            clearError(R.id.layout_first_name);
        }
        return valid;
    }

    private void setError(@IdRes int idRes, CharSequence message) {
        TextInputLayout layout = findById(activity, idRes);
        layout.setError(message);
    }

    private void clearError(@IdRes int idRes) {
        TextInputLayout layout = findById(activity, idRes);
        layout.setErrorEnabled(false);
    }
}
