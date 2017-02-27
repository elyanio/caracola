package com.polymitasoft.caracola.components;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.polymitasoft.caracola.R;

import io.requery.Persistable;
import io.requery.meta.Type;

/**
 * @author rainermf
 * @since 25/2/2017
 */

public abstract class SimpleListAdapter<E extends Persistable> extends RecyclerListAdapter<E, SimpleViewHolder> {

    public SimpleListAdapter(Context context, Type<E> type) {
        super(context, type);
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.simple_list_item, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final E item, final SimpleViewHolder holder, int position) {
        super.onBindViewHolder(item, holder, position);
        holder.colorStrip.setBackgroundColor(Colors.INSTANCE.getColor((int) holder.getItemId()));
        holder.deleteMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDeletion(item);
            }
        });
    }
}
