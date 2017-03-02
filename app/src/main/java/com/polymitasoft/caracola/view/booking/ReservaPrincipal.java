package com.polymitasoft.caracola.view.booking;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.view.menu.ActionMenuItemView;
import android.view.Menu;
import android.view.MenuItem;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.components.DrawerActivity;
import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.datamodel.Bedroom;
import com.polymitasoft.caracola.datamodel.Booking;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

import static butterknife.ButterKnife.findById;
import static com.polymitasoft.caracola.view.booking.CalendarState.toCalendarState;

/**
 * @author yanier.alfonso
 */
public class ReservaPrincipal extends DrawerActivity
        implements NavigationView.OnNavigationItemSelectedListener, EditBookingDialogFragment.OnBookingEditListener, DisponibilidadDialogFragment.OnDisponibilidadListener {

    private List<Bedroom> bedrooms = new ArrayList<>();

    //escenas
    private ReservaEsenaPrincipal reservaEsenaPrincipal;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadData();
        configurarControles();
    }

    private void loadData() {
        EntityDataStore<Persistable> dataStore = DataStoreHolder.getInstance().getDataStore(this);
        bedrooms = dataStore.select(Bedroom.class).orderBy(Bedroom.NAME).get().toList();
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
        DisponibilidadDialogFragment newFragment = DisponibilidadDialogFragment.newInstance(primerDiaSelec.getCalendar(), segundoDiaSelec.getCalendar());
        newFragment.show(ft, "show_disponibilidad");
    }

    public List<Bedroom> obtenerDisponibilidad(LocalDate dia1, LocalDate dia2) {
        List<Bedroom> bedrooms = getReservaEsenaPrincipal().getReservaPanelHabitacionActual().disponibilidad(dia1, dia2);
        return bedrooms;
    }

    @Override
    public void actualizarSeleccionEnCalendaio(LocalDate dia1, LocalDate dia2) {
        ReservaPanelHabitacion panelHabitacionActual = reservaEsenaPrincipal.getReservaPanelHabitacionActual();
        panelHabitacionActual.actualizarColorRangoModoTodos(panelHabitacionActual.obtenerVistaDiaFict(dia1), panelHabitacionActual.obtenerVistaDiaFict(dia2), CalendarState.UNSELECTED.color());;
    }

    @Override
    public void clickEnListaDisponibilidad(LocalDate dia1, LocalDate dia2, Bedroom bedroom) {
        ReservaPanelHabitacion panelHabitacionActual = reservaEsenaPrincipal.getReservaPanelHabitacionActual();
//        panelHabitacionActual.setPrimerDiaSelec(panelHabitacionActual.obtenerVistaDiaFict(dia1));
//        panelHabitacionActual.setSegundoDiaSelec(panelHabitacionActual.obtenerVistaDiaFict(dia2));
        DialogoHacerPreReserva dialog = new DialogoHacerPreReserva(this, bedroom, panelHabitacionActual.obtenerVistaDiaFict(dia1), panelHabitacionActual.obtenerVistaDiaFict(dia2), true);
        dialog.show();
    }

    public void clickPreR() {
        ReservaPanelHabitacion panelHabitacionActual = reservaEsenaPrincipal.getReservaPanelHabitacionActual();
        DialogoHacerPreReserva dialog = new DialogoHacerPreReserva(this, (Bedroom) panelHabitacionActual.getHabitacion(), panelHabitacionActual.getPrimerDiaSelec(), panelHabitacionActual.getSegundoDiaSelec(), false);
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reserva_todos, menu);
        this.menu = menu;
        for (int i = 0; i < bedrooms.size(); i++) {
            menu.add(0, i, 0, bedrooms.get(i).getName());
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specFify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id != R.id.show_m) {
            if (id == R.id.all_m) {  // todas las bedrooms
                reservaEsenaPrincipal.getReservaPanelHabitacionActual().setHabitacion(null);
                reservaEsenaPrincipal.getReservaPanelHabitacionActual().actualizarCambioHabitacion();
                reservaEsenaPrincipal.getReservaPanelHabitacionActual().limpiarTodo();
            } else {
                reservaEsenaPrincipal.getReservaPanelHabitacionActual().setHabitacion(bedrooms.get(id));
                reservaEsenaPrincipal.getReservaPanelHabitacionActual().actualizarCambioHabitacion();
                reservaEsenaPrincipal.getReservaPanelHabitacionActual().limpiarTodo();
            }
            ActionMenuItemView item1 = findById(this, R.id.show_m);
            item1.setTitle(item.getTitle().toString());
        }
        return super.onOptionsItemSelected(item);
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

    public void actualizarMenu(){
        EntityDataStore<Persistable> dataStore = DataStoreHolder.getInstance().getDataStore(this);
        if (menu != null) {
            for (int i = 0; i < bedrooms.size(); i++) {
                menu.removeItem(i);
            }
            bedrooms = dataStore.select(Bedroom.class).orderBy(Bedroom.NAME).get().toList();
            for (int i = 0; i < bedrooms.size(); i++) {
                Bedroom bedroom = bedrooms.get(i);
                menu.add(0, i, 0, bedroom.getName());
            }
            Bedroom habitacion = (Bedroom) reservaEsenaPrincipal.getReservaPanelHabitacionActual().getHabitacion();
            ActionMenuItemView item1 = findById(this, R.id.show_m);
            if(habitacion == null){
                item1.setTitle("Disponibilidad");
            }else{
                Bedroom actual = dataStore.select(Bedroom.class).where(Bedroom.ID.equal(habitacion.getId())).get().firstOrNull();
                if(actual == null){
                    item1.setTitle("Disponibilidad");
                    reservaEsenaPrincipal.getReservaPanelHabitacionActual().setHabitacion(null);
                }else{
                    item1.setTitle(habitacion.getName());
                }
            }
            reservaEsenaPrincipal.getReservaPanelHabitacionActual().actualizarCambioHabitacion();
        }
    }

    private void refrescarCache() {
        if(reservaEsenaPrincipal != null){
            if(reservaEsenaPrincipal.getReservaPanelHabitacionActual() != null){
                reservaEsenaPrincipal.getReservaPanelHabitacionActual().refrescarCache();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        actualizarMenu();
        refrescarCache();
    }


}
