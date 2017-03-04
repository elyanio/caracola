package com.polymitasoft.caracola.view.booking;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.view.menu.ActionMenuItemView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.polymitasoft.caracola.CaracolaApplication;
import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.components.DrawerActivity;
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

    private static final String EDIT_BOOKING_DIALOG_TAG = "EDIT_BOOKING_DIALOG_TAG";
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
        EntityDataStore<Persistable> dataStore = CaracolaApplication.instance().getDataStore();
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
        EditBookingDialogFragment newFragment = EditBookingDialogFragment.newInstance(
                getReservaEsenaPrincipal().getReservaPanelHabitacionActual().getPreReservaSelecc());
        showDialog(newFragment, EDIT_BOOKING_DIALOG_TAG);
    }

    private void showDialog(DialogFragment dialogFragment, String tag) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag(tag);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        dialogFragment.show(ft, tag);
    }

    public void showDisponibilidad() {
        VistaDia primerDiaSelec = getReservaEsenaPrincipal().getReservaPanelHabitacionActual().getPrimerDiaSelec();
        VistaDia segundoDiaSelec = getReservaEsenaPrincipal().getReservaPanelHabitacionActual().getSegundoDiaSelec();
        DisponibilidadDialogFragment newFragment = DisponibilidadDialogFragment.newInstance(primerDiaSelec.getCalendar(), segundoDiaSelec.getCalendar());
        showDialog(newFragment, "show_disponibilidad");
    }

    public List<Bedroom> obtenerDisponibilidad(LocalDate dia1, LocalDate dia2) {
        return getReservaEsenaPrincipal().getReservaPanelHabitacionActual().disponibilidad(dia1, dia2);
    }

    @Override
    public void actualizarSeleccionEnCalendaio(LocalDate dia1, LocalDate dia2) {
        ReservaPanelHabitacion panelHabitacionActual = reservaEsenaPrincipal.getReservaPanelHabitacionActual();
        panelHabitacionActual.actualizarColorRangoModoTodos(panelHabitacionActual.obtenerVistaDiaFict(dia1), panelHabitacionActual.obtenerVistaDiaFict(dia2), CalendarState.UNSELECTED.color());
    }

    @Override
    public void clickEnListaDisponibilidad(LocalDate dia1, LocalDate dia2, Bedroom bedroom) {
        showEditDialog(bedroom, dia1, dia2);
    }

    public void clickPreR() {
        ReservaPanelHabitacion panelHabitacionActual = reservaEsenaPrincipal.getReservaPanelHabitacionActual();
        showEditDialog(panelHabitacionActual.getHabitacion(),
                panelHabitacionActual.getPrimerDiaSelec().getCalendar(),
                panelHabitacionActual.getSegundoDiaSelec().getCalendar());
    }

    private void showEditDialog(Bedroom bedroom, LocalDate day1, LocalDate day2) {
        LocalDate checkInDate = day1;
        LocalDate checkOutDate = day2;
        if(day1.isAfter(day2)) {
            checkInDate = day2;
            checkOutDate = day1;
        }
        EditBookingDialogFragment dialog = EditBookingDialogFragment.newInstance(bedroom, checkInDate, checkOutDate);
        showDialog(dialog, EDIT_BOOKING_DIALOG_TAG);
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
        ReservaPanelHabitacion bedroomPanel = getReservaEsenaPrincipal().getReservaPanelHabitacionActual();
        VistaDia vistaDiaFictIni = bedroomPanel.obtenerVistaDiaFict(oldBooking.getCheckInDate());
        VistaDia vistaDiaFictFin = bedroomPanel.obtenerVistaDiaFict(oldBooking.getCheckOutDate());
        VistaDia vistaDiaFictIniNew = bedroomPanel.obtenerVistaDiaFict(newBooking.getCheckInDate());
        VistaDia vistaDiaFictFinNew = bedroomPanel.obtenerVistaDiaFict(newBooking.getCheckOutDate());

        bedroomPanel.actualizarColorRangoModoH(vistaDiaFictIni, vistaDiaFictFin, CalendarState.EMPTY.color());
        bedroomPanel.actualizarColorRangoModoH(vistaDiaFictIniNew, vistaDiaFictFinNew, toCalendarState(newBooking.getState()).color());
        bedroomPanel.limpiarTodo();
    }

    @Override
    public void onBookingCreate(Booking newBooking) {
        ReservaPanelHabitacion bedroomPanel = getReservaEsenaPrincipal().getReservaPanelHabitacionActual();
        bedroomPanel.adicionarCalendarioReservaAMeses(newBooking);
        if(getReservaEsenaPrincipal().getCurrentPosition() != 0) {
            VistaDia primerDiaSelec = bedroomPanel.getPrimerDiaSelec();
            VistaDia segundoDiaSelec = bedroomPanel.getSegundoDiaSelec();
            bedroomPanel.actualizarColorRangoModoH(primerDiaSelec, segundoDiaSelec, toCalendarState(newBooking.getState()).color());
            bedroomPanel.limpiarTodo();
        } else {
            bedroomPanel.actualizarCambioHabitacion();
        }
    }

    public void actualizarMenu(){
        EntityDataStore<Persistable> dataStore = CaracolaApplication.instance().getDataStore();
        if (menu != null) {
            for (int i = 0; i < bedrooms.size(); i++) {
                menu.removeItem(i);
            }
            bedrooms = dataStore.select(Bedroom.class).orderBy(Bedroom.NAME).get().toList();
            for (int i = 0; i < bedrooms.size(); i++) {
                Bedroom bedroom = bedrooms.get(i);
                menu.add(0, i, 0, bedroom.getName());
            }
            Bedroom habitacion = reservaEsenaPrincipal.getReservaPanelHabitacionActual().getHabitacion();
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
