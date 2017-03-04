package com.polymitasoft.caracola.components;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.google.common.collect.Sets;
import com.polymitasoft.caracola.CaracolaApplication;
import com.polymitasoft.caracola.R;

import java.util.EnumSet;
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

    @BindView(R.id.listRecyclerView) RecyclerView recyclerView;
    protected EntityDataStore<Persistable> data;
    private ExecutorService executor;
    private QueryRecyclerAdapter<T, ? extends RecyclerView.ViewHolder> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_items);
        ButterKnife.bind(this);

        data = CaracolaApplication.instance().getDataStore();
        executor = Executors.newSingleThreadExecutor();
        adapter = createAdapter();
        adapter.setExecutor(executor);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    protected EnumSet<Options> getOptions() {
        return EnumSet.noneOf(Options.class);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!getOptions().contains(Options.NO_ADD_MENU)) {
            getMenuInflater().inflate(R.menu.list_add_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_plus:
                onActionPlusMenu();
                return true;
        }
        return false;
    }

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

    public enum Options {
        NO_ADD_MENU
    }

}
