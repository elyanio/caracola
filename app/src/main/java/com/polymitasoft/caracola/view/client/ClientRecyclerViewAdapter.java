package com.polymitasoft.caracola.view.client;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.components.SimpleListAdapter;
import com.polymitasoft.caracola.components.SimpleViewHolder;
import com.polymitasoft.caracola.dataaccess.BookingDao;
import com.polymitasoft.caracola.datamodel.Booking;
import com.polymitasoft.caracola.datamodel.Client;
import com.polymitasoft.caracola.view.client.ClientFragment.OnListInteractionListener;

import io.requery.query.Result;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Client} and makes a call to the
 * specified {@link OnListInteractionListener}.
 */
class ClientRecyclerViewAdapter extends SimpleListAdapter<Client> {

    private final OnListInteractionListener mListener;
    private final BookingDao bookingDao;
    private final Booking booking;

    ClientRecyclerViewAdapter(Context context, Booking booking, OnListInteractionListener listener) {
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
    }

    @Override
    protected void viewItem(Client item) {
        editItem(item);
    }

    @Override
    protected void editItem(Client item) {
        mListener.onClientListInteraction(item);
    }
}
