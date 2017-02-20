package com.polymitasoft.caracola.view.service;

import android.app.Activity;
import android.widget.EditText;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.datamodel.Client;
import com.polymitasoft.caracola.datamodel.Country;
import com.polymitasoft.caracola.datamodel.Gender;
import com.polymitasoft.caracola.datamodel.InternalService;
import com.polymitasoft.caracola.util.FormatUtils;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author rainermf
 * @since 16/2/2017
 */
class InternalServiceBinding {

    @BindView(R.id.service_name) EditText name;
    @BindView(R.id.service_price) EditText price;
    private InternalService service;

    InternalServiceBinding(Activity activity, InternalService service) {
        ButterKnife.bind(this, activity);
        setService(service);
    }

    public InternalService getService() {
        service.setName(name.getText().toString());
        service.setDefaultPrice(FormatUtils.parseMoney(price.getText().toString()));
        return service;
    }

    public void setService(InternalService service) {
        this.service = service;
        name.setText(service.getName());
        price.setText(FormatUtils.formatMoney(service.getDefaultPrice()));
    }
}
