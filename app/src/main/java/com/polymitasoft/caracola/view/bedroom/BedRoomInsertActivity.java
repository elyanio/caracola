package com.polymitasoft.caracola.view.bedroom;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

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
    private int codeRoom;

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
                showDialogCode();
                return true;
        }
        return false;
    }

    private void saveBedroom(int code) {
        bedroom = binding.getBedroom();
        bedroom.setHostel(hostel);
        bedroom.setCode(code);
        data.upsert(bedroom);
        finish();
    }

    private void showDialogCode() {
        final View inflate = getLayoutInflater().inflate(R.layout.room_code, null);
        final AlertDialog.Builder adb = new AlertDialog.Builder(this).setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText roomCode = (EditText) inflate.findViewById(R.id.roomCode);
                String codigo = roomCode.getText().toString();
                int code = Integer.parseInt(codigo);
                saveBedroom(code);
            }
        }).setNegativeButton("Cancelar", null);
        adb.setView(inflate);
        adb.setTitle("Room Code");
        adb.show();
    }
}
