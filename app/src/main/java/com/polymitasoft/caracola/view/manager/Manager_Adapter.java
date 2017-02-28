package com.polymitasoft.caracola.view.manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.datamodel.Hostel;
import com.polymitasoft.caracola.datamodel.Manager;

import java.util.List;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;


/**
 * Created by asio on 2/21/2017.
 */
public class Manager_Adapter extends BaseAdapter {

    private EntityDataStore<Persistable> dataStore;
    private Context context;
    private List<Manager> managers;
    private String codeHostel;
    private boolean isChecked[];

    public Manager_Adapter(Context context, String codeHostel) {

        this.context = context;
        this.codeHostel = codeHostel;

        dataStore = DataStoreHolder.getInstance().getDataStore(context);
        Hostel hostel = dataStore.select(Hostel.class).where(Hostel.CODE.eq(codeHostel)).get().first();
        managers = dataStore.select(Manager.class).where(Manager.HOSTEL.eq(hostel)).get().toList();

        isChecked = new boolean[managers.size()];
//        getChecked();
    }

//    private void getChecked() {
//        Result<Hostel> hostels = dataStore.select(Hostel.class).where(Hostel.CODE.eq(codeHostel)).get();
//        Result<Manager> managers = dataStore.select(Manager.class).where(Manager.HOSTEL.eq(hostels.first())).get();
//        managersChecked = managers.toList();
//    }
//
//    private boolean contain(Manager manager) {
//        return managersChecked.contains(manager);
//    }

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
        View item = inflater.inflate(R.layout.manager_item_lista, null);

        TextView nombre_gestor = (TextView) item.findViewById(R.id.name_gestor);
        TextView numero_gestor = (TextView) item.findViewById(R.id.number_gestor);
        ImageView imageView = (ImageView) item.findViewById(R.id.eliminar_manager_button);

//        CheckBox checkBox = (CheckBox) item.findViewById(R.id.checkBox_manager);

        nombre_gestor.setText(managers.get(position).getName());
        numero_gestor.setText(managers.get(position).getPhoneNumber());

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dataStore.delete(managers.get(position));
                actualizarListaManager();
                notifyDataSetChanged();
            }
        });

//        if (contain(managers.get(position))) {
//            checkBox.setChecked(true);
//            isChecked[position] = true;
//        }
//        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (isChecked[position]) {
//                    isChecked[position] = false;
//                } else {
//                    isChecked[position] = true;
//                }
//
//            }
//        });
        return item;
    }

    public void actualizarListaManager() {
        Hostel hostel = dataStore.select(Hostel.class).where(Hostel.CODE.eq(codeHostel)).get().first();
        managers = dataStore.select(Manager.class).where(Manager.HOSTEL.eq(hostel)).get().toList();
        isChecked = new boolean[managers.size()];
    }

    public EntityDataStore<Persistable> getDataStore() {
        return dataStore;
    }

    public List<Manager> getManagers() {
        return managers;
    }


    public boolean[] getIsChecked() {
        return isChecked;
    }
}
