package com.polymitasoft.caracola.view.client;

import android.app.Activity;
import android.widget.EditText;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.datamodel.Consumption;
import com.polymitasoft.caracola.util.FormatUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author rainermf
 * @since 16/2/2017
 */
class ConsumptionBinding {

    @BindView(R.id.amountText) EditText amount;
    @BindView(R.id.priceText) EditText price;
    @BindView(R.id.dateText) EditText date;
//    @BindView(R.id.serviceWiget) EditText service;
    private Consumption consumption;

    ConsumptionBinding(Activity activity, Consumption consumption) {
        ButterKnife.bind(this, activity);
        setConsumption(consumption);
    }

    public Consumption getConsumption() {
//        consumption.setInternalService(service.getInternalService());
        consumption.setAmount(Integer.parseInt(amount.getText().toString()));
        consumption.setPrice(FormatUtils.parseMoney(price.getText().toString()));
        consumption.setDate(FormatUtils.parseDate(date.getText().toString()));

        return consumption;
    }

    public void setConsumption(Consumption consumption) {
        this.consumption = consumption;
//        service.setInternalService(consumption.getInternalService());
        amount.setText(String.valueOf(consumption.getAmount()));
        price.setText(FormatUtils.formatMoney(consumption.getPrice()));
        date.setText(FormatUtils.formatDate(consumption.getDate()));
    }
}
