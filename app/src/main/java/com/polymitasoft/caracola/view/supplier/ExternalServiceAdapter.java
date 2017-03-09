package com.polymitasoft.caracola.view.supplier;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.Shape;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatDelegate;
import android.widget.RelativeLayout;

import com.amulyakhare.textdrawable.TextDrawable;
import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.components.Colors;
import com.polymitasoft.caracola.components.SimpleListAdapter;
import com.polymitasoft.caracola.components.SimpleViewHolder;
import com.polymitasoft.caracola.datamodel.ExternalService;
import com.polymitasoft.caracola.datamodel.Supplier;

import io.requery.query.Result;

import static com.polymitasoft.caracola.util.Metrics.dp;
import static java.lang.Character.toUpperCase;

/**
 * @author rainermf
 * @since 15/2/2017
 */
class ExternalServiceAdapter extends SimpleListAdapter<ExternalService> {

    ExternalServiceAdapter(Context context) {
        super(context, ExternalService.$TYPE);
    }

    @Override
    public Result<ExternalService> performQuery() {
        return dataStore.select(ExternalService.class).get();
    }

    @Override
    public void onBindViewHolder(final ExternalService item, SimpleViewHolder holder, int position) {
        super.onBindViewHolder(item, holder, position);
        holder.primaryText.setText(item.getName());
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
    protected void setupIconView(ExternalService item, SimpleViewHolder holder, int position) {
        int color = Colors.INSTANCE.getColor(item.getId());
        if(item.getId() % 3 == 0) {
            String name = item.getName();
            char letter = name.isEmpty() ? '\0' : toUpperCase(name.charAt(0));
            drawIconLetter(color, letter, holder.colorStrip);
        } else {
            drawIconImage(color, drawables[item.getId() % 6], holder.colorStrip);
        }
    }

    @Override
    protected void viewItem(ExternalService item) {
        Intent intent = new Intent(context, ExternalServiceViewActivity.class);
        intent.putExtra(SupplierListFragment.ARG_SERVICE_ID, item.getId());
        context.startActivity(intent);
    }

    protected void editItem(ExternalService service) {
        Intent intent = new Intent(context, ExternalServiceEditActivity.class);
        intent.putExtra(ExternalServiceEditActivity.EXTRA_SERVICE_ID, service.getId());
        context.startActivity(intent);
    }
}
