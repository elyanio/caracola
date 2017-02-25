package com.polymitasoft.caracola.view.supplier;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.datamodel.ISupplier;
import com.polymitasoft.caracola.view.ListActivity;

import java.util.Random;

import io.requery.android.QueryRecyclerAdapter;
import io.requery.query.Result;

/**
 * Created by rainermf on 16/2/2017.
 */

public class SupplierListActivity extends ListActivity<ISupplier> {

    @Override
    protected QueryRecyclerAdapter<ISupplier, SimpleViewHolder> createAdapter() {
        return new SupplierAdapter();
    }

    class SupplierAdapter extends QueryRecyclerAdapter<ISupplier, SimpleViewHolder> implements View.OnClickListener {

        private final Random random = new Random();
        private final int[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA};

        SupplierAdapter() {
//            super(SupplierEntity.$TYPE);
        }

        @Override
        public Result<ISupplier> performQuery() {
            return data.select(ISupplier.class).get();
        }

        @Override
        public void onBindViewHolder(ISupplier item, SimpleViewHolder holder, int position) {
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
            ISupplier supplier = (ISupplier) v.getTag();
            if (supplier != null) {
//                Intent intent = new Intent(ServiceListActivity.this, BookingEditActivity.class);
//                intent.putExtra(BookingEditActivity.EXTRA_BOOKING_ID, service.getId());
//                startActivity(intent);
            }
        }
    }
}
