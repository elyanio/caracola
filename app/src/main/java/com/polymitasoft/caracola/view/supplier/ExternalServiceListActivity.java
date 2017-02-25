package com.polymitasoft.caracola.view.supplier;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.datamodel.IExternalService;
import com.polymitasoft.caracola.view.ListActivity;

import java.util.Random;

import io.requery.android.QueryRecyclerAdapter;
import io.requery.query.Result;

/**
 * @author rainermf
 * @since 16/2/2017
 */
public class ExternalServiceListActivity extends ListActivity<IExternalService> {

    @Override
    protected QueryRecyclerAdapter<IExternalService, SimpleViewHolder> createAdapter() {
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
                Intent intent = new Intent(this, ExternalServiceEditActivity.class);
                startActivity(intent);
                return true;
        }
        return false;
    }

    /**
     * Created by rainermf on 15/2/2017.
     */
    class ServiceAdapter extends QueryRecyclerAdapter<IExternalService, SimpleViewHolder> implements View.OnClickListener {

        private final Random random = new Random();
        private final int[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA};

        ServiceAdapter() {
//            super(ExternalServiceEntity.$TYPE);
        }

        @Override
        public Result<IExternalService> performQuery() {
            return data.select(IExternalService.class).get();
        }

        @Override
        public void onBindViewHolder(IExternalService item, SimpleViewHolder holder, int position) {
            holder.primaryText.setText(item.getName());
            holder.colorStrip.setBackgroundColor(colors[random.nextInt(colors.length)]);
            holder.itemView.setTag(item);
        }

        @Override
        public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.simple_list_item, null);
            SimpleViewHolder holder = new SimpleViewHolder(view);
            view.setOnClickListener(this);
            return holder;
        }

        @Override
        public void onClick(View v) {
            IExternalService service = (IExternalService) v.getTag();
            if (service != null) {
                Intent intent = new Intent(ExternalServiceListActivity.this, ExternalServiceEditActivity.class);
                intent.putExtra(ExternalServiceEditActivity.EXTRA_SERVICE_ID, service.getId());
                startActivity(intent);
            }
        }
    }
}
