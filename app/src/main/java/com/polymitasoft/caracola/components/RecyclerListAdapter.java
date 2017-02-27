package com.polymitasoft.caracola.components;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;

import com.polymitasoft.caracola.CaracolaApplication;
import com.polymitasoft.caracola.R;

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
    private boolean deleteConfirmationEnabled = true;

    public RecyclerListAdapter(Context context, Type<E> type) {
        super(type);
        this.context = context;
        dataStore = CaracolaApplication.instance().getDataStore();
    }

    @Override
    public void onBindViewHolder(final E item, final VH holder, int position) {
        holder.itemView.setTag(item);
    }

    public boolean isDeleteConfirmationEnabled() {
        return deleteConfirmationEnabled;
    }

    public void setDeleteConfirmationEnabled(boolean deleteConfirmationEnabled) {
        this.deleteConfirmationEnabled = deleteConfirmationEnabled;
    }

    protected CharSequence getDeleteConfirmationMessage(E item) {
        return context.getString(R.string.delete_confirmation_message);
    }

    protected void handleDeletion(final E item) {
        if(isDeleteConfirmationEnabled()) {
            new AlertDialog.Builder(context)
                    .setPositiveButton(R.string.yes_action_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteItem(item);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(R.string.cancel_action_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setMessage(getDeleteConfirmationMessage(item))
                    .show();
        }
    }

    protected void deleteItem(final E item) {
        dataStore.delete(item);
        queryAsync();
    }
}
