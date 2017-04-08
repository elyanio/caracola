package com.polymitasoft.caracola.view.booking;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.dataaccess.BookingDao;
import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.datamodel.Bedroom;
import com.polymitasoft.caracola.datamodel.Booking;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

import static com.polymitasoft.caracola.util.FormatUtils.capitalize;
import static com.polymitasoft.caracola.view.booking.CalendarState.EMPTY;
import static com.polymitasoft.caracola.view.booking.CalendarState.NO_DAY;
import static com.polymitasoft.caracola.view.booking.CalendarState.SELECTED;
import static com.polymitasoft.caracola.view.booking.CalendarState.toCalendarState;

public class VistaMes extends LinearLayout {
    private final ReservaPanelHabitacion reservaPanelHabitacion;
    private final LocalDate inicio_mes;
    private List<VistaDia> dias;
    private List<Booking> preReservas;

    private AdaptadorGridMes adaptadorGrid;
    private TextView textMes;
    private GridView gridVista;
    private int cantNoDias;
    private BookingDao bookingDao;

    public VistaMes(Context context, ReservaPanelHabitacion reservaPanelHabitacion, LocalDate inicio_mes) {
        super(context);
        this.reservaPanelHabitacion = reservaPanelHabitacion;
        this.inicio_mes = inicio_mes;
        EntityDataStore<Persistable> dataStore = DataStoreHolder.getInstance().getDataStore(getContext());
        bookingDao = new BookingDao(dataStore);
        inicializar();
        cargarCalendarioReservas();
        obtenerControles();
        crearDias();
        adaptadorGrid = new AdaptadorGridMes(context, android.R.layout.simple_list_item_1, dias);
        configurarControles();
    }

    private void inicializar() {
        String infladorServicio = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater asioInflador = (LayoutInflater) getContext().getSystemService(infladorServicio);
        asioInflador.inflate(R.layout.reserva_principal_elemento_lista_calendario, this, true);
    }

    private void obtenerControles() {
        textMes = (TextView) findViewById(R.id.reserva_text_mes);
        gridVista = (GridView) findViewById(R.id.reserva_gridView);
    }

    private void cargarCalendarioReservas() {
        LocalDate finMes = LocalDate.of(inicio_mes.getYear(), inicio_mes.getMonth(), inicio_mes.lengthOfMonth());
        preReservas = new ArrayList<>(bookingDao.bookingsBetween(inicio_mes, finMes));
    }

