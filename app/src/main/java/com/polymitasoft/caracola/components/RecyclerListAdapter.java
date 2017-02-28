package com.polymitasoft.caracola.components;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.polymitasoft.caracola.CaracolaApplication;

import io.requery.Persistable;
import io.requery.android.QueryRecyclerAdapter;
import io.requery.meta.Type;
import io.requery.sql.EntityDataStore;

/**
 * @author rainermf
 * @since 25/2/2017
 */

public abstract class RecyclerListAdapter<E extends Persistable, VH extends RecyclerView.ViewHolder> extends QueryRecyclerAdapter<E, VH> {

    protected EntityDataStore<Persistable> dataStore;
    protected Context context;

    public RecyclerListAdapter(Context context, Type<E> type) {
        super(type);
        this.context = context;
        dataStore = CaracolaApplication.instance().getDataStore();
    }

    @Override
    public void onBindViewHolder(final E item, final VH holder, int position) {
        holder.itemView.setTag(item);
    }
}
