package com.polymitasoft.caracola.view.supplier;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.polymitasoft.caracola.CaracolaApplication;
import com.polymitasoft.caracola.components.RecyclerListActivity;
import com.polymitasoft.caracola.components.RecyclerListFragment;
import com.polymitasoft.caracola.datamodel.Supplier;
import com.polymitasoft.caracola.datamodel.SupplierService;

import java.util.EnumSet;

import io.requery.Persistable;
import io.requery.android.QueryRecyclerAdapter;
import io.requery.sql.EntityDataStore;

import static com.polymitasoft.caracola.components.RecyclerListActivity.Options.ADD_MENU;

/**
 * @author rainermf
 * @since 27/2/2017
 */

public class ServiceBySupplierListFragment extends RecyclerListFragment<SupplierService, ServiceBySupplierListFragment.OnListInteractionListener> {

    public static final String ARG_SUPPLIER_ID = "supplierId";
    private Supplier supplier;

    public static ServiceBySupplierListFragment newInstance(Supplier supplier) {

        Bundle args = new Bundle();
        args.putInt(ARG_SUPPLIER_ID, supplier.getId());

        ServiceBySupplierListFragment fragment = new ServiceBySupplierListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EntityDataStore<Persistable> dataStore = CaracolaApplication.instance().getDataStore();
        int idSupplier = getArguments().getInt(ARG_SUPPLIER_ID);
        supplier = dataStore.findByKey(Supplier.class, idSupplier);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected QueryRecyclerAdapter<SupplierService, ? extends RecyclerView.ViewHolder> createAdapter() {
        return new ServiceSupplierAdapter(getContext(), supplier);
    }

    @Override
    protected EnumSet<RecyclerListActivity.Options> removedDefaults() {
        return EnumSet.of(ADD_MENU);
    }

    public interface OnListInteractionListener {
        void onServiceListInteraction(SupplierService item);
    }
}
