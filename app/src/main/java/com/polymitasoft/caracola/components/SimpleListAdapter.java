package com.polymitasoft.caracola.components;

import android.content.Context;
import android.view.View;

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
