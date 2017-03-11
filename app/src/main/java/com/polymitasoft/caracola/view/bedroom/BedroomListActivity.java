package com.polymitasoft.caracola.view.bedroom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.components.RecyclerListActivity;
import com.polymitasoft.caracola.components.SimpleListAdapter;
import com.polymitasoft.caracola.components.SimpleViewHolder;
import com.polymitasoft.caracola.datamodel.Bedroom;

import java.util.EnumSet;

import io.requery.android.QueryRecyclerAdapter;
import io.requery.query.Result;

/**
 * @author rainermf
 * @since 16/2/2017
 */
public class BedroomListActivity extends RecyclerListActivity<Bedroom> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.title_bedrooms);
    }

    @Override
    protected QueryRecyclerAdapter<Bedroom, SimpleViewHolder> createAdapter() {
        return new BedroomAdapter(this);
    }

    @Override
    protected void onActionAddMenu() {
        Intent intent = new Intent(this, BedroomEditActivity.class);
        startActivity(intent);
    }

    static class BedroomAdapter extends SimpleListAdapter<Bedroom> {

        BedroomAdapter(Context context) {
            super(context, Bedroom.$TYPE);
        }

        @Override
        public Result<Bedroom> performQuery() {
            return dataStore.select(Bedroom.class).get();
        }

        @Override
        public void onBindViewHolder(final Bedroom item, SimpleViewHolder holder, int position) {
            super.onBindViewHolder(item, holder, position);
            holder.primaryText.setText(item.getName());
        }

        protected void viewItem(Bedroom item) {
            Intent intent = new Intent(context, BedroomEditActivity.class);
            intent.putExtra(BedroomEditActivity.EXTRA_BEDROOM_ID, item.getId());
            context.startActivity(intent);
        }

        @Override
        protected boolean shouldShowEditIcon() {
            return false;
        }
    }
}
