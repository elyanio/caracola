package com.polymitasoft.caracola.view.booking;

//import android.app.Application;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ActionMenuItemView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.polymitasoft.caracola.DataStoreHolder;
import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.datamodel.Bedroom;

import java.util.ArrayList;
import java.util.List;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;


public class ReservaPrincipal extends AppCompatActivity {
    public final static int BEDROOM_AMOUNT = 10;  //debe ser seguro

    //controles
    private Button bt_limpiar;
    private Button bt_fisicaR;
    private Button bt_preR;
    private Button bt_eliminaR;
    private Button bt_editR;
    private LinearLayout esenas_frameLayout;
    private EntityDataStore<Persistable> dataStore;

    private List<Bedroom> bedrooms = new ArrayList<>();

    //escenas
    private ReservaEsenaPrincipal reservaEsenaPrincipal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserva_principal_activity);

        loadData();
        obtenerControles();
        configurarControles();
        eventos();
    }

    private void loadData() {
        dataStore = DataStoreHolder.getInstance().getDataStore(getApplicationContext());
        bedrooms = dataStore.select(Bedroom.class).get().toList();
    }

    private void obtenerControles() {
        esenas_frameLayout = (LinearLayout) findViewById(R.id.reserva_esenas);
        bt_limpiar = (Button) findViewById(R.id.reserva_bt_limpiar);
        bt_fisicaR = (Button) findViewById(R.id.reserva_bt_FisicaR);
        bt_preR = (Button) findViewById(R.id.reservar_bt_preReservar);
        bt_eliminaR = (Button) findViewById(R.id.reserva_bt_eliminarR);
        bt_editR = (Button) findViewById(R.id.reserva_bt_edit);
    }

    private void configurarControles() {
        reservaEsenaPrincipal = new ReservaEsenaPrincipal(this);
        esenas_frameLayout.addView(reservaEsenaPrincipal);
    }

    private void eventos() {
        bt_preR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPreR();
            }
        });
        bt_editR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickEditR();
            }
        });
        bt_eliminaR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickEliminarR();
            }
        });
        bt_fisicaR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_fisicaR();
            }
        });
    }

    private void click_fisicaR() {
    }

    private void clickEliminarR() {
        reservaEsenaPrincipal.clickEliminarR();
    }

    private void clickEditR() {
        DialogEditarPreReserva dialog = new DialogEditarPreReserva(this);
        dialog.show();
    }

    private void clickPreR() {
        DialogoHacerPreReserva dialog = new DialogoHacerPreReserva(this);
        dialog.show();
    }

    public void disableButtons() {
        bt_preR.setEnabled(false);
        bt_fisicaR.setEnabled(false);
        bt_editR.setEnabled(false);
        bt_eliminaR.setEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reserva_todos, menu);

        for(int i = 0; i < bedrooms.size(); i++) {
            menu.add(0, i, 0, bedrooms.get(i).getName());
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(item.getItemId() != R.id.show_m)
        {
            if(item.getItemId() == R.id.all_m)
            {  // todas las bedrooms
                reservaEsenaPrincipal.getReservaPanelHabitacionActual().setHabitacion(null);
                reservaEsenaPrincipal.getReservaPanelHabitacionActual().actualizarCambioHabitacion();
                reservaEsenaPrincipal.getReservaPanelHabitacionActual().limpiarTodo();
            }
            else
            {
                reservaEsenaPrincipal.getReservaPanelHabitacionActual().setHabitacion(bedrooms.get(item.getItemId()));
                reservaEsenaPrincipal.getReservaPanelHabitacionActual().actualizarCambioHabitacion();
                reservaEsenaPrincipal.getReservaPanelHabitacionActual().limpiarTodo();
            }
            ActionMenuItemView item1 = (ActionMenuItemView) findViewById(R.id.show_m);
            item1.setTitle(item.getTitle().toString());
        }
        return super.onOptionsItemSelected(item);
    }

    public ReservaEsenaPrincipal getReservaEsenaPrincipal() {
        return reservaEsenaPrincipal;
    }

    public Button getBt_editR() {
        return bt_editR;
    }

    public Button getBt_eliminaR() {
        return bt_eliminaR;
    }

    public Button getBt_fisicaR() {
        return bt_fisicaR;
    }

    public Button getBt_limpiar() {
        return bt_limpiar;
    }

    public Button getBt_preR() {
        return bt_preR;
    }

    public List<Bedroom> getBedrooms() {
        return bedrooms;
    }
}
