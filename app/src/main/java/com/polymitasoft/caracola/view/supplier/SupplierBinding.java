package com.polymitasoft.caracola.view.supplier;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.datamodel.ExternalService;
import com.polymitasoft.caracola.datamodel.Supplier;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static butterknife.ButterKnife.findById;

/**
 * @author rainermf
 * @since 28/2/2017
 */
class SupplierBinding {

    private final Activity activity;
    private Supplier supplier;
    @BindView(R.id.supplier_name) EditText nameText;
    @BindView(R.id.supplier_email) EditText emailAddressText;
    @BindView(R.id.supplier_address) EditText addressText;
    @BindView(R.id.supplier_phone1) EditText phone1Text;
    @BindView(R.id.supplier_phone2) EditText phone2Text;
    @BindView(R.id.supplier_services) SupplierServicesSelector services;

    SupplierBinding(Activity activity, Supplier supplier) {
        this.activity = activity;
        ButterKnife.bind(this, this.activity);
        setSupplier(supplier);
    }

    @Nullable
    public Supplier getSupplier() {
        if(!validate()) {
            return null;
        }
        supplier.setName(nameText.getText().toString());
        supplier.setEmailAddress(emailAddressText.getText().toString());
        supplier.setAddress(addressText.getText().toString());
        supplier.setPhoneNumbers(Arrays.asList(
                phone1Text.getText().toString().trim(),
                phone2Text.getText().toString().trim()
        ));
        return supplier;
    }

    List<ExternalService> getServices() {
        return services.getServices();
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
        nameText.setText(supplier.getName());
        emailAddressText.setText(supplier.getEmailAddress());
        addressText.setText(supplier.getAddress());
        services.setSupplier(supplier);
        List<String> phoneNumbers = supplier.getPhoneNumbers();
        if(phoneNumbers.size() > 0) {
            phone1Text.setText(phoneNumbers.get(0));
        }
        if(phoneNumbers.size() > 1) {
            phone2Text.setText(phoneNumbers.get(1));
        }
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
