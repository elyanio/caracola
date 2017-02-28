package com.polymitasoft.caracola.view.supplier;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.components.RecyclerListActivity;
import com.polymitasoft.caracola.components.SimpleListAdapter;
import com.polymitasoft.caracola.components.SimpleViewHolder;
import com.polymitasoft.caracola.datamodel.ExternalService;

import io.requery.android.QueryRecyclerAdapter;
import io.requery.query.Result;

/**
 * @author rainermf
 * @since 16/2/2017
 */
public class ExternalServiceListActivity extends RecyclerListActivity<ExternalService> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBarTitle(R.string.title_external_services);
    }

    @Override
    protected QueryRecyclerAdapter<ExternalService, SimpleViewHolder> createAdapter() {
        return new ServiceAdapter(this);
    }

    @Override
    protected void onActionPlusMenu() {
        Intent intent = new Intent(this, ExternalServiceEditActivity.class);
        startActivity(intent);
    }

    /**
     * Created by rainermf on 15/2/2017.
     */
    static class ServiceAdapter extends SimpleListAdapter<ExternalService> {

        ServiceAdapter(Context context) {
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
            editItem(item);
        }

        protected void editItem(ExternalService service) {
            Intent intent = new Intent(context, ExternalServiceEditActivity.class);
            intent.putExtra(ExternalServiceEditActivity.EXTRA_SERVICE_ID, service.getId());
            context.startActivity(intent);
        }
    }
}
