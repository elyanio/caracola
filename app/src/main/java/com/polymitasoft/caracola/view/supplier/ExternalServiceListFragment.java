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

public class ExternalServiceListFragment extends RecyclerListFragment<ExternalService, ExternalServiceListFragment.OnListInteractionListener> {

    public static ExternalServiceListFragment newInstance() {

        Bundle args = new Bundle();

        ExternalServiceListFragment fragment = new ExternalServiceListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ExternalServiceListFragment() {
    }

    @Override
    protected QueryRecyclerAdapter<ExternalService, ? extends RecyclerView.ViewHolder> createAdapter() {
        return new ExternalServiceAdapter(getContext());
    }

    @Override
    protected void onActionPlusMenu() {
        startActivity(new Intent(getContext(), ExternalServiceEditActivity.class));
    }

    public interface OnListInteractionListener {
        void onServiceListInteraction(ExternalService item);
    }
}
