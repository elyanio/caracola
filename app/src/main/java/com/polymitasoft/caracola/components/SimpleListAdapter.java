package com.polymitasoft.caracola.components;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.polymitasoft.caracola.R;

import java.util.EnumSet;

import io.requery.Persistable;
import io.requery.meta.Type;

/**
 * @author rainermf
 * @since 25/2/2017
 */

public abstract class SimpleListAdapter<E extends Persistable> extends RecyclerListAdapter<E, SimpleViewHolder> {

    private boolean deleteConfirmationEnabled = true;
    private EnumSet<Options> options;

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
        if(getOptions().contains(Options.DELETE_ICON)) {
            holder.deleteMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleDeletion(item);
                }
            });
        } else {
            holder.deleteMenu.setVisibility(View.GONE);
        }
        if(getOptions().contains(Options.EDIT_ICON)) {
            holder.editMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editItem(item);
                }
            });
        } else {
            holder.editMenu.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewItem(item);
            }
        });
    }

    public boolean isDeleteConfirmationEnabled() {
        return deleteConfirmationEnabled;
    }

    public final void setDeleteConfirmationEnabled(boolean deleteConfirmationEnabled) {
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

    protected void editItem(final E item) {

    }

    protected void viewItem(final E item) {

    }

    protected EnumSet<Options> removedDefaults() {
        return EnumSet.noneOf(Options.class);
    }

    private EnumSet<Options> getOptions() {
        if(options == null) {
            options = EnumSet.complementOf(removedDefaults());
        }
        return options;
    }

    public enum Options {
        EDIT_ICON,
        DELETE_ICON
    }
}
