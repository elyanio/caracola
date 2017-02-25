package com.polymitasoft.caracola.view.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.components.Colors;
import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.datamodel.InternalService;
import com.polymitasoft.caracola.util.FormatUtils;
import com.polymitasoft.caracola.view.ListActivity;

import io.requery.Persistable;
import io.requery.android.QueryRecyclerAdapter;
import io.requery.query.Result;
import io.requery.sql.EntityDataStore;

/**
 * @author rainermf
 * @since 16/2/2017
 */
public class InternalServiceListActivity extends ListActivity<InternalService> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_internal_services);
        }
    }

    @Override
    protected QueryRecyclerAdapter<InternalService, SimpleViewHolder> createAdapter() {
        return new ServiceAdapter(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_plus:
                Intent intent = new Intent(this, InternalServiceEditActivity.class);
                startActivity(intent);
                return true;
        }
        return false;
    }

    /**
     * Created by rainermf on 15/2/2017.
     */
    static class ServiceAdapter extends QueryRecyclerAdapter<InternalService, SimpleViewHolder> implements View.OnClickListener {

        private EntityDataStore<Persistable> dataStore;
        private Context context;

        ServiceAdapter(Context context) {
            super(InternalService.$TYPE);
            this.context = context;
            dataStore = DataStoreHolder.getInstance().getDataStore(context);
        }

        @Override
        public Result<InternalService> performQuery() {
            return dataStore.select(InternalService.class).get();
        }

        @Override
        public void onBindViewHolder(final InternalService item, final SimpleViewHolder holder, int position) {
            holder.primaryText.setText(item.getName());
            holder.secondaryText.setText(FormatUtils.formatMoneyWithCurrency(item.getDefaultPrice()));
            holder.colorStrip.setBackgroundColor(Colors.INSTANCE.getColor(item.getId()));

            holder.editMenu.setOnClickListener(this);
            holder.deleteMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteService(item);
                }
            });
            holder.itemView.setTag(item);
        }

        @Override
        public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.simple_list_item, parent, false);
            SimpleViewHolder holder = new SimpleViewHolder(view);
            view.setOnClickListener(this);
            return holder;
        }

        @Override
        public void onClick(View v) {
            InternalService service = (InternalService) v.getTag();
            if (service != null) {
                Intent intent = new Intent(context, InternalServiceEditActivity.class);
                intent.putExtra(InternalServiceEditActivity.EXTRA_SERVICE_ID, service.getId());
                context.startActivity(intent);
            }
        }

        public void deleteService(InternalService service) {
            dataStore.delete(service);
            queryAsync();
        }
    }
}
