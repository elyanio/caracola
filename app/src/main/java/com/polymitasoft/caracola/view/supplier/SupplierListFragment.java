package com.polymitasoft.caracola.view.supplier;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.polymitasoft.caracola.components.RecyclerListFragment;
import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.datamodel.ExternalService;
import com.polymitasoft.caracola.datamodel.Supplier;

import io.requery.Persistable;
import io.requery.android.QueryRecyclerAdapter;
import io.requery.sql.EntityDataStore;

/**
 * @author rainermf
 * @since 27/2/2017
 */

public class SupplierListFragment extends RecyclerListFragment<Supplier> {

    public static final String ARG_SERVICE_ID = "serviceId";

    public SupplierListFragment() {
    }

    public static SupplierListFragment newInstance() {
        
        Bundle args = new Bundle();
        
        SupplierListFragment fragment = new SupplierListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static SupplierListFragment newInstance(ExternalService externalService) {

        Bundle args = new Bundle();
        args.putInt(ARG_SERVICE_ID, externalService.getId());

        SupplierListFragment fragment = new SupplierListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected QueryRecyclerAdapter<Supplier, ? extends RecyclerView.ViewHolder> createAdapter() {
        EntityDataStore<Persistable> dataStore = DataStoreHolder.INSTANCE.getDataStore();
        int idService = getArguments().getInt(ARG_SERVICE_ID);
        ExternalService service = dataStore.findByKey(ExternalService.class, idService);
        return new SupplierAdapter(getContext(), service);
    }

    @Override
    protected void onActionPlusMenu() {
        startActivity(new Intent(getContext(), SupplierEditActivity.class));
    }
}
