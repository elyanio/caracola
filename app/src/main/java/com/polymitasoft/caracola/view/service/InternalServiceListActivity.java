package com.polymitasoft.caracola.view.service;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.datamodel.InternalService;
import com.polymitasoft.caracola.datamodel.InternalServiceEntity;
import com.polymitasoft.caracola.view.ListActivity;

import java.util.Random;

import io.requery.android.QueryRecyclerAdapter;
import io.requery.query.Result;

/**
 * @author rainermf
 * @since 16/2/2017
 */
public class InternalServiceListActivity extends ListActivity<InternalService> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_internal_services);
        }
    }

    @Override
    protected QueryRecyclerAdapter<InternalService, SimpleViewHolder> createAdapter() {
        return new ServiceAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_plus:
                Intent intent = new Intent(this, InternalServiceEditActivity.class);
                startActivity(intent);
                return true;
        }
        return false;
    }

    /**
     * Created by rainermf on 15/2/2017.
     */
    class ServiceAdapter extends QueryRecyclerAdapter<InternalService, SimpleViewHolder> implements View.OnClickListener {

        private final Random random = new Random();
        private final int[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA};

        ServiceAdapter() {
//            super(InternalServiceEntity.$TYPE);
        }

        @Override
        public Result<InternalService> performQuery() {
            return data.select(InternalService.class).get();
        }

        @Override
        public void onBindViewHolder(InternalService item, SimpleViewHolder holder, int position) {
            holder.name.setText(item.getName());
            holder.image.setBackgroundColor(colors[random.nextInt(colors.length)]);
            holder.itemView.setTag(item);
        }

        @Override
        public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.booking_item, null);
            SimpleViewHolder holder = new SimpleViewHolder(view);
            view.setOnClickListener(this);
            return holder;
        }

        @Override
        public void onClick(View v) {
            InternalService service = (InternalService) v.getTag();
            if (service != null) {
                Intent intent = new Intent(InternalServiceListActivity.this, InternalServiceEditActivity.class);
                intent.putExtra(InternalServiceEditActivity.EXTRA_SERVICE_ID, service.getId());
                startActivity(intent);
            }
        }
    }
}
