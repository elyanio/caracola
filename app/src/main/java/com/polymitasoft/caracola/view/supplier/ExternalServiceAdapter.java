package com.polymitasoft.caracola.view.supplier;

import android.content.Context;
import android.content.Intent;

import com.google.common.base.Strings;
import com.polymitasoft.caracola.components.Colors;
import com.polymitasoft.caracola.components.SimpleListAdapter;
import com.polymitasoft.caracola.components.SimpleViewHolder;
import com.polymitasoft.caracola.datamodel.ExternalService;

import io.requery.query.Result;

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

    @Override
    protected void setupIconView(ExternalService item, SimpleViewHolder holder, int position) {
        int color = Colors.INSTANCE.getColor(item.getId());
        if (Strings.isNullOrEmpty(item.getIcon())) {
            String name = item.getName();
            char letter = name.isEmpty() ? '\0' : toUpperCase(name.charAt(0));
            drawIconLetter(color, letter, holder.colorStrip);
        } else {
            drawIconImage(color, item.getIcon(), holder.colorStrip);
        }
    }

    @Override
    protected void viewItem(ExternalService item) {
        Intent intent = new Intent(context, ExternalServiceInfoActivity.class);
        intent.putExtra(SupplierListFragment.ARG_SERVICE_ID, item.getId());
        context.startActivity(intent);
    }

    protected void editItem(ExternalService service) {
        Intent intent = new Intent(context, ExternalServiceEditActivity.class);
        intent.putExtra(ExternalServiceEditActivity.EXTRA_SERVICE_ID, service.getId());
        context.startActivity(intent);
    }
}
