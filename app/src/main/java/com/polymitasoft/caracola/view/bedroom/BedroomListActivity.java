package com.polymitasoft.caracola.view.bedroom;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.datamodel.IBedroom;
import com.polymitasoft.caracola.view.ListActivity;

import java.util.Random;

import io.requery.android.QueryRecyclerAdapter;
import io.requery.query.Result;

/**
 * Created by rainermf on 16/2/2017.
 */

public class BedroomListActivity extends ListActivity<IBedroom> {

    @Override
    protected QueryRecyclerAdapter<IBedroom, SimpleViewHolder> createAdapter() {
        return new BedroomAdapter();
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
                Intent intent = new Intent(this, BedroomEditActivity.class);
                startActivity(intent);
                return true;
        }
        return false;
    }

    /**
     * Created by rainermf on 15/2/2017.
     */
    class BedroomAdapter extends QueryRecyclerAdapter<IBedroom, SimpleViewHolder> implements View.OnClickListener {

        private final Random random = new Random();
        private final int[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA};

//        BedroomAdapter() {
//            super(BedroomEntity.$TYPE);
//        }

        @Override
        public Result<IBedroom> performQuery() {
            return data.select(IBedroom.class).get();
        }

        @Override
        public void onBindViewHolder(IBedroom item, SimpleViewHolder holder, int position) {
            holder.primaryText.setText(item.getName());
            holder.colorStrip.setBackgroundColor(colors[random.nextInt(colors.length)]);
            holder.itemView.setTag(item);
            holder.itemView.setOnClickListener(this);
        }

        @Override
        public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.booking_item, parent, false);
            return new SimpleViewHolder(view);
        }

        @Override
        public void onClick(View v) {
            IBedroom service = (IBedroom) v.getTag();
            if (service != null) {
                Intent intent = new Intent(BedroomListActivity.this, BedroomEditActivity.class);
                intent.putExtra(BedroomEditActivity.EXTRA_BEDROOM_ID, service.getId());
                startActivity(intent);
            }
        }
    }
}
