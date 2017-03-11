package com.polymitasoft.caracola.view.supplier;

import android.content.Context;
import android.content.DialogInterface;

import com.polymitasoft.caracola.components.Colors;
import com.polymitasoft.caracola.components.SimpleListAdapter;
import com.polymitasoft.caracola.components.SimpleViewHolder;
import com.polymitasoft.caracola.datamodel.Supplier;
import com.polymitasoft.caracola.datamodel.SupplierService;

import io.requery.query.Result;

import static com.polymitasoft.caracola.datamodel.SupplierService.SUPPLIER;
import static java.lang.Character.toUpperCase;

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
        holder.secondaryText.setText(item.getDescription());
    }

    @Override
    protected void setupIconView(SupplierService item, SimpleViewHolder holder, int position) {
        int color = Colors.INSTANCE.getColor(item.getService().getId());
        int icon = item.getService().getIcon();
        if (icon == 0) {
            String name = item.getService().getName();
            char letter = name.isEmpty() ? '\0' : toUpperCase(name.charAt(0));
            drawIconLetter(color, letter, holder.colorStrip);
        } else {
            drawIconImage(color, icon, holder.colorStrip);
        }
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
