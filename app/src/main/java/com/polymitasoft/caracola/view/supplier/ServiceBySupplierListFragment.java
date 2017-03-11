package com.polymitasoft.caracola.view.supplier;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.polymitasoft.caracola.components.RecyclerListFragment;
import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.dataaccess.SupplierDao;
import com.polymitasoft.caracola.datamodel.ExternalService;
import com.polymitasoft.caracola.datamodel.Supplier;
import com.polymitasoft.caracola.datamodel.SupplierService;

import java.util.List;

import io.requery.Persistable;
import io.requery.android.QueryRecyclerAdapter;
import io.requery.sql.EntityDataStore;

/**
 * @author rainermf
 * @since 27/2/2017
 */

public class ServiceBySupplierListFragment extends RecyclerListFragment<SupplierService> {

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
        EntityDataStore<Persistable> dataStore = DataStoreHolder.INSTANCE.getDataStore();
        int idSupplier = getArguments().getInt(ARG_SUPPLIER_ID);
        supplier = dataStore.findByKey(Supplier.class, idSupplier);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected QueryRecyclerAdapter<SupplierService, ? extends RecyclerView.ViewHolder> createAdapter() {
        return new ServiceSupplierAdapter(getContext(), supplier);
    }

    @Override
    protected void onActionAddMenu() {
        SupplierDao dao = new SupplierDao();
        final List<ExternalService> services = dao.notRenderedServices(supplier).toList();
        int size = services.size();

        if (size == 0) {
            new AlertDialog.Builder(getContext())
                    .setMessage("Todos los servicios existentes ya han sido vinculados a este proveedor.")
                    .show();
            return;
        }

        CharSequence[] servicesLabels = new CharSequence[size];
        for (int i = 0; i < size; i++) {
            servicesLabels[i] = services.get(i).getName();
        }
        new AlertDialog.Builder(getContext())
                .setItems(servicesLabels, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        addSupplierService(services.get(which));
                    }
                })
                .show();
    }

    private void addSupplierService(ExternalService service) {
        SupplierService item = new SupplierService()
                .setService(service)
                .setSupplier(supplier)
                .setDescription("");
        new SupplierServiceEditDialog(getContext(), item)
                .setOkListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getAdapter().queryAsync();
                    }
                })
                .show();
    }
}
