package com.polymitasoft.caracola.view.bedroom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.datamodel.Bedroom;
import com.polymitasoft.caracola.datamodel.Hostel;

import java.util.List;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

/**
 * Created by asio on 2/25/2017.
 */

public class BedroomHostelAdapter extends BaseAdapter {

    private String hostelCode;
    private EntityDataStore<Persistable> dataStore;
    private Context context;
    private List<Bedroom> bedrooms;

    public BedroomHostelAdapter(Context context, String hostelCode) {
        this.context = context;
        this.hostelCode = hostelCode;

        dataStore = DataStoreHolder.INSTANCE.getDataStore();
        Hostel hostel = dataStore.select(Hostel.class).where(Hostel.CODE.eq(hostelCode)).get().first();
        bedrooms = dataStore.select(Bedroom.class).where(Bedroom.HOSTEL.eq(hostel)).get().toList();
    }

    @Override
    public int getCount() {
        return bedrooms.size();
    }

    @Override
    public Object getItem(int i) {
        return bedrooms.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item = inflater.inflate(R.layout.simple_list_item, null);

        TextView primary_text = (TextView) item.findViewById(R.id.primary_text);
        TextView secundary_text = (TextView) item.findViewById(R.id.secondary_text);
        ImageView delete_menu = (ImageView) item.findViewById(R.id.delete_menu);

        ImageView edit_menu = (ImageView) item.findViewById(R.id.edit_menu);
        edit_menu.setVisibility(View.INVISIBLE);

        primary_text.setText(bedrooms.get(i).getName());
        final int roomCode = bedrooms.get(i).getCode();
        secundary_text.setText("Código: " + roomCode);

        delete_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                desvincularHabitacion(roomCode);
            }
        });

        return item;
    }

    private void desvincularHabitacion(int roomCode) {

        Hostel hostel = dataStore.select(Hostel.class).where(Hostel.CODE.eq(hostelCode)).get().first();
        Bedroom bedroom = dataStore.select(Bedroom.class).where(Bedroom.HOSTEL.eq(hostel).and(Bedroom.CODE.eq(roomCode))).get().first();
        bedroom.setCode(0);
        bedroom.setHostel(null);
        dataStore.update(bedroom);
        actualizarLista();
        notifyDataSetChanged();
    }

    public void actualizarLista() {
        Hostel hostel = dataStore.select(Hostel.class).where(Hostel.CODE.eq(hostelCode)).get().first();
        bedrooms = dataStore.select(Bedroom.class).where(Bedroom.HOSTEL.eq(hostel)).get().toList();
    }
}

