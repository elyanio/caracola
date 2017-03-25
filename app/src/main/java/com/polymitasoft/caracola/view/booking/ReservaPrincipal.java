package com.polymitasoft.caracola.view.booking;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.view.menu.ActionMenuItemView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.communication.ManageSmsBooking;
import com.polymitasoft.caracola.components.DrawerActivity;
import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.datamodel.Bedroom;
import com.polymitasoft.caracola.datamodel.Booking;
import com.polymitasoft.caracola.view.drm.CheckActivation;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
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
        init();
    }

    private void init() {
        loadData();
        configurarControles();
        Object restore =  getLastCustomNonConfigurationInstance();

        if (restore != null)
        {
            ReservaPanelHabitacion panelHabitacionActual = reservaEsenaPrincipal.getReservaPanelHabitacionActual();
            SalvarEstadoReservaPrincipal salvaActivity = (SalvarEstadoReservaPrincipal)restore;
            panelHabitacionActual.setHabitacion(salvaActivity.getHabitacion());
            panelHabitacionActual.setPrimerDiaSelec(salvaActivity.getPrimerDiaSelec());
            panelHabitacionActual.setSegundoDiaSelec(null);  //porq yo llamo a click dia aki mismo
            panelHabitacionActual.setPreReservaSelecc(salvaActivity.getPreReservaSelecc());
            panelHabitacionActual.setVisibleTextNota(salvaActivity.isVisibleTextNota());
            panelHabitacionActual.actualizarCambioHabitacion();
            panelHabitacionActual.animateNote(0, salvaActivity.getPreReservaSelecc());
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == ReservaPanelHabitacion.REQUEST_SEND_DELETE_SMS) {
            Booking booking = reservaEsenaPrincipal.getReservaPanelHabitacionActual().getPreReservaSelecc();
            new ManageSmsBooking(booking).sendDeleteMessage();
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void loadData() {
        EntityDataStore<Persistable> dataStore = DataStoreHolder.INSTANCE.getDataStore();
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
        MenuItem item1 = (MenuItem) menu.findItem(R.id.show_m);
        Bedroom habitacion = reservaEsenaPrincipal.getReservaPanelHabitacionActual().getHabitacion();
        if( habitacion == null){
            item1.setTitle("Disponibilidad");
        }else {
            item1.setTitle(habitacion.getName());
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
        EntityDataStore<Persistable> dataStore = DataStoreHolder.INSTANCE.getDataStore();
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
    public Object onRetainCustomNonConfigurationInstance(){
        ReservaPanelHabitacion panelHabitacionActual = reservaEsenaPrincipal.getReservaPanelHabitacionActual();
        SalvarEstadoReservaPrincipal salvarActivity = new SalvarEstadoReservaPrincipal();
        salvarActivity.setHabitacion(panelHabitacionActual.getHabitacion());
        salvarActivity.setPrimerDiaSelec(panelHabitacionActual.getPrimerDiaSelec());
        salvarActivity.setSegundoDiaSelec(panelHabitacionActual.getSegundoDiaSelec());
        salvarActivity.setPreReservaSelecc(panelHabitacionActual.getPreReservaSelecc());
        salvarActivity.setVisibleTextNota(panelHabitacionActual.getVisibleTextNota());
        return salvarActivity;
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkActivation();
        actualizarMenu();
        refrescarCache();
    }

    private void checkActivation() {
        new CheckActivation(this).execute();
    }

    class SalvarEstadoReservaPrincipal{
        private Bedroom habitacion = null;
        private VistaDia primerDiaSelec = null;
        private VistaDia segundoDiaSelec = null;
        private Booking preReservaSelecc = null;
        private boolean visibleTextNota = false;

        public SalvarEstadoReservaPrincipal() {
        }

        public Bedroom getHabitacion() {
            return habitacion;
        }

        public void setHabitacion(Bedroom habitacion) {
            this.habitacion = habitacion;
        }

        public VistaDia getPrimerDiaSelec() {
            return primerDiaSelec;
        }

        public void setPrimerDiaSelec(VistaDia primerDiaSelec) {
            this.primerDiaSelec = primerDiaSelec;
        }

        public VistaDia getSegundoDiaSelec() {
            return segundoDiaSelec;
        }

        public void setSegundoDiaSelec(VistaDia segundoDiaSelec) {
            this.segundoDiaSelec = segundoDiaSelec;
        }

        public Booking getPreReservaSelecc() {
            return preReservaSelecc;
        }

        public void setPreReservaSelecc(Booking preReservaSelecc) {
            this.preReservaSelecc = preReservaSelecc;
        }

        public boolean isVisibleTextNota() {
            return visibleTextNota;
        }

        public void setVisibleTextNota(boolean visibleTextNota) {
            this.visibleTextNota = visibleTextNota;
        }
    }
}
