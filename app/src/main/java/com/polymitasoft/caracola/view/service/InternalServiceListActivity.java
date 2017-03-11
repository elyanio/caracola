package com.polymitasoft.caracola.view.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.components.RecyclerListActivity;
import com.polymitasoft.caracola.components.SimpleListAdapter;
import com.polymitasoft.caracola.components.SimpleViewHolder;
import com.polymitasoft.caracola.datamodel.InternalService;
import com.polymitasoft.caracola.util.FormatUtils;

import io.requery.android.QueryRecyclerAdapter;
import io.requery.query.Result;

import static com.polymitasoft.caracola.datamodel.InternalService.HIDDEN;
import static com.polymitasoft.caracola.datamodel.InternalService.NAME;

/**
 * @author rainermf
 * @since 16/2/2017
 */
public class InternalServiceListActivity extends RecyclerListActivity<InternalService> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBarTitle(R.string.title_internal_services);
    }

    @Override
    protected QueryRecyclerAdapter<InternalService, SimpleViewHolder> createAdapter() {
        return new ServiceAdapter(this);
    }

    @Override
    protected void onActionAddMenu() {
        Intent intent = new Intent(this, InternalServiceEditActivity.class);
        startActivity(intent);
    }

    /**
     * Created by rainermf on 15/2/2017.
     */
    static class ServiceAdapter extends SimpleListAdapter<InternalService> {

        ServiceAdapter(Context context) {
            super(context, InternalService.$TYPE);
        }

        @Override
        public Result<InternalService> performQuery() {
            return dataStore.select(InternalService.class)
                    .where(HIDDEN.eq(false))
                    .orderBy(NAME.lower())
                    .get();
        }

        @Override
        public void onBindViewHolder(final InternalService item, final SimpleViewHolder holder, int position) {
            super.onBindViewHolder(item, holder, position);
            holder.primaryText.setText(item.getName());
            holder.secondaryText.setText(FormatUtils.formatMoneyWithCurrency(item.getDefaultPrice()));
        }

        @Override
        protected boolean shouldShowEditIcon() {
            return false;
        }

        @Override
        protected void viewItem(InternalService item) {
            Intent intent = new Intent(context, InternalServiceEditActivity.class);
            intent.putExtra(InternalServiceEditActivity.EXTRA_SERVICE_ID, item.getId());
            context.startActivity(intent);
        }

        @Override
        protected void deleteItem(InternalService item) {
            try {
                dataStore.delete(item);
            } catch (Exception e) {
                item.setHidden(true);
                dataStore.update(item);
            }
            queryAsync();
        }
    }
}
