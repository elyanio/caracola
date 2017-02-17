package com.polymitasoft.caracola.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.polymitasoft.caracola.DataStoreHolder;
import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.datamodel.InternalService;
import com.polymitasoft.caracola.view.service.ServiceListActivity;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.requery.Persistable;
import io.requery.android.QueryRecyclerAdapter;
import io.requery.query.Result;
import io.requery.sql.EntityDataStore;

/**
 * Created by rainermf on 16/2/2017.
 */

public abstract class ListActivity<T> extends AppCompatActivity {

    @BindView(R.id.bookingsRecyclerView) RecyclerView recyclerView;
    protected EntityDataStore<Persistable> data;
    private ExecutorService executor;
    private QueryRecyclerAdapter<T, Holder> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_bookings);
        ButterKnife.bind(this);

        data = DataStoreHolder.getInstance().getDataStore(this);
        executor = Executors.newSingleThreadExecutor();
        adapter = createAdapter();
        adapter.setExecutor(executor);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    protected abstract QueryRecyclerAdapter<T, Holder> createAdapter();

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

    public static class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.name) public TextView name;
        @BindView(R.id.picture) public ImageView image;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
