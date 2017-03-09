package com.polymitasoft.caracola.components;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.amulyakhare.textdrawable.TextDrawable;
import com.polymitasoft.caracola.R;

import java.util.EnumSet;

import io.requery.Persistable;
import io.requery.meta.Type;

import static com.polymitasoft.caracola.components.SimpleListAdapter.Options.DELETE_CONFIRMATION;
import static com.polymitasoft.caracola.util.Metrics.dp;

/**
 * @author rainermf
 * @since 25/2/2017
 */

public abstract class SimpleListAdapter<E extends Persistable> extends RecyclerListAdapter<E, SimpleViewHolder> {

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
        setupIconView(item, holder, position);
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

    protected void setupIconView(E item, SimpleViewHolder holder, int position) {
        drawColorStrip(Colors.INSTANCE.getColor((int) holder.getItemId()), holder.colorStrip);
    }

    protected final void drawColorStrip(@ColorInt int color, ImageView colorStrip) {
        colorStrip.setBackgroundColor(color);
    }

    protected final void drawIconLetter(@ColorInt int color, char letter, ImageView colorStrip) {
        int pad = dp(5);
        colorStrip.setPadding(pad, pad, pad, pad);

        TextDrawable drawable = TextDrawable.builder().buildRound(String.valueOf(letter), color);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) colorStrip.getLayoutParams();
        int size = dp(70);
        params.width = size;
        params.height = size;
        colorStrip.setImageDrawable(drawable);
    }

    protected final void drawIconImage(@ColorInt int color, @DrawableRes int drawableRes, ImageView colorStrip) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        int pad = dp(13);
        int margin = dp(5);

        colorStrip.setPadding(pad, pad, pad, pad);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) colorStrip.getLayoutParams();
        int size = dp(60);
        params.width = size;
        params.height = size;
        params.setMargins(margin, margin, margin, margin);

        final Drawable icon = ContextCompat.getDrawable(context, drawableRes);
        DrawableCompat.setTint(icon, Color.WHITE);

        final ShapeDrawable background = new ShapeDrawable(new OvalShape());
        background.getPaint().setColor(color);

        colorStrip.setBackground(background);
        colorStrip.setImageDrawable(icon);
    }

    protected CharSequence getDeleteConfirmationMessage(E item) {
        return context.getString(R.string.delete_confirmation_message);
    }

    protected void handleDeletion(final E item) {
        if(getOptions().contains(DELETE_CONFIRMATION)) {
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

    protected enum Options {
        EDIT_ICON,
        DELETE_ICON,
        DELETE_CONFIRMATION
    }
}
