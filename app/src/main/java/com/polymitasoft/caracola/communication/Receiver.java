package com.polymitasoft.caracola.communication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

import com.polymitasoft.caracola.CaracolaApplication;
import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.datamodel.Bedroom;
import com.polymitasoft.caracola.datamodel.Booking;
import com.polymitasoft.caracola.datamodel.BookingState;
import com.polymitasoft.caracola.datamodel.Hostel;
import com.polymitasoft.caracola.datamodel.LocalDateConverter;
import com.polymitasoft.caracola.datamodel.Manager;
import com.polymitasoft.caracola.notification.StateBar;
import com.polymitasoft.caracola.util.FormatUtils;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.math.BigDecimal;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

/**
 * Created by asio on 2/24/2017.
 */

public class Receiver extends BroadcastReceiver {

    private static final String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private Intent intent;

    @Override
    public void onReceive(Context context, Intent intent) {


        this.intent = intent;
        String action = intent.getAction();

        if (action.equals(ACTION_SMS_RECEIVED)) {
            String number_manager = null, str = "";
            SmsMessage[] msgs = getMessagesFromIntent(intent);

            if (msgs != null) {

                for (int i = 0; i < msgs.length; i++) {
                    number_manager = msgs[i].getOriginatingAddress();
                    str += msgs[i].getMessageBody().toString();
                }
            }
            String[] valores = str.split("#");
            //<$#2017-02-16#2017-02-24#30,00#2#0#Holaaaaaaaaa
            if (number_manager != null) {

                if (valores[0].equals("<$")) // este simbolo significa que es booking
                {
                    String showState = parssearBookingState(valores[4]);
                    if (chequearFidelidadMensaje(context, valores[5], number_manager)) {

                        insertarBooking(valores[1], valores[2], valores[3], valores[4], valores[5], valores[6], context);

                        String mensaje = "Nueva reserva gestionada.\nFecha de inicio: " + valores[1] + "\nFecha final: " +
                                valores[2] + "\nTipo de Reserva: " + valores[3] + "\n" + valores[6];
                        StateBar stateBar = new StateBar();
                        stateBar.notificar(context, CaracolaApplication.class, "Nueva Reserva", number_manager, "Phone", "", mensaje);

                        Mensajero.confirmar_recibo(number_manager);
                    }
                } else if (valores[0].equals(">$")) // este simbolo actualizar
                {
                    //>$#2017-01-10#2017-02-16#2017-02-24#30,00#2#0#Holaaaaaaaaa
                    if (chequearFidelidadMensaje(context, valores[6], number_manager)) {

                        actualizarBooking(valores[1], valores[2], valores[3], valores[4], valores[5], valores[6], valores[7], context);

                        String mensaje = "Actualizacion de reserva.\nFecha de inicio: " + valores[1] + "\nFecha final: " +
                                valores[2] + "\nTipo de Reserva: " + valores[3] + "\n" + valores[6];

                        StateBar stateBar = new StateBar();
                        stateBar.notificar(context, CaracolaApplication.class, "Actualizacion de reserva", number_manager, "Phone", "", mensaje);

                        Mensajero.confirmar_recibo(number_manager);
                    }
                } else if (valores[0].equals("$$")) {
                    //$$#2017-02-16#111
                    if (chequearFidelidadMensaje(context, valores[2], number_manager)) {

                        Booking booking = borrarBooking(valores[1], valores[2], context);

                        LocalDateConverter localDateConverter = new LocalDateConverter();
                        String mensaje = "Actualizacion de reserva.\nFecha de inicio: " + localDateConverter.convertToPersisted(booking.getCheckInDate()) + "\nFecha final: " +
                                localDateConverter.convertToPersisted(booking.getCheckOutDate()) + "\nHabitacion: " + booking.getBedroom().getName();

                        StateBar stateBar = new StateBar();
                        stateBar.notificar(context, CaracolaApplication.class, "Eliminar Booking", number_manager, "Phone", "", mensaje);

                        Mensajero.confirmar_recibo(number_manager);
                    }
                }
            }
        }
    }

    private Booking borrarBooking(String fechaInicio, String roomCode, Context context) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("y-MM-dd");
        EntityDataStore<Persistable> dataStore = DataStoreHolder.getInstance().getDataStore(context);
        LocalDate localDateInicio = LocalDate.parse(fechaInicio, formatter);
        int code = Integer.parseInt(roomCode);

