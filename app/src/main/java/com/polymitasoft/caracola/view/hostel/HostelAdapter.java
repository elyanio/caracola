package com.polymitasoft.caracola.view.hostel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.datamodel.Hostel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

/**
 * Created by asio on 2/24/2017.
 */

public class HostelAdapter extends BaseAdapter {

    private EntityDataStore<Persistable> dataStore;
    private Context context;
    List<Hostel> hostels;

    public HostelAdapter(Context context) {
        this.context = context;
        dataStore = DataStoreHolder.getInstance().getDataStore(context);
        hostels = new ArrayList<>();
        hostels = dataStore.select(Hostel.class).get().toList();
    }

    @Override
    public int getCount() {
        return hostels.size();
    }

    @Override
    public Object getItem(int position) {
        return hostels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item = inflater.inflate(R.layout.hostel_item_lista, null);

        TextView hostalName = (TextView) item.findViewById(R.id.name_hostal);
        TextView hostalCode = (TextView) item.findViewById(R.id.code_hostal);

        hostalName.setText(hostels.get(position).getName());
        hostalCode.setText(hostels.get(position).getCode());

        return item;
    }

    public void actualizarListaHostel() {
        hostels = dataStore.select(Hostel.class).get().toList();
    }

    public EntityDataStore<Persistable> getDataStore() {
        return dataStore;
    }
}
