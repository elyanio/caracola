package com.polymitasoft.caracola.components;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.dataaccess.DataStoreHolder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.requery.Persistable;
import io.requery.android.QueryRecyclerAdapter;
import io.requery.sql.EntityDataStore;

/**
 * @author rainermf
 * @since 16/2/2017
 */
public abstract class RecyclerListActivity<T> extends AppCompatActivity {

    protected EntityDataStore<Persistable> data;
    @BindView(R.id.listRecyclerView) RecyclerView recyclerView;
    @BindView(R.id.fab) FloatingActionButton fab;
    private ExecutorService executor;
    private QueryRecyclerAdapter<T, ? extends RecyclerView.ViewHolder> adapter;
    private boolean addMenuVisible = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_items);
        ButterKnife.bind(this);

        if (isAddMenuVisible()) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onActionPlusMenu();
                }
            });
        } else {
            fab.setVisibility(View.GONE);
        }
        data = DataStoreHolder.INSTANCE.getDataStore();
        executor = Executors.newSingleThreadExecutor();
        adapter = createAdapter();
        adapter.setExecutor(executor);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    protected QueryRecyclerAdapter<T, ? extends RecyclerView.ViewHolder> getAdapter() {
        return adapter;
    }

    protected final void setBarTitle(@StringRes int title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    protected final void setBarTitle(CharSequence title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    protected abstract QueryRecyclerAdapter<T, ? extends RecyclerView.ViewHolder> createAdapter();

    protected void onActionPlusMenu() {
    }

    @Override
    protected void onResume() {
        adapter.queryAsync();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        executor.shutdown();
        adapter.close();
        super.onDestroy();
    }

    public boolean isAddMenuVisible() {
        return addMenuVisible;
    }
}
