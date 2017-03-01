package com.polymitasoft.caracola.view.supplier;

import android.content.Context;
import android.content.Intent;

import com.polymitasoft.caracola.components.SimpleListAdapter;
import com.polymitasoft.caracola.components.SimpleViewHolder;
import com.polymitasoft.caracola.dataaccess.SupplierDao;
import com.polymitasoft.caracola.datamodel.ExternalService;
import com.polymitasoft.caracola.datamodel.Supplier;

import io.requery.query.Result;

/**
 * @author rainermf
 * @since 27/2/2017
 */
class SupplierAdapter extends SimpleListAdapter<Supplier> {

    private ExternalService service;
    private SupplierDao dao;

    SupplierAdapter(Context context) {
        this(context, null);
    }

    SupplierAdapter(Context context, ExternalService service) {
        super(context, Supplier.$TYPE);
        this.service = service;
        dao = new SupplierDao();
    }

    @Override
    public Result<Supplier> performQuery() {
        if(service == null) {
            return dataStore.select(Supplier.class).orderBy(Supplier.NAME.lower()).get();
        } else {
            return dao.withService(service);
        }
    }

    @Override
    public void onBindViewHolder(Supplier item, SimpleViewHolder holder, int position) {
        super.onBindViewHolder(item, holder, position);
        holder.primaryText.setText(item.getName());
    }

    @Override
    protected void viewItem(Supplier item) {
        editItem(item);
    }

    @Override
    protected void editItem(Supplier item) {
        Intent intent = new Intent(context, SupplierEditActivity.class);
        intent.putExtra(SupplierEditActivity.EXTRA_SUPPLIER_ID, item.getId());
        context.startActivity(intent);
    }
}
