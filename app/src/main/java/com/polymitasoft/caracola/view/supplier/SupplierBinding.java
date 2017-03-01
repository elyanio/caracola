package com.polymitasoft.caracola.view.supplier;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.polymitasoft.caracola.CaracolaApplication;
import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.datamodel.ExternalService;
import com.polymitasoft.caracola.datamodel.Supplier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.requery.Persistable;
import io.requery.android.QueryAdapter;
import io.requery.query.Result;
import io.requery.sql.EntityDataStore;

/**
 * @author rainermf
 * @since 28/2/2017
 */
public class SupplierBinding {

    private Supplier supplier;
    private Activity activity;
    @BindView(R.id.supplier_name) EditText name;
    @BindView(R.id.supplier_description) EditText description;
    @BindView(R.id.supplier_email) EditText emailAddress;
    @BindView(R.id.supplier_address) EditText address;
    @BindView(R.id.supplier_services) SupplierServices services;

    public SupplierBinding(Activity activity, Supplier supplier) {
        ButterKnife.bind(this, activity);
        this.activity = activity;
        setSupplier(supplier);
    }

    public Supplier getSupplier() {
        supplier.setName(name.getText().toString());
        supplier.setDescription(description.getText().toString());
        supplier.setEmailAddress(emailAddress.getText().toString());
        supplier.setAddress(address.getText().toString());
        // TODO Poner los servicios
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
        name.setText(supplier.getName());
        description.setText(supplier.getDescription());
        emailAddress.setText(supplier.getEmailAddress());
        address.setText(supplier.getAddress());
        services.setSupplier(supplier);
    }
}
