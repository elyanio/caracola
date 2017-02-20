package com.polymitasoft.caracola.view.supplier;

import android.app.Activity;
import android.widget.EditText;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.datamodel.ExternalService;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author rainermf
 * @since 16/2/2017
 */
class ExternalServiceBinding {

    @BindView(R.id.service_name) EditText name;
    private ExternalService service;

    ExternalServiceBinding(Activity activity, ExternalService service) {
        ButterKnife.bind(this, activity);
        setService(service);
    }

    public ExternalService getService() {
        service.setName(name.getText().toString());
        return service;
    }

    public void setService(ExternalService service) {
        this.service = service;
        name.setText(service.getName());
    }
}
