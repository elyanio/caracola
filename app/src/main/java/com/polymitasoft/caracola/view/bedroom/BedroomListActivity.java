package com.polymitasoft.caracola.view.bedroom;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.polymitasoft.caracola.components.RecyclerListActivity;
import com.polymitasoft.caracola.components.SimpleListAdapter;
import com.polymitasoft.caracola.components.SimpleViewHolder;
import com.polymitasoft.caracola.datamodel.Bedroom;

import io.requery.android.QueryRecyclerAdapter;
import io.requery.query.Result;

/**
 * @author rainermf
 * @since 16/2/2017
 */
public class BedroomListActivity extends RecyclerListActivity<Bedroom> {

    @Override
    protected QueryRecyclerAdapter<Bedroom, SimpleViewHolder> createAdapter() {
        return new BedroomAdapter(this);
    }

    @Override
    protected void onActionPlusMenu() {
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
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editItem(item);
                }
            });
            holder.editMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editItem(item);
                }
            });
        }

        private void editItem(Bedroom item) {
            Intent intent = new Intent(context, BedroomEditActivity.class);
            intent.putExtra(BedroomEditActivity.EXTRA_BEDROOM_ID, item.getId());
            context.startActivity(intent);
        }
    }
}
