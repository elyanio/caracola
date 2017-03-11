package com.polymitasoft.caracola.view.supplier;

import android.app.Activity;
import android.widget.EditText;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.datamodel.ExternalService;
import com.polymitasoft.caracola.datamodel.Supplier;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author rainermf
 * @since 28/2/2017
 */
class SupplierBinding {

    private Supplier supplier;
    @BindView(R.id.supplier_name) EditText name;
    @BindView(R.id.supplier_email) EditText emailAddress;
    @BindView(R.id.supplier_address) EditText address;
    @BindView(R.id.supplier_phone1) EditText phone1;
    @BindView(R.id.supplier_phone2) EditText phone2;
    @BindView(R.id.supplier_services) SupplierServicesSelector services;

    SupplierBinding(Activity activity, Supplier supplier) {
        ButterKnife.bind(this, activity);
        setSupplier(supplier);
    }

    public Supplier getSupplier() {
        supplier.setName(name.getText().toString());
        supplier.setEmailAddress(emailAddress.getText().toString());
        supplier.setAddress(address.getText().toString());
        supplier.setPhoneNumbers(Arrays.asList(
                phone1.getText().toString().trim(),
                phone2.getText().toString().trim()
        ));
        return supplier;
    }

    List<ExternalService> getServices() {
        return services.getServices();
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
        name.setText(supplier.getName());
        emailAddress.setText(supplier.getEmailAddress());
        address.setText(supplier.getAddress());
        services.setSupplier(supplier);
        List<String> phoneNumbers = supplier.getPhoneNumbers();
        if(phoneNumbers.size() > 0) {
            phone1.setText(phoneNumbers.get(0));
        }
        if(phoneNumbers.size() > 1) {
            phone2.setText(phoneNumbers.get(1));
        }
    }
}
