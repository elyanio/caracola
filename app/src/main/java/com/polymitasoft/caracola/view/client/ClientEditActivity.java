package com.polymitasoft.caracola.view.client;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.polymitasoft.caracola.DataStoreHolder;
import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.datamodel.Client;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

/**
 * Created by rainermf on 13/2/2017.
 */

public class ClientEditActivity extends AppCompatActivity {

    @BindView(R.id.bookingsRecyclerView) RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.client_edit_model);
        ButterKnife.bind(this);

        ClientEditAdapter adapter = new ClientEditAdapter();

        LinearLayoutManager layout = new LinearLayoutManager(this);
        layout.setOrientation(LinearLayoutManager.VERTICAL);

        EntityDataStore<Persistable> dataStore = DataStoreHolder.getInstance().getDataStore(this);
        Client client = dataStore.select(Client.class).get().first();
        adapter.setClient(client);
//        setClient(client);

        recyclerView.setLayoutManager(layout);
        recyclerView.setAdapter(adapter);
    }

//    public Client getClient() {
//        Client client = new Client();
//        client.setPassport(editView.getPassport());
//        client.setFirstName(editView.getFirstName());
//        client.setLastName(editView.getLastName());
////        client.setCountry(editView.getCountry());
////        client.setGender(editView.getGender());
////        client.setBirthday(editView.getBirthday());
//
//        return client;
//    }
}
