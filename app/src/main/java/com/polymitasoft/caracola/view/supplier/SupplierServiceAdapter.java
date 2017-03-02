package com.polymitasoft.caracola.view.supplier;

import android.content.Context;

import com.polymitasoft.caracola.components.SimpleListAdapter;
import com.polymitasoft.caracola.components.SimpleViewHolder;
import com.polymitasoft.caracola.dataaccess.SupplierDao;
import com.polymitasoft.caracola.datamodel.ExternalService;
import com.polymitasoft.caracola.datamodel.Supplier;
import com.polymitasoft.caracola.datamodel.SupplierService;

import java.util.Locale;

import io.requery.query.Result;

import static com.polymitasoft.caracola.datamodel.SupplierService.SERVICE;

/**
 * @author rainermf
 * @since 27/2/2017
 */
class SupplierServiceAdapter extends SimpleListAdapter<SupplierService> {

    private ExternalService service;
    private SupplierDao dao;

    SupplierServiceAdapter(Context context, ExternalService service) {
        super(context, SupplierService.$TYPE);
        this.service = service;
        dao = new SupplierDao();
    }

    @Override
    public Result<SupplierService> performQuery() {
        return dataStore.select(SupplierService.class).where(SERVICE.eq(service)).get();
    }

    @Override
    public void onBindViewHolder(SupplierService item, SimpleViewHolder holder, int position) {
        super.onBindViewHolder(item, holder, position);
        Supplier supplier = item.getSupplier();
        holder.primaryText.setText(supplier.getName());
        holder.secondaryText.setText(String.format(Locale.getDefault(), "$%.2f (%.2f)", item.getPrice(), item.getComission()));
    }

    @Override
    protected void viewItem(SupplierService item) {
        editItem(item);
    }

    @Override
    protected void editItem(SupplierService item) {
//        Intent intent = new Intent(context, SupplierServiceEditActivity.class);
//        intent.putExtra(SupplierServiceEditActivity.EXTRA_SUPPLIER_SERVICE_ID, item.getId());
//        context.startActivity(intent);
    }
}
