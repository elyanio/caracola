package com.polymitasoft.caracola.view.manager;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.datamodel.Hostel;
import com.polymitasoft.caracola.datamodel.Manager;

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
    private ImageView delete_manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        codeHostel = getIntent().getExtras().getString("CODE");

        manager_adapter = new Manager_Adapter(this, codeHostel);
        managers = (ListView) findViewById(R.id.lista_gestor);
        managers.setAdapter(manager_adapter);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Gestor");
        }
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
        }

        return super.onOptionsItemSelected(item);
    }

    private void insertarManager() {
        final View inflate = getLayoutInflater().inflate(R.layout.manager_insertar, null);
        final AlertDialog.Builder adb = new AlertDialog.Builder(this).setPositiveButton("Insertar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                EditText name_manager = (EditText) inflate.findViewById(R.id.manager_editText_nombre);
                EditText number_manager = (EditText) inflate.findViewById(R.id.manager_editText_numero);

                String number = number_manager.getText().toString();

                if (name_manager.getText().toString().length() > 0) {
                    if (number.length() == 8 && number.charAt(0) == '5') {
                        Manager manager = new Manager();
                        manager.setName(name_manager.getText().toString());
                        manager.setPhoneNumber(number_manager.getText().toString());

                        EntityDataStore<Persistable> dataStore = manager_adapter.getDataStore();
                        Result<Hostel> hostel = dataStore.select(Hostel.class).where(Hostel.CODE.eq(codeHostel)).get();
                        manager.setHostel(hostel.first());

                        dataStore.upsert(manager);

                        manager_adapter.actualizarListaManager();
                    } else {
                        Toast.makeText(ManagerActivity.this, "Su numero no es correcto.", Toast.LENGTH_LONG).show();
                        insertarManager();
                    }
                } else {
                    Toast.makeText(ManagerActivity.this, "Escriba los datos por favor.", Toast.LENGTH_LONG).show();
                    insertarManager();
                }

            }
        }).setNegativeButton("Cancelar", null);
        adb.setCancelable(false);
        adb.setView(inflate);
        adb.setTitle("Nuevo Gestor");
        adb.show();
    }

    private void actualizarrManager(final String name, final String numero) {
        final View inflate = getLayoutInflater().inflate(R.layout.manager_insertar, null);

        final EditText name_manager = (EditText) inflate.findViewById(R.id.manager_editText_nombre);
        final EditText number_manager = (EditText) inflate.findViewById(R.id.manager_editText_numero);

        name_manager.setText(name);
        number_manager.setText(numero);

        final EntityDataStore<Persistable> dataStore = manager_adapter.getDataStore();
        final Manager manager = dataStore.select(Manager.class).where(Manager.NAME.eq(name).and(Manager.PHONE_NUMBER.eq(numero))).get().first();

        final AlertDialog.Builder adb = new AlertDialog.Builder(this).setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                String number = number_manager.getText().toString();

                if (name_manager.getText().toString().length() > 0) {
                    if (number.length() == 8 && number.charAt(0) == '5') {

                        manager.setName(name_manager.getText().toString());
                        manager.setPhoneNumber(number_manager.getText().toString());
                        dataStore.update(manager);
                        manager_adapter.actualizarListaManager();
                        manager_adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(ManagerActivity.this, "Su numero no es correcto.", Toast.LENGTH_LONG).show();
                        actualizarrManager(name, numero);
                    }
                } else {
                    Toast.makeText(ManagerActivity.this, "Escriba los datos por favor.", Toast.LENGTH_LONG).show();
                    actualizarrManager(name, numero);
                }

            }
        }).setNegativeButton("Cancelar", null);
        adb.setCancelable(false);
        adb.setView(inflate);
        adb.setTitle("Actualizar Gestor");
        adb.show();
    }

    public class Manager_Adapter extends BaseAdapter {

        private EntityDataStore<Persistable> dataStore;
        private Context context;
        private List<Manager> managers;
        private String codeHostel;

        public Manager_Adapter(Context context, String codeHostel) {

            this.context = context;
            this.codeHostel = codeHostel;

            dataStore = DataStoreHolder.getInstance().getDataStore(context);
            Hostel hostel = dataStore.select(Hostel.class).where(Hostel.CODE.eq(codeHostel)).get().first();
            managers = dataStore.select(Manager.class).where(Manager.HOSTEL.eq(hostel)).get().toList();

        }

        @Override
        public int getCount() {
            return managers.size();
        }

        @Override
        public Object getItem(int position) {
            return managers.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View item = inflater.inflate(R.layout.manager_item_lista, null);

            TextView nombre_gestor = (TextView) item.findViewById(R.id.name_gestor);
            TextView numero_gestor = (TextView) item.findViewById(R.id.number_gestor);
            ImageView imageView = (ImageView) item.findViewById(R.id.eliminar_manager_button);

            nombre_gestor.setText(managers.get(position).getName());
            numero_gestor.setText(managers.get(position).getPhoneNumber());

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    TextView nombre_gestor = (TextView) item.findViewById(R.id.name_gestor);
                    TextView numero_gestor = (TextView) item.findViewById(R.id.number_gestor);

                    String nombre = nombre_gestor.getText().toString();
                    String numero = numero_gestor.getText().toString();

                    actualizarrManager(nombre, numero);

                    Manager manager = dataStore.select(Manager.class).where(Manager.NAME.eq(nombre).and(Manager.PHONE_NUMBER.eq(numero))).get().first();

                }
            });

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dataStore.delete(managers.get(position));
                    actualizarListaManager();
                    notifyDataSetChanged();
                }
            });
            return item;
        }


        public void actualizarListaManager() {
            Hostel hostel = dataStore.select(Hostel.class).where(Hostel.CODE.eq(codeHostel)).get().first();
            managers = dataStore.select(Manager.class).where(Manager.HOSTEL.eq(hostel)).get().toList();
        }

        public EntityDataStore<Persistable> getDataStore() {
            return dataStore;
        }

        public List<Manager> getManagers() {
            return managers;
        }
    }
}
