package com.polymitasoft.caracola.view.booking;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.dataaccess.BookingDao;
import com.polymitasoft.caracola.datamodel.Booking;
import com.polymitasoft.caracola.datamodel.Client;
import com.polymitasoft.caracola.datamodel.IBooking;
import com.polymitasoft.caracola.datamodel.IClient;
import com.polymitasoft.caracola.view.booking.ClientFragment.OnListInteractionListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.requery.Persistable;
import io.requery.android.QueryRecyclerAdapter;
import io.requery.query.Result;
import io.requery.sql.EntityDataStore;

/**
 * {@link RecyclerView.Adapter} that can display a {@link IClient} and makes a call to the
 * specified {@link OnListInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ClientRecyclerViewAdapter extends QueryRecyclerAdapter<Client, ClientRecyclerViewAdapter.ViewHolder> {

    private final OnListInteractionListener mListener;
    private final BookingDao bookingDao;
    private final Booking booking;

    public ClientRecyclerViewAdapter(EntityDataStore<Persistable> dataStore, Booking booking, OnListInteractionListener listener) {
        this.booking = booking;
        mListener = listener;

        bookingDao = new BookingDao(dataStore);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_client, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public Result<Client> performQuery() {
        return bookingDao.getClients(booking);
    }

    @Override
    public void onBindViewHolder(final Client item, ViewHolder holder, int position) {

        String format = holder.primaryText.getContext().getText(R.string.person_full_name).toString();
        String name = String.format(format, item.getFirstName(), item.getLastName());
        holder.primaryText.setText(name);
        holder.secondaryText.setText(item.getPassport());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onClientListInteraction(item);
                }
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.primary_text) TextView primaryText;
        @BindView(R.id.secondary_text) TextView secondaryText;

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
