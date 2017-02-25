package com.polymitasoft.caracola.components;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.dataaccess.DataStoreHolder;

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

    public RecyclerListAdapter(Context context) {
        this(context, null);
    }

    public RecyclerListAdapter(Context context, Type<E> type) {
        super(type);
        this.context = context;
        dataStore = DataStoreHolder.getInstance().getDataStore(context);
    }

    @Override
    public void onBindViewHolder(final E item, final VH holder, int position) {
        holder.itemView.setTag(item);
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.simple_list_item, parent, false);
        RecyclerView.ViewHolder holder = new SimpleViewHolder(view);
        return (VH) holder;
    }

    protected void deleteItem(E item) {
        dataStore.delete(item);
        queryAsync();
    }
}
