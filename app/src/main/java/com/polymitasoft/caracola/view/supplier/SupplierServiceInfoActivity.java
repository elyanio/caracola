package com.polymitasoft.caracola.view.supplier;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.datamodel.SupplierService;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

public class SupplierServiceInfoActivity extends AppCompatActivity {

    public static final String ARG_SUPPLIER_SERVICE_ID = "supplierServiceId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_service_info);

        Intent intent = getIntent();
        int supplierServiceId = intent.getIntExtra(ARG_SUPPLIER_SERVICE_ID, -1);
        if (supplierServiceId == -1) {
            throw new RuntimeException("You should pass a supplierService to this activity");
        }
        EntityDataStore<Persistable> dataStore = DataStoreHolder.INSTANCE.getDataStore();

        SupplierService supplierService = dataStore.findByKey(SupplierService.class, supplierServiceId);

        SupplierInfoFragment fragment = SupplierInfoFragment.newInstance(supplierService);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment).commit();
    }

}
