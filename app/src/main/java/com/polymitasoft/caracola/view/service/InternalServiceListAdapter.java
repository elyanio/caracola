package com.polymitasoft.caracola.view.service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.datamodel.InternalService;
import com.polymitasoft.caracola.util.FormatUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.requery.Persistable;
import io.requery.android.QueryAdapter;
import io.requery.query.Result;
import io.requery.sql.EntityDataStore;

import static com.polymitasoft.caracola.datamodel.InternalService.NAME;

/**
 * @author rainermf
 * @since 22/2/2017
 */

public class InternalServiceListAdapter extends QueryAdapter<InternalService> {

    EntityDataStore<Persistable> dataStore;

    public InternalServiceListAdapter(Context context) {
        super(InternalService.$TYPE);
        dataStore = DataStoreHolder.getInstance().getDataStore(context);
    }

    @Override
    public Result<InternalService> performQuery() {
        return dataStore.select(InternalService.class).orderBy(NAME).get();
    }

    @Override
    public View getView(InternalService item, View convertView, ViewGroup parent) {
        View view;
        if(convertView != null) {
            view = convertView;
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cardview_test, parent, false);
        }
        view.setTag(item);
        TextView primaryText = ButterKnife.findById(view, R.id.primary_text);
        primaryText.setText(item.getName());
        TextView secondaryText = ButterKnife.findById(view, R.id.secondary_text);
        secondaryText.setText(FormatUtils.formatMoney(item.getDefaultPrice()));

        return view;
    }
}
