package com.polymitasoft.caracola.view.booking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.datamodel.Bedroom;
import com.polymitasoft.caracola.datamodel.Booking;
import com.polymitasoft.caracola.settings.SettingsActivity;
import com.polymitasoft.caracola.view.bedroom.BedroomListActivity;
import com.polymitasoft.caracola.view.gestor.ManagerActivity;
import com.polymitasoft.caracola.view.hostel.HostelActivity;
import com.polymitasoft.caracola.view.service.InternalServiceListActivity;
import com.polymitasoft.caracola.view.supplier.ExternalServiceListActivity;
import com.polymitasoft.caracola.view.supplier.SupplierListActivity;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

import static butterknife.ButterKnife.findById;
import static com.polymitasoft.caracola.view.booking.CalendarState.toCalendarState;

/**
 * @author yanier.alfonso
 */
public class ReservaPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, EditBookingDialogFragment.OnBookingEditListener, DisponibilidadDialogFragment.OnDisponibilidadListener {

    @BindView(R.id.reserva_esenas) LinearLayout esenas_frameLayout;
//    @BindView(R.id.editButton) Button editButton;
//    @BindView(R.id.bookButton) Button bookButton;
//    @BindView(R.id.deleteButton) Button deleteButton;
//    @BindView(R.id.checkInButton) Button checkInButton;
        @BindView(R.id.reserva_layout_base) BookingButtonBar bookingButtonBar;
    private List<Bedroom> bedrooms = new ArrayList<>();

    //escenas
    private ReservaEsenaPrincipal reservaEsenaPrincipal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserva_principal_activity);
        ButterKnife.bind(this);

        Toolbar toolbar = findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findById(this, R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findById(this, R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        loadData();
        configurarControles();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findById(this, R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void loadData() {
        EntityDataStore<Persistable> dataStore = DataStoreHolder.getInstance().getDataStore(this);
        bedrooms = dataStore.select(Bedroom.class).get().toList();
    }

    private void configurarControles() {
        reservaEsenaPrincipal = new ReservaEsenaPrincipal(this);
        esenas_frameLayout.addView(reservaEsenaPrincipal);
        bookingButtonBar.setReservaPrincipal(this);
    }


    public void click_fisicaR() {
        reservaEsenaPrincipal.click_fisicaR();
    }

    public void clickEliminarR() {
        reservaEsenaPrincipal.clickEliminarR();
    }

    public void clickEditR() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("edit_booking_dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        EditBookingDialogFragment newFragment = EditBookingDialogFragment.newInstance(
                getReservaEsenaPrincipal().getReservaPanelHabitacionActual().getPreReservaSelecc().getId());
        newFragment.show(ft, "edit_booking_dialog");
    }

    public void showDisponibilidad() {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("show_disponibilidad");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        VistaDia primerDiaSelec = getReservaEsenaPrincipal().getReservaPanelHabitacionActual().getPrimerDiaSelec();
        VistaDia segundoDiaSelec = getReservaEsenaPrincipal().getReservaPanelHabitacionActual().getSegundoDiaSelec();
        DisponibilidadDialogFragment newFragment = DisponibilidadDialogFragment.newInstance(primerDiaSelec.getCalendar(),segundoDiaSelec.getCalendar());
        newFragment.show(ft, "show_disponibilidad");
    }

    public List<Bedroom> obtenerDisponibilidad(LocalDate dia1, LocalDate dia2){
        List<Bedroom> bedrooms = DataStoreHolder.getInstance().getDataStore(this).select(Bedroom.class).get().toList();
        return  bedrooms;
    }

    @Override
    public void actualizarSeleccionEnCalendaio(LocalDate dia1, LocalDate dia2) {
        ReservaPanelHabitacion panelHabitacionActual = reservaEsenaPrincipal.getReservaPanelHabitacionActual();
        panelHabitacionActual.actualizarColorRangoModoTodos(panelHabitacionActual.obtenerVistaDiaFict(dia1), panelHabitacionActual.obtenerVistaDiaFict(dia2), CalendarState.UNSELECTED.color());;
    }

    public void clickPreR() {
        DialogoHacerPreReserva dialog = new DialogoHacerPreReserva(this);
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reserva_todos, menu);

        for (int i = 0; i < bedrooms.size(); i++) {
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

        if (id != R.id.show_m) {
            if (id == R.id.all_m) {  // todas las bedrooms
                reservaEsenaPrincipal.getReservaPanelHabitacionActual().setHabitacion(null);
                reservaEsenaPrincipal.getReservaPanelHabitacionActual().actualizarCambioHabitacion();
                reservaEsenaPrincipal.getReservaPanelHabitacionActual().limpiarTodo();
            } else {
                reservaEsenaPrincipal.getReservaPanelHabitacionActual().setHabitacion(bedrooms.get(item.getItemId()));
                reservaEsenaPrincipal.getReservaPanelHabitacionActual().actualizarCambioHabitacion();
                reservaEsenaPrincipal.getReservaPanelHabitacionActual().limpiarTodo();
            }
            ActionMenuItemView item1 = findById(this, R.id.show_m);
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
                startActivity(new Intent(this, InternalServiceListActivity.class));
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
            case R.id.nav_gestor:
                startActivity(new Intent(this, HostelActivity.class));
        }

        DrawerLayout drawer = findById(this, R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public ReservaEsenaPrincipal getReservaEsenaPrincipal() {
        return reservaEsenaPrincipal;
    }


    public List<Bedroom> getBedrooms() {
        return bedrooms;
    }

    public BookingButtonBar getBookingButtonBar() {
        return bookingButtonBar;
    }

    @Override
    public void onBookingEdit(Booking oldBooking, Booking newBooking) {
        ReservaPanelHabitacion reservaPanelHabitacionActual = getReservaEsenaPrincipal().getReservaPanelHabitacionActual();
        VistaDia vistaDiaFictIni = reservaPanelHabitacionActual.obtenerVistaDiaFict(oldBooking.getCheckInDate());
        VistaDia vistaDiaFictFin = reservaPanelHabitacionActual.obtenerVistaDiaFict(oldBooking.getCheckOutDate());
        VistaDia vistaDiaFictIniNew = reservaPanelHabitacionActual.obtenerVistaDiaFict(newBooking.getCheckInDate());
        VistaDia vistaDiaFictFinNew = reservaPanelHabitacionActual.obtenerVistaDiaFict(newBooking.getCheckOutDate());


        reservaPanelHabitacionActual.actualizarColorRangoModoH(vistaDiaFictIni, vistaDiaFictFin, CalendarState.EMPTY.color());
        reservaPanelHabitacionActual.actualizarColorRangoModoH(vistaDiaFictIniNew, vistaDiaFictFinNew, toCalendarState(newBooking.getState()).color());
        reservaPanelHabitacionActual.limpiarTodo();
    }
}
