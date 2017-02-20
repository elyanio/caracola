package com.polymitasoft.caracola.view.supplier;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.datamodel.ExternalService;
import com.polymitasoft.caracola.view.ListActivity;

import java.util.Random;

import io.requery.android.QueryRecyclerAdapter;
import io.requery.query.Result;

/**
 * Created by rainermf on 16/2/2017.
 */

public class ExternalServiceListActivity extends ListActivity<ExternalService> {

    @Override
    protected QueryRecyclerAdapter<ExternalService, SimpleViewHolder> createAdapter() {
        return new ServiceAdapter();
    }

    /**
     * Created by rainermf on 15/2/2017.
     */
    class ServiceAdapter extends QueryRecyclerAdapter<ExternalService, SimpleViewHolder> implements View.OnClickListener {

        private final Random random = new Random();
        private final int[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA};

        ServiceAdapter() {
            super(ExternalService.$TYPE);
        }

        @Override
        public Result<ExternalService> performQuery() {
            return data.select(ExternalService.class).get();
        }

        @Override
        public void onBindViewHolder(ExternalService item, SimpleViewHolder holder, int position) {
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
            ExternalService service = (ExternalService) v.getTag();
            if (service != null) {
//                Intent intent = new Intent(ServiceListActivity.this, BookingEditActivity.class);
//                intent.putExtra(BookingEditActivity.EXTRA_BOOKING_ID, service.getId());
//                startActivity(intent);
            }
        }
    }
}
