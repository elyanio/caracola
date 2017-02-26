package com.polymitasoft.caracola.view.hostel;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.datamodel.Hostel;
import com.polymitasoft.caracola.datamodel.Manager;
import com.polymitasoft.caracola.view.bedroom.BedroomHostelActivity;
import com.polymitasoft.caracola.view.bedroom.BedroomLonlyActivity;
import com.polymitasoft.caracola.view.manager.ManagerActivity;

import java.util.ArrayList;

import io.requery.Persistable;
import io.requery.query.Result;
import io.requery.sql.EntityDataStore;

/**
 * Created by asio on 2/24/2017.
 */

public class HostelActivity extends AppCompatActivity {

    private ListView hostels;
    private HostelAdapter hostelAdapter;
    private String nameHostal;
    private String codeHostal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hostel);

        hostelAdapter = new HostelAdapter(this);
        hostels = (ListView) findViewById(R.id.hostal_lista);
        hostels.setAdapter(hostelAdapter);

        hostels.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                final Hostel hostel = (Hostel) hostelAdapter.getItem(i);
                final PopupMenu popupMenu = new PopupMenu(HostelActivity.this, view);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        int id = item.getItemId();
                        switch (id) {
                            case R.id.menu_hostel_proveedor:
                                startActivity(new Intent(HostelActivity.this, ManagerActivity.class).putExtra("CODE", hostel.getCode()));
                                break;
                            case R.id.menu_hostel_habitacion:
                                startActivity(new Intent(HostelActivity.this, BedroomHostelActivity.class).putExtra("CODE", hostel.getCode()));
                                break;
                            case R.id.menu_hostel_bind:
                                startActivity(new Intent(HostelActivity.this, BedroomLonlyActivity.class).putExtra("CODE", hostel.getCode()));
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_plus:
                abrirDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void abrirDialog() {
        final View inflate = getLayoutInflater().inflate(R.layout.hostel_insertar, null);
        final AlertDialog.Builder adb = new AlertDialog.Builder(this).setPositiveButton("Insertar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {


                EditText name_hostel = (EditText) inflate.findViewById(R.id.hostel_editText_nombre);
                EditText number_hostel = (EditText) inflate.findViewById(R.id.hostel_editText_code);

                nameHostal = name_hostel.getText().toString();
                codeHostal = number_hostel.getText().toString();
                insertarHostel();

                startActivity(new Intent(HostelActivity.this, ManagerActivity.class).putExtra("CODE", codeHostal));
                finish();

            }
        }).setNegativeButton("Cancelar", null);
        adb.setView(inflate);
        adb.setTitle("Nuevo Hostal");
        adb.show();
    }

    private void insertarHostel() {
        Hostel hostel = new Hostel();
        hostel.setName(nameHostal);
        hostel.setCode(codeHostal);

        EntityDataStore<Persistable> dataStore = hostelAdapter.getDataStore();
        dataStore.insert(hostel);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == RESULT_OK) {
//
//            Hostel hostel = new Hostel();
//            hostel.setName(nameHostal);
//            hostel.setCode(codeHostal);
//
//            EntityDataStore<Persistable> dataStore = hostelAdapter.getDataStore();
//            ArrayList<Integer> checks = data.getIntegerArrayListExtra("CHECK");
//
//            for (int check : checks) {
//                Result<Manager> manager = dataStore.select(Manager.class).where(Manager.ID.eq(check)).get();
//                Manager mng = manager.first();
//                mng.setHostel(hostel);
//
//                dataStore.insert(mng);
//            }
//        }
//    }
}

