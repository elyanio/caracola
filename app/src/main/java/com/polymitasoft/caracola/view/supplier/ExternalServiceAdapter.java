package com.polymitasoft.caracola.view.supplier;

import android.content.Context;
import android.content.Intent;

import com.polymitasoft.caracola.components.SimpleListAdapter;
import com.polymitasoft.caracola.components.SimpleViewHolder;
import com.polymitasoft.caracola.datamodel.ExternalService;

import io.requery.query.Result;

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
