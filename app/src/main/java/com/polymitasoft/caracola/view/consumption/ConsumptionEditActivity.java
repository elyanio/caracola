package com.polymitasoft.caracola.view.consumption;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.datamodel.Booking;
import com.polymitasoft.caracola.datamodel.Consumption;
import com.polymitasoft.caracola.datamodel.ConsumptionBuilder;

import org.threeten.bp.LocalDate;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;


public class ConsumptionEditActivity extends AppCompatActivity {

    public static final String EXTRA_CONSUMPTION_ID = "consumptionId";
    public static final String EXTRA_BOOKING_ID = "bookingId";

    private EntityDataStore<Persistable> data;
    private Consumption consumption;
    private ConsumptionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_consumption);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_edit_consumption);
        }
        data = DataStoreHolder.INSTANCE.getDataStore();
        int consumptionId = getIntent().getIntExtra(EXTRA_CONSUMPTION_ID, -1);
        if (consumptionId == -1) {
            int idBooking = getIntent().getIntExtra(EXTRA_BOOKING_ID, -1);
            Booking booking = null;
            if(idBooking != -1) {
                booking = data.findByKey(Booking.class, idBooking);
            }
            consumption = new ConsumptionBuilder()
                    .booking(booking)
                    .date(LocalDate.now())
                    .build();
        } else {
            consumption = data.findByKey(Consumption.class, consumptionId);
        }
        binding = new ConsumptionBinding(this, consumption);

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
                save();
                return true;
        }
        return false;
    }

    private void save() {
        consumption = binding.getConsumption();
        if(consumption.getInternalService() == null) {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.error_select_service)
                    .setPositiveButton(R.string.ok_action_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        } else {
            data.upsert(consumption);
            finish();
        }
    }
}