        Bedroom bedroom = dataStore.select(Bedroom.class).where(Bedroom.CODE.eq(code)).get().first();
        Booking booking = dataStore.select(Booking.class).where(Booking.BEDROOM.eq(bedroom).and(Booking.CHECK_IN_DATE.eq(localDateInicio))).get().first();
        dataStore.delete(booking);

        return booking;
    }

    private boolean chequearFidelidadMensaje(Context context, String valor, String number_manager) {

        int roomCode = Integer.parseInt(valor);
        EntityDataStore<Persistable> dataStore = DataStoreHolder.getInstance().getDataStore(context);
        Bedroom bedroom = dataStore.select(Bedroom.class).where(Bedroom.CODE.eq(roomCode)).get().first();
        Hostel hostel = bedroom.getHostel();
        Manager managerHostel = dataStore.select(Manager.class).where(Manager.HOSTEL.eq(hostel).and(Manager.PHONE_NUMBER.eq(number_manager))).get().firstOrNull();

        if (managerHostel == null) {
            return false;
        } else {
            return true;
        }
    }

    private void insertarBooking(String fechaInicio, String fechaFin, String precio, String estado, String codigo, String comentario, Context context) {

        int state = Integer.parseInt(estado);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("y-MM-dd");
        EntityDataStore<Persistable> dataStore = DataStoreHolder.getInstance().getDataStore(context);

        LocalDate localDateInicio = LocalDate.parse(fechaInicio, formatter);
        LocalDate localDateFin = LocalDate.parse(fechaFin, formatter);
        BigDecimal price = FormatUtils.parseMoney(precio);
        BookingState bookingState = BookingState.values()[state];
        int roomCode = Integer.parseInt(codigo);
        Bedroom bedroom = dataStore.select(Bedroom.class).where(Bedroom.CODE.eq(roomCode)).get().first();
        String comment = comentario;

        Booking booking = new Booking();
        booking.setCheckInDate(localDateInicio);
        booking.setCheckOutDate(localDateFin);
        booking.setState(bookingState);
        booking.setBedroom(bedroom);
        booking.setNote(comment);
        booking.setPrice(price);

        dataStore.insert(booking);
    }

    private void actualizarBooking(String fechaInicioVieja, String fechaInicio, String fechaFin, String precio, String estado, String codigo, String comentario, Context context) {

        int state = Integer.parseInt(estado);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("y-MM-dd");
        EntityDataStore<Persistable> dataStore = DataStoreHolder.getInstance().getDataStore(context);

        LocalDate localDateInicio = LocalDate.parse(fechaInicio, formatter);
        LocalDate localDateInicioVieja = LocalDate.parse(fechaInicioVieja, formatter);
        LocalDate localDateFin = LocalDate.parse(fechaFin, formatter);
        BigDecimal price = FormatUtils.parseMoney(precio);
        BookingState bookingState = BookingState.values()[state];
        int roomCode = Integer.parseInt(codigo);
        Bedroom bedroom = dataStore.select(Bedroom.class).where(Bedroom.CODE.eq(roomCode)).get().first();
        String comment = comentario;

        Booking booking = dataStore.select(Booking.class).where(Booking.BEDROOM.eq(bedroom).and(Booking.CHECK_IN_DATE.eq(localDateInicioVieja))).get().first();
        booking.setCheckInDate(localDateInicio);
        booking.setCheckOutDate(localDateFin);
        booking.setState(bookingState);
        booking.setBedroom(bedroom);
        booking.setNote(comment);
        booking.setPrice(price);

        dataStore.update(booking);
    }

    private String parssearBookingState(String valor) {

        int estado = Integer.parseInt(valor);
        BookingState bookingState = BookingState.values()[estado];
        String showState = "";

        if (bookingState == BookingState.CHECKED_IN) {
            showState = "Registrado";
        } else if (bookingState == BookingState.PENDING) {
            showState = "Pendiente";
        } else {
            showState = "Confirmado";
        }
        return showState;
    }

    public static SmsMessage[] getMessagesFromIntent(Intent intent) {
        Object[] messages = (Object[]) intent.getSerializableExtra("pdus");
        byte[][] pduObjs = new byte[messages.length][];

        for (int i = 0; i < messages.length; i++) {
            pduObjs[i] = (byte[]) messages[i];
        }

        byte[][] pdus = new byte[pduObjs.length][];
        int pduCount = pdus.length;
        SmsMessage[] msgs = new SmsMessage[pduCount];

        for (int i = 0; i < pduCount; i++) {
            pdus[i] = pduObjs[i];
            msgs[i] = SmsMessage.createFromPdu(pdus[i]);
        }
        return msgs;
    }
}
