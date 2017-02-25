package com.polymitasoft.caracola.view.bedroom;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.datamodel.Bedroom;
import com.polymitasoft.caracola.datamodel.BedroomBuilder;
import com.polymitasoft.caracola.datamodel.Hostel;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

/**
 * Created by asio on 2/25/2017.
 */

public class BedroomInsertActivity extends AppCompatActivity {

    private EntityDataStore<Persistable> data;
    private Bedroom bedroom;
    private BedroomBinding binding;
    private Hostel hostel;
    private String codeHostel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bedroom);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_insert_bedroom);
        }

        codeHostel = getIntent().getExtras().getString("CODE");
        data = DataStoreHolder.getInstance().getDataStore(this);
        hostel = data.select(Hostel.class).where(Hostel.CODE.eq(codeHostel)).get().first();

        bedroom = new BedroomBuilder().build(); // creating a new bedroom
        binding = new BedroomBinding(this, bedroom);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveBedroom();
                return true;
        }
        return false;
    }

    private void saveBedroom() {
        bedroom = binding.getBedroom();
        bedroom.setHostel(hostel);
        data.upsert(bedroom);
        finish();
    }
}