    private void crearDias() {
        int cant_dias_mes = inicio_mes.lengthOfMonth();
        VistaDia temp;
        //poner los no dias
        ponerNoDias();

        dias = new ArrayList<>(cantNoDias + cant_dias_mes);
        for (int i = 1; i <= cantNoDias; i++) {
            LocalDate defecto = LocalDate.of(2000, Month.JANUARY, i);
            dias.add(new VistaDia(getContext(), defecto, NO_DAY.color()));
        }
         DateTimeFormatter format = DateTimeFormatter.ofPattern("y/M/d");
        // poner los dias
        for (int i = 1; i <= cant_dias_mes; i++) {
            LocalDate dia = LocalDate.of(inicio_mes.getYear(), inicio_mes.getMonth(), i);
            String s = dia.format(format);
//            Log.e("dias",s);
            Pair<Integer, CellLocation> pair = obtenerColorAlcrear(dia);
            temp = new VistaDia(getContext(), dia, pair.first, pair.second);
//            Log.e("color",temp.getColor()+"");
            dias.add(temp);
            temp.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
    }

    public void refrescarCache(){
        cargarCalendarioReservas();
    }

    private void ponerNoDias() {
        DayOfWeek primerDiaSemana = DayOfWeek.MONDAY;
        DayOfWeek dia_semana = inicio_mes.getDayOfWeek();
        int cont = dia_semana.getValue() - primerDiaSemana.getValue();
        if (cont < 0) {
            cont += 7;
        }
        cantNoDias = cont;
        DateTimeFormatter format = DateTimeFormatter.ofPattern("y/M/d");
//        Log.e("dia",inicio_mes.format(format));
//        Log.e("cant no dias",cantNoDias + "");
    }

    public void actualizarCambioHabitacion() {
        refrescarCache();
        for (VistaDia dia : dias) {
            if (dia.getColor() != NO_DAY.color()) {
                Pair<Integer, CellLocation> pair = obtenerColorAlcrear(dia.getCalendar());
                dia.pintarColor(pair.first, pair.second);
            }
        }
    }

    private Pair<Integer, CellLocation> obtenerColorAlcrear(LocalDate dia) {
        if (reservaPanelHabitacion.esModoTodo()) {
            return new Pair<>(obtenerColorModoTodoALcrear(dia), CellLocation.MIDDLE);
        } else {
            for (Booking booking : preReservas) {
                Bedroom habitacion = booking.getBedroom();
                if (booking.getCheckInDate().compareTo(dia) <= 0 && booking.getCheckOutDate().compareTo(dia) >= 0 &&
                        habitacion.getId() == reservaPanelHabitacion.getHabitacion().getId()) {
                    CellLocation location;
                    if (booking.getCheckInDate().equals(booking.getCheckOutDate())) {
                        location = CellLocation.ALONE;
                    } else if (booking.getCheckInDate().equals(dia)) {
                        location = CellLocation.BEGINING;
                    } else if (booking.getCheckOutDate().equals(dia)) {
                        location = CellLocation.END;
                    } else {
                        location = CellLocation.MIDDLE;
                    }
                    return new Pair<>(toCalendarState(booking.getState()).color(), location);
                }
            }
            return new Pair<>(EMPTY.color(), CellLocation.MIDDLE);
        }
    }

    public int obtenerColorModoTodoALcrear(LocalDate dia) {
        int repitenciaDelDia = 0;
        ReservaPrincipal reservaPrincipal = (ReservaPrincipal) getContext();
        int cantCuarto = reservaPrincipal.getBedrooms().size();
        for (Booking calendario_reserva : preReservas) {
            if ((calendario_reserva.getCheckInDate()).compareTo(dia) <= 0 && (calendario_reserva.getCheckOutDate()).compareTo(dia) >= 0) {
                repitenciaDelDia++;
            }
        }

        if (repitenciaDelDia == 0) {
            return EMPTY.color();
        } else if (repitenciaDelDia < cantCuarto) {
            return CalendarState.PARTIALLY_OCCUPIED.color();
        } else {
            return CalendarState.OCUPPIED.color();
        }
    }

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM 'de' y");

    private void configurarControles() {
        gridVista.setAdapter(adaptadorGrid);
        textMes.setText(capitalize(inicio_mes.format(formatter)));
    }

    public boolean estaElDia(VistaDia vistaDia) {
        LocalDate date = vistaDia.getCalendar();
        return inicio_mes.getMonth() == date.getMonth() && inicio_mes.getYear() == date.getYear();
    }

    public boolean estaElDiaHoy() {
        return inicio_mes.isEqual(LocalDate.now());
    }

    public LocalDate getInicio_mes() {
        return inicio_mes;
    }

    public void actualizarColorRangoModoH(VistaDia diaMenor, VistaDia diaMayor, int color, boolean marcarIni, boolean marcarFin) {
        int indexIni = Collections.binarySearch(dias, diaMenor);
        int indexMayor = Collections.binarySearch(dias, diaMayor);
        if (indexIni == indexMayor) {
            if (marcarIni && marcarFin) {
                dias.get(indexIni).pintarColor(color, CellLocation.ALONE);
            } else if (marcarIni) {
                dias.get(indexIni).pintarColor(color, CellLocation.BEGINING);
            } else {
                dias.get(indexMayor).pintarColor(color, CellLocation.END);
            }
        } else {
            for (int i = indexIni; i <= indexMayor; i++) {
                if (i == indexIni && marcarIni) {
                    dias.get(i).pintarColor(color, CellLocation.BEGINING);
                } else if (i == indexMayor && marcarFin) {
                    dias.get(i).pintarColor(color, CellLocation.END);
                } else {
                    dias.get(i).pintarColor(color, CellLocation.MIDDLE);
                }
            }
        }
    }

    /*no actualiza el color en los dias solo los selecciona*/
    public void seleccionadorRangoModoH(VistaDia diaMenor, VistaDia diaMayor, int color, boolean marcarIni, boolean marcarFin) {
        int indexIni = Collections.binarySearch(dias, diaMenor);
        int indexMayor = Collections.binarySearch(dias, diaMayor);
        if (color == SELECTED.color()) {
            if (indexIni == indexMayor) {
                if (marcarIni && marcarFin) {
                    dias.get(indexIni).seleccionar(CellLocation.ALONE);
                } else if (marcarIni) {
                    dias.get(indexIni).seleccionar(CellLocation.BEGINING);
                } else {
                    dias.get(indexMayor).seleccionar(CellLocation.END);
                }
            } else {
                for (int i = indexIni; i <= indexMayor; i++) {
                    if (i == indexIni && marcarIni) {
                        dias.get(i).seleccionar(CellLocation.BEGINING);
                    } else if (i == indexMayor && marcarFin) {
                        dias.get(i).seleccionar(CellLocation.END);
                    } else {
                        dias.get(i).seleccionar(CellLocation.MIDDLE);
                    }

                }
            }

        } else {
            if (indexIni == indexMayor) {
                if (marcarIni && marcarFin) {
                    dias.get(indexIni).deSeleccionar(CellLocation.ALONE);
                } else if (marcarIni) {
                    dias.get(indexIni).deSeleccionar(CellLocation.BEGINING);
                } else {
                    dias.get(indexMayor).deSeleccionar(CellLocation.END);
                }
            } else {
                for (int i = indexIni; i <= indexMayor; i++) {
                    if (i == indexIni && marcarIni) {
                        dias.get(i).deSeleccionar(CellLocation.BEGINING);
                    } else if (i == indexMayor && marcarFin) {
                        dias.get(i).deSeleccionar(CellLocation.END);
                    } else {
                        dias.get(i).deSeleccionar(CellLocation.MIDDLE);
                    }
                }
            }
        }
    }

    /*-1 si no se puede reservar, 0 si solo existen pendientes, 1 si se puede reservar*/
    public int sePuedeReservarModoHabitacion(VistaDia diaMenor, VistaDia diaMayor) {
        int indexIni = Collections.binarySearch(dias, diaMenor);
        int indexMayor = Collections.binarySearch(dias, diaMayor);
        boolean hayPendiente = false;
        for (int i = indexIni; i <= indexMayor; i++) {
            if (dias.get(i).getColor() == CalendarState.CONFIRMED.color() || dias.get(i).getColor() == CalendarState.CHECKED_IN.color()) {
                return -1;
            }
            if (dias.get(i).getColor() == CalendarState.PENDING.color()) {
                hayPendiente = true;
            }
        }
        if (hayPendiente) {
            return 0;
        } else {
            return 1;
        }
    }

    public List<Booking> getPreReservas() {
        return preReservas;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof VistaMes && inicio_mes.equals(((VistaMes) o).getInicio_mes());
    }

    @Override
    public int hashCode() {
        return inicio_mes.hashCode();
    }

    public class AdaptadorGridMes extends ArrayAdapter<VistaDia> {

        int size;

        AdaptadorGridMes(Context context, int resource, List<VistaDia> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(final int posision, View convertView, @NonNull ViewGroup parent) {
            size = getWidth() / 7;
            VistaDia vistaDia = dias.get(posision);
            vistaDia.setLayoutParams(new GridView.LayoutParams(size, size));
            vistaDia.setGravity(Gravity.CENTER);
            return vistaDia;
        }

    }
}
