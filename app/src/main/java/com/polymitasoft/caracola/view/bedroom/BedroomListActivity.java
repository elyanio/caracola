package com.polymitasoft.caracola.view.bedroom;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.datamodel.Bedroom;
import com.polymitasoft.caracola.datamodel.InternalService;
import com.polymitasoft.caracola.view.ListActivity;

import java.util.Random;

import io.requery.android.QueryRecyclerAdapter;
import io.requery.query.Result;

/**
 * Created by rainermf on 16/2/2017.
 */

public class BedroomListActivity extends ListActivity<Bedroom> {

    @Override
    protected QueryRecyclerAdapter<Bedroom, Holder> createAdapter() {
        return new BedroomAdapter();
    }

    /**
     * Created by rainermf on 15/2/2017.
     */
    class BedroomAdapter extends QueryRecyclerAdapter<Bedroom, Holder> implements View.OnClickListener {

        private final Random random = new Random();
        private final int[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA};

        BedroomAdapter() {
            super(Bedroom.$TYPE);
        }

        @Override
        public Result<Bedroom> performQuery() {
            return data.select(Bedroom.class).get();
        }

        @Override
        public void onBindViewHolder(Bedroom item, Holder holder, int position) {
            holder.name.setText(item.getName());
            holder.image.setBackgroundColor(colors[random.nextInt(colors.length)]);
            holder.itemView.setTag(item);
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.booking_item, null);
            Holder holder = new Holder(view);
            view.setOnClickListener(this);
            return holder;
        }

        @Override
        public void onClick(View v) {
            Bedroom service = (Bedroom) v.getTag();
            if (service != null) {
//                Intent intent = new Intent(ServiceListActivity.this, BookingEditActivity.class);
//                intent.putExtra(BookingEditActivity.EXTRA_BOOKING_ID, service.getId());
//                startActivity(intent);
            }
        }
    }
}
