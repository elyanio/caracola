package com.polymitasoft.caracola.view.supplier;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatDelegate;
import android.widget.RelativeLayout;

import com.polymitasoft.caracola.R;
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
import static com.polymitasoft.caracola.datamodel.SupplierService.SUPPLIER;
import static com.polymitasoft.caracola.util.Metrics.dp;
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
        holder.secondaryText.setText(String.format(Locale.getDefault(), "$%.2f (%.2f)", item.getPrice(), item.getComission()));
    }

    private static int[] drawables = new int[] {
            R.drawable.ic_florist,
            R.drawable.ic_gym,
            R.drawable.ic_scuba_diving,
            R.drawable.ic_taxi_stand,
            R.drawable.ic_sync_black_24dp,
            R.drawable.ic_beauty_salon
    };

    @Override
    protected void setupIconView(SupplierService item, SimpleViewHolder holder, int position) {
        int color = Colors.INSTANCE.getColor(item.getService().getId());
        if(item.getId() % 3 == 0) {
            String name = item.getService().getName();
            char letter = name.isEmpty() ? '\0' : toUpperCase(name.charAt(0));
            drawIconLetter(color, letter, holder.colorStrip);
        } else {
            drawIconImage(color, drawables[item.getService().getId() % 6], holder.colorStrip);
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
