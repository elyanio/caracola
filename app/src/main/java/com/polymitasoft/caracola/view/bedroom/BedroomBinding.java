package com.polymitasoft.caracola.view.bedroom;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.datamodel.Bedroom;
import com.polymitasoft.caracola.util.FormatUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static butterknife.ButterKnife.findById;

/**
 * @author rainermf
 * @since 16/2/2017
 */
class BedroomBinding {

    private final Activity activity;
    @BindView(R.id.price_high_season) TextInputEditText priceInHighSeasonText;
    @BindView(R.id.price_low_season) TextInputEditText priceInLowSeasonText;
    @BindView(R.id.capacity) TextInputEditText capacityText;
    @BindView(R.id.bedroom_name) TextInputEditText nameText;
    private Bedroom bedroom;

    BedroomBinding(Activity activity, Bedroom bedroom) {
        this.activity = activity;
        ButterKnife.bind(this, this.activity);
        setBedroom(bedroom);
    }

    @Nullable
    public Bedroom getBedroom() {
        if (!validate()) {
            return null;
        }
        bedroom.setName(nameText.getText().toString());
        bedroom.setCapacity(Integer.parseInt(capacityText.getText().toString()));
        bedroom.setPriceInHighSeason(FormatUtils.parseMoney(priceInHighSeasonText.getText().toString()));
        bedroom.setPriceInLowSeason(FormatUtils.parseMoney(priceInLowSeasonText.getText().toString()));
        return bedroom;
    }

    public void setBedroom(Bedroom bedroom) {
        this.bedroom = bedroom;
        priceInHighSeasonText.setText(FormatUtils.formatMoney(bedroom.getPriceInHighSeason()));
        priceInLowSeasonText.setText(FormatUtils.formatMoney(bedroom.getPriceInLowSeason()));
        capacityText.setText(String.valueOf(bedroom.getCapacity()));
        nameText.setText(bedroom.getName());
    }

    private boolean validate() {
        boolean valid = true;
        String name = nameText.getText().toString();
        if (name.trim().isEmpty()) {
            setError(R.id.bedroom_name_layout, "El nombre no puede estar vacío");
            valid = false;
        } else {
            clearError(R.id.bedroom_name_layout);
        }
        try {
            Integer.parseInt(capacityText.getText().toString());
            clearError(R.id.capacity_layout);
        } catch (NumberFormatException e) {
            setError(R.id.capacity_layout, "Capacidad no válida.");
            valid = false;
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
