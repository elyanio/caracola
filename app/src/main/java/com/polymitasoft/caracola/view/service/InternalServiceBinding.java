package com.polymitasoft.caracola.view.service;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.datamodel.InternalService;
import com.polymitasoft.caracola.util.FormatUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static butterknife.ButterKnife.findById;

/**
 * @author rainermf
 * @since 16/2/2017
 */
class InternalServiceBinding {

    private final Activity activity;
    @BindView(R.id.service_name) EditText nameText;
    @BindView(R.id.service_price) EditText priceText;
    private InternalService service;

    InternalServiceBinding(Activity activity, InternalService service) {
        this.activity = activity;
        ButterKnife.bind(this, this.activity);
        setService(service);
    }

    @Nullable
    public InternalService getService() {
        if (!validate()) {
            return null;
        }
        service.setName(nameText.getText().toString());
        service.setDefaultPrice(FormatUtils.parseMoney(priceText.getText().toString()));
        return service;
    }

    public void setService(InternalService service) {
        this.service = service;
        nameText.setText(service.getName());
        priceText.setText(FormatUtils.formatMoney(service.getDefaultPrice()));
    }

    private boolean validate() {
        boolean valid = true;
        String name = nameText.getText().toString();
        if (name.trim().isEmpty()) {
            setError(R.id.layout_name, "El nombre no puede estar vacío");
            valid = false;
        } else {
            clearError(R.id.layout_name);
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
