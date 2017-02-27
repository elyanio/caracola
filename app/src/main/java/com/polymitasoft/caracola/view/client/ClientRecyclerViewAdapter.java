package com.polymitasoft.caracola.view.client;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.components.SimpleListAdapter;
import com.polymitasoft.caracola.components.SimpleViewHolder;
import com.polymitasoft.caracola.dataaccess.BookingDao;
import com.polymitasoft.caracola.datamodel.Booking;
import com.polymitasoft.caracola.datamodel.Client;
import com.polymitasoft.caracola.datamodel.IClient;
import com.polymitasoft.caracola.view.client.ClientFragment.OnListInteractionListener;

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
public class ClientRecyclerViewAdapter extends SimpleListAdapter<Client> {

    private final OnListInteractionListener mListener;
    private final BookingDao bookingDao;
    private final Booking booking;

    public ClientRecyclerViewAdapter(Context context, Booking booking, OnListInteractionListener listener) {
        super(context, Client.$TYPE);
        this.booking = booking;
        mListener = listener;

        bookingDao = new BookingDao(dataStore);
    }

    @Override
    public Result<Client> performQuery() {
        return bookingDao.getClients(booking);
    }

    @Override
    public void onBindViewHolder(final Client item, SimpleViewHolder holder, int position) {
        super.onBindViewHolder(item, holder, position);
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
}
