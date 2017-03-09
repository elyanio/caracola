package com.polymitasoft.caracola.view.supplier;

import android.content.Context;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.common.base.Strings;
import com.google.common.primitives.Chars;
import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.components.Colors;
import com.polymitasoft.caracola.components.SimpleListAdapter;
import com.polymitasoft.caracola.components.SimpleViewHolder;
import com.polymitasoft.caracola.dataaccess.SupplierDao;
import com.polymitasoft.caracola.datamodel.ExternalService;
import com.polymitasoft.caracola.datamodel.Supplier;
import com.polymitasoft.caracola.util.Metrics;

import io.requery.query.Result;

import static com.polymitasoft.caracola.util.Metrics.dp;

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
    protected void setupColorStrip(Supplier item, SimpleViewHolder holder, int position) {
        int pad = dp(5);
        holder.colorStrip.setPadding(pad, pad, pad, pad);

        int color = Colors.INSTANCE.getColor((int) holder.getItemId());
        String name = item.getName();
        String letter = name.isEmpty() ? "" : String.valueOf(name.charAt(0)).toUpperCase();
        TextDrawable drawable = TextDrawable.builder().buildRound(letter, color);
        holder.colorStrip.setLayoutParams(new RelativeLayout.LayoutParams(dp(70), dp(70)));
        holder.colorStrip.setImageDrawable(drawable);

    }

    @Override
    protected void viewItem(Supplier item) {
        Intent intent = new Intent(context, SupplierViewActivity.class);
        intent.putExtra(SupplierViewActivity.EXTRA_SUPPLIER_ID, item.getId());
        context.startActivity(intent);
    }

    @Override
    protected void editItem(Supplier item) {
        Intent intent = new Intent(context, SupplierEditActivity.class);
        intent.putExtra(SupplierEditActivity.EXTRA_SUPPLIER_ID, item.getId());
        context.startActivity(intent);
    }
}
