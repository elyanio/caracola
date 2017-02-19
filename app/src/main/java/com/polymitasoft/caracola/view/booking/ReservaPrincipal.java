package com.polymitasoft.caracola.view.booking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jakewharton.threetenabp.AndroidThreeTen;
import com.polymitasoft.caracola.DataStoreHolder;
import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.datamodel.Bedroom;
import com.polymitasoft.caracola.settings.SettingsActivity;
import com.polymitasoft.caracola.view.bedroom.BedroomListActivity;
import com.polymitasoft.caracola.view.supplier.ExternalServiceListActivity;
import com.polymitasoft.caracola.view.service.ServiceListActivity;
import com.polymitasoft.caracola.view.supplier.SupplierListActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.requery.Persistable;
import io.requery.sql.EntityDataStore;


public class ReservaPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //controles
    private EntityDataStore<Persistable> dataStore;
    @BindView(R.id.reserva_esenas) LinearLayout esenas_frameLayout;
    @BindView(R.id.editButton) Button editButton;
    @BindView(R.id.bookButton) Button bookButton;
    @BindView(R.id.deleteButton) Button deleteButton;
    @BindView(R.id.checkInButton) Button checkInButton;

    private List<Bedroom> bedrooms = new ArrayList<>();

    //escenas
    private ReservaEsenaPrincipal reservaEsenaPrincipal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserva_principal_activity);
        ButterKnife.bind(this);

        Locale.setDefault(new Locale("es"));
        AndroidThreeTen.init(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        loadData();
        configurarControles();
        eventos();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void loadData() {
        dataStore = DataStoreHolder.getInstance().getDataStore(getApplicationContext());
        bedrooms = dataStore.select(Bedroom.class).get().toList();
    }

    private void configurarControles() {
        reservaEsenaPrincipal = new ReservaEsenaPrincipal(this);
        esenas_frameLayout.addView(reservaEsenaPrincipal);
    }

    private void eventos() {
        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPreR();
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickEditR();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickEliminarR();
            }
        });
        checkInButton.setOnClickListener(new View.OnClickListener() {
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
        editButton.setEnabled(false);
        deleteButton.setEnabled(false);
        checkInButton.setEnabled(false);
        bookButton.setEnabled(false);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_camera:
                startActivity(new Intent(this, ServiceListActivity.class));
                break;
            case R.id.nav_gallery:
                startActivity(new Intent(this, ExternalServiceListActivity.class));
                break;
            case R.id.nav_slideshow:
                startActivity(new Intent(this, CurrentBookingsActivity.class));
                break;
            case R.id.nav_manage:
                startActivity(new Intent(this, SupplierListActivity.class));
                break;
            case R.id.nav_share:
                startActivity(new Intent(this, BedroomListActivity.class));
                break;
            case R.id.nav_send:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public ReservaEsenaPrincipal getReservaEsenaPrincipal() {
        return reservaEsenaPrincipal;
    }

    public void enableEdit() {
        editButton.setEnabled(true);
    }

    public void enableDelete() {
        deleteButton.setEnabled(true);
    }

    public void enableCheckIn() {
        checkInButton.setEnabled(true);
    }

    public void enableBook() {
        bookButton.setEnabled(true);
    }

    public List<Bedroom> getBedrooms() {
        return bedrooms;
    }
}
