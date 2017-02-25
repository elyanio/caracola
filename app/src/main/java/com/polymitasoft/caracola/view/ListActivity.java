package com.polymitasoft.caracola.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.R;

import org.w3c.dom.Text;

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
public abstract class ListActivity<T> extends AppCompatActivity {

    @BindView(R.id.bookingsRecyclerView) RecyclerView recyclerView;
    protected EntityDataStore<Persistable> data;
    private ExecutorService executor;
    private QueryRecyclerAdapter<T, ? extends RecyclerView.ViewHolder> adapter;

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

    protected abstract QueryRecyclerAdapter<T, ? extends RecyclerView.ViewHolder> createAdapter();

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

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.primary_text) public TextView primaryText;
        @BindView(R.id.secondary_text) public TextView secondaryText;
        @BindView(R.id.tertiary_text) public TextView tertiaryText;
        @BindView(R.id.color_strip) public View colorStrip;
        @BindView(R.id.edit_menu) public ImageView editMenu;
        @BindView(R.id.delete_menu) public ImageView deleteMenu;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
