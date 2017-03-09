package com.polymitasoft.caracola.view.supplier;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.RelativeLayout;

import com.amulyakhare.textdrawable.TextDrawable;
import com.polymitasoft.caracola.components.Colors;
import com.polymitasoft.caracola.components.SimpleListAdapter;
import com.polymitasoft.caracola.components.SimpleViewHolder;
import com.polymitasoft.caracola.dataaccess.SupplierDao;
import com.polymitasoft.caracola.datamodel.ExternalService;
import com.polymitasoft.caracola.datamodel.Supplier;
import com.polymitasoft.caracola.datamodel.SupplierService;

import java.util.Locale;

import io.requery.query.Result;

import static com.polymitasoft.caracola.datamodel.SupplierService.SERVICE;
import static com.polymitasoft.caracola.util.Metrics.dp;

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
    protected void setupColorStrip(SupplierService item, SimpleViewHolder holder, int position) {
        int pad = dp(5);
        holder.colorStrip.setPadding(pad, pad, pad, pad);

        int color = Colors.INSTANCE.getColor((int) holder.getItemId());
        String name = item.getSupplier().getName();
        String letter = name.isEmpty() ? "" : String.valueOf(name.charAt(0)).toUpperCase();
        TextDrawable drawable = TextDrawable.builder().buildRound(letter, color);
        holder.colorStrip.setLayoutParams(new RelativeLayout.LayoutParams(dp(70), dp(70)));
        holder.colorStrip.setImageDrawable(drawable);

    }

    @Override
    protected void viewItem(SupplierService item) {
        Intent intent = new Intent(context, SupplierViewActivity.class);
        intent.putExtra(SupplierViewActivity.EXTRA_SUPPLIER_ID, item.getSupplier().getId());
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
