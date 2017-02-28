package com.polymitasoft.caracola.view.supplier;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.components.RecyclerListActivity;
import com.polymitasoft.caracola.components.SimpleViewHolder;
import com.polymitasoft.caracola.datamodel.ExternalService;

import io.requery.android.QueryRecyclerAdapter;

/**
 * @author rainermf
 * @since 16/2/2017
 */
public class ExternalServiceListActivity extends RecyclerListActivity<ExternalService> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBarTitle(R.string.title_external_services);
    }

    @Override
    protected QueryRecyclerAdapter<ExternalService, SimpleViewHolder> createAdapter() {
        return new ExternalServiceAdapter(this);
    }

    @Override
    protected void onActionPlusMenu() {
        Intent intent = new Intent(this, ExternalServiceEditActivity.class);
        startActivity(intent);
    }

}
