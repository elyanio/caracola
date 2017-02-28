package com.polymitasoft.caracola.view.supplier;

import android.app.Activity;
import android.widget.EditText;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.datamodel.Supplier;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author rainermf
 * @since 28/2/2017
 */
public class SupplierBinding {

    private Supplier supplier;
    @BindView(R.id.supplier_name) EditText name;
    @BindView(R.id.supplier_description) EditText description;
    @BindView(R.id.supplier_email) EditText emailAddress;
    @BindView(R.id.supplier_address) EditText address;

    public SupplierBinding(Activity activity, Supplier supplier) {
        ButterKnife.bind(this, activity);
        setSupplier(supplier);
    }

    public Supplier getSupplier() {
        supplier.setName(name.getText().toString());
        supplier.setDescription(description.getText().toString());
        supplier.setEmailAddress(emailAddress.getText().toString());
        supplier.setAddress(address.getText().toString());
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
        name.setText(supplier.getName());
        description.setText(supplier.getDescription());
        emailAddress.setText(supplier.getEmailAddress());
        address.setText(supplier.getAddress());
    }
}
