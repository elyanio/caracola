package com.polymitasoft.caracola.view.consumption;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.dataaccess.BookingDao;
import com.polymitasoft.caracola.datamodel.Booking;
import com.polymitasoft.caracola.datamodel.Consumption;
import com.polymitasoft.caracola.util.FormatUtils;
import com.polymitasoft.caracola.view.client.ClientFragment.OnListInteractionListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.requery.Persistable;
import io.requery.android.QueryRecyclerAdapter;
import io.requery.query.Result;
import io.requery.sql.EntityDataStore;

import static com.polymitasoft.caracola.dataaccess.Consumptions.cost;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Consumption} and makes a call to the
 * specified {@link OnListInteractionListener}.
 */
public class ConsumptionRecyclerViewAdapter
        extends QueryRecyclerAdapter<Consumption, ConsumptionRecyclerViewAdapter.ViewHolder> {

    private final ConsumptionFragment.OnListInteractionListener mListener;
    private final BookingDao bookingDao;
    private final Booking booking;
    private final EntityDataStore<Persistable> dataStore;

    public ConsumptionRecyclerViewAdapter(EntityDataStore<Persistable> dataStore, Booking booking, ConsumptionFragment.OnListInteractionListener listener) {
        this.booking = booking;
        mListener = listener;

        this.dataStore = dataStore;
        bookingDao = new BookingDao(this.dataStore);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_consumption, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public Result<Consumption> performQuery() {
        return bookingDao.getConsumptions(booking);
    }

    @Override
    public void onBindViewHolder(final Consumption consumption, ViewHolder holder, int position) {

        holder.primaryText.setText(consumption.getInternalService().getName());
        holder.secondaryText.setText(FormatUtils.formatMoney(cost(consumption)));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onConsumptionListInteraction(consumption);
                }
            }
        });
        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataStore.delete(consumption);
                queryAsync();
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.primary_text) TextView primaryText;
        @BindView(R.id.secondary_text) TextView secondaryText;
        @BindView(R.id.contextual_remove) ImageView removeButton;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + primaryText.getText() + "'";
        }
    }
}