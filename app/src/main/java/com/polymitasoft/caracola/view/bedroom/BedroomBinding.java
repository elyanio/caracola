package com.polymitasoft.caracola.view.bedroom;

import android.app.Activity;
import android.widget.EditText;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.datamodel.Bedroom;
import com.polymitasoft.caracola.util.FormatUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rainermf on 16/2/2017.
 */

public class BedroomBinding {

    private Bedroom bedroom;
    @BindView(R.id.price_high_season) EditText priceInHighSeason;
    @BindView(R.id.price_low_season) EditText priceInLowSeason;
    @BindView(R.id.capacity) EditText capacity;
    @BindView(R.id.bedroom_name) EditText name;

    public BedroomBinding(Activity activity, Bedroom bedroom) {
        ButterKnife.bind(this, activity);
        setBedroom(bedroom);
    }

    public void setBedroom(Bedroom bedroom) {
        this.bedroom = bedroom;
        priceInHighSeason.setText(FormatUtils.formatMoney(bedroom.getPriceInHighSeason()));
        priceInLowSeason.setText(FormatUtils.formatMoney(bedroom.getPriceInLowSeason()));
        capacity.setText(String.valueOf(bedroom.getCapacity()));
        name.setText(bedroom.getName());
    }

    public Bedroom getBedroom() {
        bedroom.setPriceInHighSeason(FormatUtils.parseMoney(priceInHighSeason.getText().toString()));
        bedroom.setPriceInLowSeason(FormatUtils.parseMoney(priceInLowSeason.getText().toString()));
        bedroom.setCapacity(Integer.parseInt(capacity.getText().toString()));
        bedroom.setName(name.getText().toString());
        return bedroom;
    }
}
