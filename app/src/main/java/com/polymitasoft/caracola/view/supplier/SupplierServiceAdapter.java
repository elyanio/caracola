package com.polymitasoft.caracola.view.supplier;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.polymitasoft.caracola.components.Colors;
import com.polymitasoft.caracola.components.SimpleListAdapter;
import com.polymitasoft.caracola.components.SimpleViewHolder;
import com.polymitasoft.caracola.datamodel.ExternalService;
import com.polymitasoft.caracola.datamodel.Supplier;
import com.polymitasoft.caracola.datamodel.SupplierService;

import io.requery.query.Result;

import static com.polymitasoft.caracola.datamodel.SupplierService.SERVICE;
import static com.polymitasoft.caracola.datamodel.SupplierService.SUPPLIER_ID;
import static java.lang.Character.toUpperCase;

/**
 * @author rainermf
 * @since 27/2/2017
 */
class SupplierServiceAdapter extends SimpleListAdapter<SupplierService> {

    private ExternalService service;

    SupplierServiceAdapter(Context context, ExternalService service) {
        super(context, SupplierService.$TYPE);
        this.service = service;
    }

    @Override
    public Result<SupplierService> performQuery() {
        return dataStore.select(SupplierService.class)
                .join(Supplier.class).on(SUPPLIER_ID.equal(Supplier.ID))
                .where(SERVICE.eq(service))
                .orderBy(Supplier.NAME.lower()).get();
    }

    @Override
    public void onBindViewHolder(SupplierService item, SimpleViewHolder holder, int position) {
        super.onBindViewHolder(item, holder, position);
        Supplier supplier = item.getSupplier();
        holder.primaryText.setText(supplier.getName());
        holder.secondaryText.setText(item.getDescription());
    }

    @Override
    protected void setupIconView(SupplierService item, SimpleViewHolder holder, int position) {
        int color = Colors.INSTANCE.getColor(item.getSupplier().getId());
        String name = item.getSupplier().getName();
        char letter = name.isEmpty() ? '\0' : toUpperCase(name.charAt(0));
        drawIconLetter(color, letter, holder.colorStrip);
    }

    @Override
    protected void viewItem(SupplierService item) {
        Intent intent = new Intent(context, SupplierServiceInfoActivity.class);
        intent.putExtra(SupplierServiceInfoActivity.ARG_SUPPLIER_SERVICE_ID, item.getId());
        context.startActivity(intent);
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
