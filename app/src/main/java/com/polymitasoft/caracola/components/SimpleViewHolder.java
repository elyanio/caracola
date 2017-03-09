package com.polymitasoft.caracola.components;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.polymitasoft.caracola.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author rainermf
 * @since 25/2/2017
 */
public class SimpleViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.primary_text) public TextView primaryText;
    @BindView(R.id.secondary_text) public TextView secondaryText;
    @BindView(R.id.tertiary_text) public TextView tertiaryText;
    @BindView(R.id.color_strip) public ImageView colorStrip;
    @BindView(R.id.edit_menu) public ImageView editMenu;
    @BindView(R.id.delete_menu) public ImageView deleteMenu;

    public SimpleViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
