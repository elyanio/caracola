package com.polymitasoft.caracola.view.supplier;

import android.content.Context;
import android.content.DialogInterface;

import com.polymitasoft.caracola.components.SimpleListAdapter;
import com.polymitasoft.caracola.components.SimpleViewHolder;
import com.polymitasoft.caracola.dataaccess.SupplierDao;
import com.polymitasoft.caracola.datamodel.ExternalService;
import com.polymitasoft.caracola.datamodel.Supplier;
import com.polymitasoft.caracola.datamodel.SupplierService;

import java.util.Locale;

import io.requery.query.Result;

import static com.polymitasoft.caracola.datamodel.SupplierService.SERVICE;
import static com.polymitasoft.caracola.datamodel.SupplierService.SUPPLIER;

/**
 * @author rainermf
 * @since 27/2/2017
 */
class ServiceSupplierAdapter extends SimpleListAdapter<SupplierService> {

    private Supplier supplier;

    ServiceSupplierAdapter(Context context, Supplier supplier) {
        super(context, SupplierService.$TYPE);
        this.supplier = supplier;
    }

    @Override
    public Result<SupplierService> performQuery() {
        return dataStore.select(SupplierService.class).where(SUPPLIER.eq(supplier)).get();
    }

    @Override
    public void onBindViewHolder(SupplierService item, SimpleViewHolder holder, int position) {
        super.onBindViewHolder(item, holder, position);
        holder.primaryText.setText(item.getService().getName());
        holder.secondaryText.setText(String.format(Locale.getDefault(), "$%.2f (%.2f)", item.getPrice(), item.getComission()));
    }

    @Override
    protected void viewItem(SupplierService item) {
        editItem(item);
    }

    @Override
    protected void editItem(SupplierService item) {
        new SupplierServiceEditDialog(context, item)
                .setOkListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        queryAsync();
                    }
                })
                .show();
    }
}
