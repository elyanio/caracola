package com.polymitasoft.caracola.view.booking;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.polymitasoft.caracola.DataStoreHolder;
import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.components.InteractivoScrollView;
import com.polymitasoft.caracola.datamodel.Bedroom;
import com.polymitasoft.caracola.datamodel.Booking;
import com.polymitasoft.caracola.datamodel.BookingState;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;


public class ReservaPanelHabitacion extends LinearLayout {
    //contexto casteado
    private final ReservaPrincipal reservaPrincipal;

    private Bedroom habitacion;
    private VistaDia primerDiaSelec = null;
    private VistaDia segundoDiaSelec = null;
    private Booking preReservaSelecc = null;
    private List<VistaMes> meses;
    private EntityDataStore<Persistable> dataStore;

    //controles
    private LinearLayout linearLayoutMeses;
    private InteractivoScrollView scroll_meses;

    public ReservaPanelHabitacion(Context context, Bedroom bedroom) {
        super(context);
        dataStore = DataStoreHolder.getInstance().getDataStore(getContext());
        inicializar();
        this.habitacion = bedroom;
        reservaPrincipal = (ReservaPrincipal) getContext();
        obtenerControles();

        crearMeses();
        configurarControles();
    }

    private void inicializar() {
        String infladorServicio = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater asioInflador = (LayoutInflater) getContext().getSystemService(infladorServicio);
        asioInflador.inflate(R.layout.reserva_principal_panel_habitacion, this, true);
    }

    private void obtenerControles() {
        linearLayoutMeses = (LinearLayout) findViewById(R.id.reserva_listView_meses);
        scroll_meses = (InteractivoScrollView) findViewById(R.id.reserva_principal_scrollMeses);
    }

    private void configurarControles() {
        scroll_meses.setOnBottomReachedListener(new cargarNuevoMesPrincipal());
        scroll_meses.moverScrollMesActual();
        scroll_meses.scrollTo(0,1000);
    }

    public void crearMeses() {
        meses = new ArrayList<>(12);
        LocalDate today = LocalDate.now();
        int mes_actual = today.getMonthValue();

        VistaMes mes;
        for (int i = mes_actual; i < mes_actual + ReservaEsenaPrincipal.CANTIDAD_MESES_CARGADOS; i++) {
            LocalDate date = LocalDate.of(today.getYear(), i, 1);
            mes = new VistaMes(getContext(), this, date);
            linearLayoutMeses.addView(mes);
            meses.add(mes);
        }
    }

    public void click_fisicaR() {
//        Intent intent = new Intent(getContext(), Reserva_Fisica.class);
//        intent.putExtra(Habitacion.NOMBRE_TABLA, habitacion.getId_auto());
//
//        if (preReservaSelecc == null) {
//            intent.putExtra(Reserva.FECHA_INICIO, primerDiaSelec.getCalendar().getTimeInMillis());
//            intent.putExtra(Reserva.FECHA_FIN, segundoDiaSelec.getCalendar().getTimeInMillis());
//            intent.putExtra(Reserva_Fisica.PRERESERVA, Reserva_Fisica.DE_RESERVA);
//        } else {
//            intent.putExtra(Reserva.FECHA_INICIO, preReservaSelecc.getFecha_inicio().getTime());
//            intent.putExtra(Reserva.FECHA_FIN, preReservaSelecc.getFecha_inicio().getTime());
//            intent.putExtra(Reserva_Fisica.PRERESERVA, Reserva_Fisica.DE_PRERESERVA);
//        }
//        getContext().startActivity(intent);
    }

