package com.polymitasoft.caracola.view.supplier;

import android.support.v7.widget.RecyclerView;

import com.polymitasoft.caracola.components.RecyclerListFragment;
import com.polymitasoft.caracola.datamodel.ExternalService;

import io.requery.android.QueryRecyclerAdapter;

/**
 * @author rainermf
 * @since 27/2/2017
 */

public class ExternalServiceListFragment extends RecyclerListFragment<ExternalService> {

    public ExternalServiceListFragment() {
    }

    @Override
    protected QueryRecyclerAdapter<ExternalService, ? extends RecyclerView.ViewHolder> createAdapter() {
        return new ExternalServiceAdapter(getContext());
    }
}
