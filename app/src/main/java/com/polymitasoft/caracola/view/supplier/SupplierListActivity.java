package com.polymitasoft.caracola.view.supplier;

import com.polymitasoft.caracola.components.RecyclerListActivity;
import com.polymitasoft.caracola.components.SimpleViewHolder;
import com.polymitasoft.caracola.datamodel.Supplier;

import io.requery.android.QueryRecyclerAdapter;

/**
 * Created by rainermf on 16/2/2017.
 */

public class SupplierListActivity extends RecyclerListActivity<Supplier> {

    @Override
    protected QueryRecyclerAdapter<Supplier, SimpleViewHolder> createAdapter() {
        return new SupplierAdapter(this);
    }

}