    public void clickEliminarR() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Eliminar Reserva");
        dialog.setMessage("¿Seguro desea eliminar la reserva seleccionada?");
        dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dataStore.delete(preReservaSelecc);
                VistaDia vistaDiaFictIni = obtenerVistaDiaFict(preReservaSelecc.getCheckInDate());
                VistaDia vistaDiaFictFin = obtenerVistaDiaFict(preReservaSelecc.getCheckOutDate());
                actualizarColorRangoModoH(vistaDiaFictIni, vistaDiaFictFin, CalendarState.EMPTY.color());
                eliminarCalendarioReservaAMeses(preReservaSelecc);
                limpiarTodo();
                dialog.cancel();
            }
        });
        dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public boolean esModoTodo() {
        return habitacion == null;
    }

    public void clickDia(VistaDia dia) {
        if (esModoTodo()) { // el habitacionSeleccionada es todos
            reservaPrincipal.disableButtons();

        } else {            // si hay una habitacion seleccionada
            reservaPrincipal.disableButtons();
            limpiarUltimaSeleccion(); // si hubo una seleccion valida y no se prereservo
            if (primerDiaSelec == null) {          //y si no hay una primera seleccion
                primerDiaSelec = dia;
                if (primerDiaSelec.getColor() == CalendarState.CONFIRMED.color() || primerDiaSelec.getColor() == CalendarState.PENDING.color()) { // y si ese cuarto ya tiene reserva
                    preReservaSelecc = obtenerReservaModoH(primerDiaSelec);
                    seleccionadorDeReservaModoH(preReservaSelecc, CalendarState.SELECTED.color());
                    if (estaElDiaHoyEnReserva(preReservaSelecc)) {                                     // si es el dia de hoy
                        //mostrar un botoncito para si quiere hacer reserva fisica de la prereseva marcada o seleccionar la reserva marcada
                        reservaPrincipal.enableCheckIn();
                    }
                    animarprueba(1);
                    reservaPrincipal.enableEdit();
                    reservaPrincipal.enableDelete();
                } else {                                                                   // no es hay reserva
                    primerDiaSelec.seleccionar(CellLocation.ALONE);
                    preReservaSelecc = null;
                }
            } else {                                // si ya hay una seleccion
                // aqui no es necesario seleccionar reserva q lo hubiera hecho en el toque anterior,ah no tengo culpa
                segundoDiaSelec = dia;
                if (primerDiaSelec.getColor() == CalendarState.CONFIRMED.color() || primerDiaSelec.getColor() == CalendarState.PENDING.color()) { // si la primera seleccion esta en una reserva
                    Booking calendario_reserva = obtenerReservaModoH(primerDiaSelec);
                    seleccionadorDeReservaModoH(calendario_reserva, CalendarState.UNSELECTED.color()); //deseleccionar reserva anteriior
                    if (segundoDiaSelec.getColor() != CalendarState.CONFIRMED.color() && segundoDiaSelec.getColor() != CalendarState.PENDING.color()) { // y la segunda no tiene reserv
                        segundoDiaSelec.seleccionar(CellLocation.ALONE);
                        preReservaSelecc = null;
                        animarprueba(0);
                    } else {  //la segunda tiene reserva
                        preReservaSelecc = obtenerReservaModoH(segundoDiaSelec);
                        seleccionadorDeReservaModoH(preReservaSelecc, CalendarState.SELECTED.color()); //seleccionar
                        reservaPrincipal.enableEdit();
                        reservaPrincipal.enableDelete();
                        if (estaElDiaHoyEnReserva(preReservaSelecc)) {                                     // si es el dia de hoy
                            //mostrar un botoncito para si quiere hacer reserva fisica de la prereseva marcada o seleccionar la reserva marcada
                            reservaPrincipal.enableCheckIn();
                        }
                    }
                    primerDiaSelec = segundoDiaSelec;
                    segundoDiaSelec = null;
                } else {                                       // la primera seleccion no es una reserva
                    switch (sePuedeReservarModoH(primerDiaSelec, segundoDiaSelec)) {
                        case -1: {  // no se puede reservar
                            if (segundoDiaSelec.getColor() == CalendarState.CONFIRMED.color() || segundoDiaSelec.getColor() == CalendarState.PENDING.color()) {
                                preReservaSelecc = obtenerReservaModoH(segundoDiaSelec);
                                seleccionadorDeReservaModoH(preReservaSelecc, CalendarState.SELECTED.color());
                                if (estaElDiaHoyEnReserva(preReservaSelecc)) {                                     // si es el dia de hoy
                                    //mostrar un botoncito para si quiere hacer reserva fisica de la prereseva marcada o seleccionar la reserva marcada
                                    reservaPrincipal.enableCheckIn();
                                }
                                animarprueba(1);
                                primerDiaSelec.deSeleccionar();
                                primerDiaSelec = segundoDiaSelec;
                                segundoDiaSelec = null;
                                reservaPrincipal.enableEdit();
                                reservaPrincipal.enableDelete();
                            } else {
                                Toast toast = Toast.makeText(getContext(), "no se puede seleccionar", Toast.LENGTH_SHORT);
                                toast.show();
                                primerDiaSelec.deSeleccionar();
                                primerDiaSelec = null;
                                segundoDiaSelec = null;
                                preReservaSelecc = null;
                            }
                            break;
                        }
                        case 0: {  //solo son pendientes
                            //                            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                            //                            dialog.setTitle("Eliminar Reserva");
                            //                            dialog.setMessage("¿Seguro desea eliminar la reserva seleccionada?");
                            //                            dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            //                                public void onClick(DialogInterface dialog, int which) {
                            //                                    if (estaElDiaHoyEnRango(primerDiaSelec, segundoDiaSelec)) {                                     // si es el dia de hoy
                            //                                        //mostrar un botoncito para si quiere hacer reserva fisica de la prereseva marcada o seleccionar la reserva marcada
                            //                                        bt_fisicaR.setEnabled(true);
                            //                                    }
                            //                                    calendario_reserva_dao.eliminar(preReservaSelecc);
                            //                                    VistaDia vistaDiaFictIni = obtenerVistaDiaFict(preReservaSelecc.getFecha_inicio());
                            //                                    VistaDia vistaDiaFictFin = obtenerVistaDiaFict(preReservaSelecc.getFecha_fin());
                            //                                    actualizarColorRangoModoH(vistaDiaFictIni, vistaDiaFictFin, CalendarState.EMPTY.color());
                            //                                    eliminarCalendarioReservaAMeses(preReservaSelecc);
                            //                                    dialog.cancel();
                            //                                }
                            //                            });
                            //                            dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            //                                public void onClick(DialogInterface dialog, int which) {
                            //                                    dialog.cancel();
                            //                                }
                            //                            });
                            //                            dialog.show();
                            if (segundoDiaSelec.getColor() == CalendarState.CONFIRMED.color() || segundoDiaSelec.getColor() == CalendarState.PENDING.color()) {
                                //                                no funciona el seleccionR UN PENDIENTE COMO SEGUNDO ELEMENTO
                                preReservaSelecc = obtenerReservaModoH(segundoDiaSelec);
                                seleccionadorDeReservaModoH(preReservaSelecc, CalendarState.SELECTED.color());
                                if (estaElDiaHoyEnReserva(preReservaSelecc)) {                                     // si es el dia de hoy
                                    //mostrar un botoncito para si quiere hacer reserva fisica de la prereseva marcada o seleccionar la reserva marcada
                                    reservaPrincipal.enableCheckIn();
                                }
                                animarprueba(1);
                                primerDiaSelec.deSeleccionar();
                                primerDiaSelec = segundoDiaSelec;
                                segundoDiaSelec = null;
                                reservaPrincipal.enableEdit();
                                reservaPrincipal.enableDelete();
                            } else {
                                Toast toast = Toast.makeText(getContext(), "no se puede seleccionar", Toast.LENGTH_SHORT);
                                toast.show();
                                primerDiaSelec.deSeleccionar();
                                primerDiaSelec = null;
                                segundoDiaSelec = null;
                                preReservaSelecc = null;
                            }
                            break;
                        }
                        case 1: { // se puede reservar
                            selecionadorRangoDiasTocadosModoH(CalendarState.SELECTED.color());
                            if (estaElDiaHoyEnRango(primerDiaSelec, segundoDiaSelec)) {                                     // si es el dia de hoy
                                //mostrar un botoncito para si quiere hacer reserva fisica de la prereseva marcada o seleccionar la reserva marcada
                                reservaPrincipal.enableCheckIn();
                            }
                            reservaPrincipal.enableBook();
                            preReservaSelecc = null;
                            break;
                        }
                    }
                }
            }
        }
    }

    /*-1 si no se puede reservar, 0 si solo existen pendientes, 1 si se puede reservar*/
    private int sePuedeReservarModoH(VistaDia dia1, VistaDia dia2) {
        VistaDia diaMenor = dia1;
        VistaDia diaMayor = dia2;
        boolean hayPendientes = false;
        int temp;
        // para si se seleciona de atras pa alante
        if (dia1.getCalendar().isAfter(dia2.getCalendar())) {
            diaMenor = dia2;
            diaMayor = dia1;
        }
        if (VistaDia.mismoMes(diaMenor, diaMayor)) {
//            Log.e("dia menor pa busc mes", " anno" + diaMenor.getCalendar().get(Calendar.YEAR) + " mes " + diaMenor.getCalendar().get(Calendar.MONTH) + " dia " + diaMenor.getCalendar().get(Calendar.DAY_OF_MONTH));
            VistaMes actualMes = mesDelDia(diaMenor);
            return actualMes.sePuedeReservarModoHabitacion(diaMenor, diaMayor);
        } else {
            VistaMes mesMenor = mesDelDia(diaMenor);
            VistaDia fictDiaFinal = obtenerDiaFinalMes(diaMenor.getCalendar());
            temp = mesMenor.sePuedeReservarModoHabitacion(diaMenor, fictDiaFinal);
            if (temp == -1) {
                return -1;
            } else if (temp == 0) {
                hayPendientes = true;
            }

            // se puede reservar mes mayor
            VistaMes mesMayor = mesDelDia(diaMayor);
            VistaDia fictDiaIni = obtenerDiaIniMes(diaMayor.getCalendar());
            temp = mesMayor.sePuedeReservarModoHabitacion(fictDiaIni, diaMayor);
            if (temp == -1) {
                return -1;
            } else if (temp == 0) {
                hayPendientes = true;
            }

            // se puede reservar meses intermedios
            int indexMesMenor = meses.indexOf(mesMenor);
            int indexMesMayor = meses.indexOf(mesMayor);
            for (int i = indexMesMenor + 1; i < indexMesMayor; i++) {
                VistaMes mesActual = meses.get(i);
                VistaDia fictDiaInii = obtenerDiaIniMes(mesActual.getInicio_mes());
                VistaDia fictDiaFinali = obtenerDiaFinalMes(mesActual.getInicio_mes());
                temp = mesActual.sePuedeReservarModoHabitacion(fictDiaInii, fictDiaFinali);
                if (temp == -1) {
                    return -1;
                } else if (temp == 0) {
                    hayPendientes = true;
                }
            }
            // decicion final
            return (hayPendientes ? 0 : 1);
        }
    }

    public void actualizarColorRangoModoH(VistaDia dia1, VistaDia dia2, int color) {
        VistaDia diaMenor = dia1;
        VistaDia diaMayor = dia2;
        // para si se seleciona de atras pa alante
        if (dia1.getCalendar().isAfter(dia2.getCalendar())) {
            diaMenor = dia2;
            diaMayor = dia1;
        }

        if (VistaDia.mismoMes(diaMenor, diaMayor)) {
            VistaMes actualMes = mesDelDia(diaMenor);
            if (color == CalendarState.SELECTED.color() || color == CalendarState.UNSELECTED.color()) {
                actualMes.seleccionadorRangoModoH(diaMenor, diaMayor, color, true, true);
            } else {
                actualMes.actualizarColorRangoModoH(diaMenor, diaMayor, color, true, true);
            }
        } else {
            //pintar mes menor
            VistaMes mesMenor = mesDelDia(diaMenor);
            VistaDia fictDiaFinal = obtenerDiaFinalMes(diaMenor.getCalendar());
            if (color == CalendarState.SELECTED.color() || color == CalendarState.UNSELECTED.color()) {
                mesMenor.seleccionadorRangoModoH(diaMenor, fictDiaFinal, color, true, false);
            } else {
                mesMenor.actualizarColorRangoModoH(diaMenor, fictDiaFinal, color, true, false);
            }

            // pintar mes mayor
            VistaMes mesMayor = mesDelDia(diaMayor);
            VistaDia fictDiaIni = obtenerDiaIniMes(diaMayor.getCalendar());
            if (color == CalendarState.SELECTED.color() || color == CalendarState.UNSELECTED.color()) {
                mesMayor.seleccionadorRangoModoH(fictDiaIni, diaMayor, color, false, true);
            } else {
                mesMayor.actualizarColorRangoModoH(fictDiaIni, diaMayor, color, false, true);
            }

            // pintar meses
            int indexMesMenor = meses.indexOf(mesMenor);
            int indexMesMayor = meses.indexOf(mesMayor);
            for (int i = indexMesMenor + 1; i < indexMesMayor; i++) {
                VistaMes mesActual = meses.get(i);
                VistaDia fictDiaInii = obtenerDiaIniMes(mesActual.getInicio_mes());
                VistaDia fictDiaFinali = obtenerDiaFinalMes(mesActual.getInicio_mes());
                if (color == CalendarState.SELECTED.color() || color == CalendarState.UNSELECTED.color()) {
                    mesActual.seleccionadorRangoModoH(fictDiaInii, fictDiaFinali, color, false, false);
                } else {
                    mesActual.actualizarColorRangoModoH(fictDiaInii, fictDiaFinali, color, false, false);
                }
            }
        }
    }

    private void animarprueba(int modo) {
        if (modo == 0) { //encoger
            ViewPropertyAnimator vpa = reservaPrincipal.getReservaEsenaPrincipal().getLayoutCabecera().animate();
            vpa.scaleY(2);
            vpa.setDuration(500); //2 secs
            vpa.setInterpolator(new AccelerateDecelerateInterpolator());
            vpa.start();
        } else {  // alargar
            ViewPropertyAnimator vpa = reservaPrincipal.getReservaEsenaPrincipal().getLayoutCabecera().animate();
            vpa.scaleY(20);
            vpa.setDuration(500); //2 secs
            vpa.setInterpolator(new AccelerateDecelerateInterpolator());
            vpa.start();
        }
    }

    public void eliminarCalendarioReservaAMeses(Booking calendario_reserva) {
        VistaDia fechaIniC = obtenerVistaDiaFict(calendario_reserva.getCheckInDate());
        VistaDia fechaFinC = obtenerVistaDiaFict(calendario_reserva.getCheckOutDate());
        VistaMes mesMenor = mesDelDia(fechaIniC);
        VistaMes mesMayor = mesDelDia(fechaFinC);
        int indexMesMenor = meses.indexOf(mesMenor);
        int indexMesMayor = meses.indexOf(mesMayor);
        for (int i = indexMesMenor; i <= indexMesMayor; i++) {
            VistaMes mesActual = meses.get(i);
            mesActual.getPreReservas().remove(calendario_reserva);
        }
    }

    public void seleccionadorDeReservaModoH(Booking calendario_reserva, int color) {
        VistaDia diaIni = obtenerVistaDiaFict(calendario_reserva.getCheckInDate());
        VistaDia diaFin = obtenerVistaDiaFict(calendario_reserva.getCheckOutDate());
        ;
        actualizarColorRangoModoH(diaIni, diaFin, color);
    }

    public void selecionadorRangoDiasTocadosModoH(int color) {
        if (color == CalendarState.SELECTED.color()) {
            actualizarColorRangoModoH(primerDiaSelec, segundoDiaSelec, CalendarState.SELECTED.color());
        } else {
            actualizarColorRangoModoH(primerDiaSelec, segundoDiaSelec, CalendarState.UNSELECTED.color());
        }
    }

    public void salvarPreReservaYadicionarALosMeses(VistaDia primerDiaSelec, VistaDia diaFin, BookingState estado, String nota, BigDecimal price) {
        VistaDia diaMenor = primerDiaSelec;
        VistaDia diaMayor = diaFin;
        // para si se seleciona de atras pa alante
        if (primerDiaSelec.getCalendar().isAfter(diaFin.getCalendar())) {
            diaMenor = diaFin;
            diaMayor = primerDiaSelec;
        }

        Booking booking = new Booking();
        booking.setCheckInDate(diaMenor.getCalendar());
        booking.setCheckOutDate(diaMayor.getCalendar());
        booking.setState(estado);
        booking.setBedroom(habitacion);
        booking.setNote(nota);
        booking.setPrice(price);
        dataStore.insert(booking);

        // adicionar en los meses necesarios
        adicionarCalendarioReservaAMeses(booking);
    }

    public void adicionarCalendarioReservaAMeses(Booking calendario_reserva) {
        VistaDia fechaIniC = obtenerVistaDiaFict(calendario_reserva.getCheckInDate());
        VistaDia fechaFinC = obtenerVistaDiaFict(calendario_reserva.getCheckOutDate());
        VistaMes mesMenor = mesDelDia(fechaIniC);
        VistaMes mesMayor = mesDelDia(fechaFinC);
        int indexMesMenor = meses.indexOf(mesMenor);
        int indexMesMayor = meses.indexOf(mesMayor);
        for (int i = indexMesMenor; i <= indexMesMayor; i++) {
            VistaMes mesActual = meses.get(i);
            mesActual.getPreReservas().add(calendario_reserva);
        }
    }

    public void actualizarCambioHabitacion() {
        for (VistaMes mes : meses) {
            mes.actualizarCambioHabitacion();
        }
    }

    public boolean estaElDiaHoyEnReserva(Booking calendario_reserva) {
        LocalDate diaMenor = calendario_reserva.getCheckInDate();
        LocalDate diaMayor = calendario_reserva.getCheckOutDate();
        LocalDate hoy = LocalDate.now();
        if (diaMenor.compareTo(hoy) <= 0 && diaMayor.compareTo(hoy) >= 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean estaElDiaHoyEnRango(VistaDia dia1, VistaDia dia2) {
        VistaDia diaMenor = dia1;
        VistaDia diaMayor = dia2;
        // para si se seleciona de atras pa alante
        if (dia1.getCalendar().isAfter(dia2.getCalendar())) {
            diaMenor = dia2;
            diaMayor = dia1;
        }
        LocalDate hoy = LocalDate.now();
        if (diaMenor.getCalendar().compareTo(hoy) <= 0 && diaMayor.getCalendar().compareTo(hoy) >= 0) {
            return true;
        } else {
            return false;
        }
    }

    /*si no hay devuelve null*/
    public Booking obtenerReservaModoH(VistaDia dia) {
        VistaMes vistaMes = mesDelDia(dia);
        for (Booking calendario_reserva : vistaMes.getPreReservas()) {
            //            Log.e("mes", "dia " + calendario_reserva.getFecha_inicio().getDay());
            if (calendario_reserva.getCheckInDate().compareTo(dia.getCalendar()) <= 0 && calendario_reserva.getCheckOutDate().compareTo(dia.getCalendar()) >= 0 && calendario_reserva.getBedroom().getId() == habitacion.getId()) {
                return calendario_reserva;
            }
        }
        return null;
    }

    public void limpiarUltimaSeleccion() {
        if (primerDiaSelec != null && segundoDiaSelec != null) { // hay alguna seleccion
            selecionadorRangoDiasTocadosModoH(CalendarState.UNSELECTED.color());
            primerDiaSelec = null;
            segundoDiaSelec = null;
            preReservaSelecc = null;
        }
    }

    public void limpiarTodo() {
        reservaPrincipal.disableButtons();
        primerDiaSelec = null;
        segundoDiaSelec = null;
        preReservaSelecc = null;
        animarprueba(0);
    }

    public VistaMes mesDelDia(VistaDia dia) {
        for (VistaMes mes : meses) {
            if (mes.estaElDia(dia)) {
                return mes;
            }
        }
        return null;
    }

    public VistaDia obtenerDiaFinalMes(LocalDate dia) {
        return new VistaDia(getContext(), LocalDate.of(dia.getYear(), dia.getMonth(), dia.lengthOfMonth()), CalendarState.EMPTY.color());
    }

    public VistaDia obtenerDiaIniMes(LocalDate dia) {
        return new VistaDia(getContext(), LocalDate.of(dia.getYear(), dia.getMonth(), 1), CalendarState.EMPTY.color());
    }

    public VistaDia obtenerVistaDiaFict(LocalDate date) {
        return new VistaDia(getContext(), date, CalendarState.EMPTY.color());
    }

    public void crearNuevoMesArriba() {
        LocalDate ultimoMes = meses.get(0).getInicio_mes();
        LocalDate calendar = LocalDate.of(ultimoMes.getYear(), ultimoMes.getMonth(), 1).minusMonths(1);
        VistaMes mes = new VistaMes(getContext(), misma(), calendar);
        linearLayoutMeses.addView(mes,0);
        meses.add(0,mes);
    }

    public void crearNuevoMesAbajo() {
        LocalDate ultimoMes = meses.get(meses.size() - 1).getInicio_mes();
        LocalDate calendar = LocalDate.of(ultimoMes.getYear(), ultimoMes.getMonth(), 1).plusMonths(1);
        VistaMes mes = new VistaMes(getContext(), misma(), calendar);
        linearLayoutMeses.addView(mes);
        meses.add(mes);
    }

    public Booking getPreReservaSelecc() {
        return preReservaSelecc;
    }

    public VistaDia getPrimerDiaSelec() {
        return primerDiaSelec;
    }

    public VistaDia getSegundoDiaSelec() {
        return segundoDiaSelec;
    }

    public Bedroom getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(Bedroom habitacion) {
        this.habitacion = habitacion;
    }

    public ReservaPanelHabitacion misma() {
        return this;
    }

    public class cargarNuevoMesPrincipal implements InteractivoScrollView.CapturadorEventoMoverScroll {

        @Override
        public void onMover(int sentido) {
            if (sentido == SENTIDO_SCROLL_ABAJO) {
                crearNuevoMesAbajo();
            } else {
                crearNuevoMesArriba();
            }

        }
    }
}












