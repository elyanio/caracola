package com.polymitasoft.caracola.view.bedroom;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.datamodel.Bedroom;
import com.polymitasoft.caracola.datamodel.Hostel;

import java.util.ArrayList;
import java.util.List;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

/**
 * Created by asio on 2/26/2017.
 */

public class BedroomLonlyActivity extends AppCompatActivity {

    private ListView listView;
    private BedroomAdapter bedroomAdapter;
    private Bedroom bedroom;
    private String hostelCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lonly_bedroom);

        hostelCode = getIntent().getExtras().getString("CODE");
        bedroomAdapter = new BedroomAdapter(this);
        listView = (ListView) findViewById(R.id.list_bedroom_lonly);
        listView.setAdapter(bedroomAdapter);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Asignar Habitación");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ok, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_bind_done:
                finish();
        }
        return false;
    }

    private void showDialogCode() {
        final View inflate = getLayoutInflater().inflate(R.layout.room_code, null);
        final AlertDialog.Builder adb = new AlertDialog.Builder(this).setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText roomCode = (EditText) inflate.findViewById(R.id.roomCode);
                String codigo = roomCode.getText().toString();
                if (codigo.length() > 0) {
                    int code = Integer.parseInt(codigo);

                    if (chequearCodigoCuarto(code)) {

                        EntityDataStore<Persistable> dataStore = bedroomAdapter.getDataStore();
                        Hostel hostel = dataStore.select(Hostel.class).where(Hostel.CODE.eq(hostelCode)).get().first();
                        bedroom.setCode(code);
                        bedroom.setHostel(hostel);
                        dataStore.update(bedroom);
                        bedroomAdapter.actualizarVista();
                        bedroomAdapter.notifyDataSetChanged();
                    }
                    else
                    {
                        Toast.makeText(BedroomLonlyActivity.this, "Ya exite una habitacion con ese codigo, ingrese otro por favor.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(BedroomLonlyActivity.this, "Escriba los datospor favor.", Toast.LENGTH_LONG).show();
                }

            }
        }).setNegativeButton("Cancelar", null);
        adb.setCancelable(false);
        adb.setView(inflate);
        adb.setTitle("Room Code");
        adb.show();
    }

    private boolean chequearCodigoCuarto(int code) {
        EntityDataStore<Persistable> dataStore = bedroomAdapter.getDataStore();
        Bedroom bedroom = dataStore.select(Bedroom.class).where(Bedroom.CODE.eq(code)).get().firstOrNull();
        if (bedroom == null) {
            return true;
        } else {
            return false;
        }
    }

    public class BedroomAdapter extends BaseAdapter {

        private EntityDataStore<Persistable> dataStore;
        private Context context;
        private List<Bedroom> bedrooms;
        private Hostel nullHostel = null;


        public BedroomAdapter(Context context) {
            this.context = context;
            dataStore = DataStoreHolder.getInstance().getDataStore(context);
            bedrooms = new ArrayList<>();
            bedrooms = dataStore.select(Bedroom.class).where(Bedroom.HOSTEL.eq(nullHostel)).get().toList();
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
        public View getView(final int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View item = inflater.inflate(R.layout.simple_list_item, null);

            TextView bedroomName = (TextView) item.findViewById(R.id.primary_text);
            TextView bedroomCode = (TextView) item.findViewById(R.id.secondary_text);

            bedroomName.setText(bedrooms.get(i).getName());
            bedroomCode.setText("Codigo:" + bedrooms.get(i).getCode());

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bedroom = bedrooms.get(i);
                    showDialogCode();
                }
            });
            return item;
        }

        public void actualizarVista() {
            bedrooms = dataStore.select(Bedroom.class).where(Bedroom.HOSTEL.eq(nullHostel)).get().toList();
        }

        public EntityDataStore<Persistable> getDataStore() {
            return dataStore;
        }
    }
}
