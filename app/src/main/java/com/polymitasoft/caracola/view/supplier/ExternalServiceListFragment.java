package com.polymitasoft.caracola.view.supplier;

import android.content.Intent;
import android.os.Bundle;
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

    public static ExternalServiceListFragment newInstance() {

        Bundle args = new Bundle();

        ExternalServiceListFragment fragment = new ExternalServiceListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected QueryRecyclerAdapter<ExternalService, ? extends RecyclerView.ViewHolder> createAdapter() {
        return new ExternalServiceAdapter(getContext());
    }

    @Override
    protected void onActionAddMenu() {
        startActivity(new Intent(getContext(), ExternalServiceEditActivity.class));
    }
}
