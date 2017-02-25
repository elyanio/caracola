package com.polymitasoft.caracola.view.gestor;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.datamodel.Hostel;
import com.polymitasoft.caracola.datamodel.Manager;

import java.sql.SQLException;
import java.util.List;

import io.requery.Persistable;
import io.requery.query.Result;
import io.requery.sql.EntityDataStore;

/**
 * Created by asio on 2/21/2017.
 */
public class ManagerActivity extends AppCompatActivity {

    private ListView managers;
    private Manager_Adapter manager_adapter;
    private String manager_name;
    private String manager_number;
    private String codeHostel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        codeHostel = getIntent().getExtras().getString("CODE");

        manager_adapter = new Manager_Adapter(this, codeHostel);
        managers = (ListView) findViewById(R.id.lista_gestor);
        managers.setAdapter(manager_adapter);
        manager_adapter.notifyDataSetChanged();

        eventos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_manager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_hg_add:
                insertarManager();
                break;
            case R.id.action_hg_done:
                actualizarManagers();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void actualizarManagers() {

        List<Manager> managers = manager_adapter.getManagers();
        boolean[] isChecked = manager_adapter.getIsChecked();
        EntityDataStore<Persistable> dataStore = manager_adapter.getDataStore();

        for (int i = 0; i < isChecked.length; i++) {

            if (isChecked[i]) {
                dataStore.delete(managers.get(i));
            }
        }
        manager_adapter.actualizarListaManager();
        manager_adapter.notifyDataSetChanged();
    }

    private void eventos() {
        managers.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                Intent intent = new Intent(ManagerActivity.this, Hostal_Activity.class);
//                Gestor gestor = (Gestor) manager_adapter.getItem(position);
//                Bundle bundle = new Bundle();
//                bundle.putInt("IDGESTOR", gestor.getId_auto());
//                bundle.putInt("ESTADO", estado);
//
//                intent.putExtras(bundle);
//                startActivity(intent);
//                finish();
            }
        });
    }

    private void insertarManager() {
        final View inflate = getLayoutInflater().inflate(R.layout.manager_insertar, null);
        final AlertDialog.Builder adb = new AlertDialog.Builder(this).setPositiveButton("Insertar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                EditText name_manager = (EditText) inflate.findViewById(R.id.manager_editText_nombre);
                EditText number_manager = (EditText) inflate.findViewById(R.id.manager_editText_numero);

                Manager manager = new Manager();
                manager.setName(name_manager.getText().toString());
                manager.setPhoneNumber(number_manager.getText().toString());

                EntityDataStore<Persistable> dataStore = manager_adapter.getDataStore();
                Result<Hostel> hostel = dataStore.select(Hostel.class).where(Hostel.CODE.eq(codeHostel)).get();
                manager.setHostel(hostel.first());

                dataStore.upsert(manager);

                manager_adapter.actualizarListaManager();
            }
        }).setNegativeButton("Cancelar", null);
        adb.setCancelable(false);
        adb.setView(inflate);
        adb.setTitle("Nuevo Gestor");
        adb.show();
    }
}
