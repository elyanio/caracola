package com.polymitasoft.caracola.communication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.datamodel.Bedroom;
import com.polymitasoft.caracola.datamodel.Booking;
import com.polymitasoft.caracola.datamodel.BookingState;
import com.polymitasoft.caracola.datamodel.Hostel;
import com.polymitasoft.caracola.datamodel.LocalDateConverter;
import com.polymitasoft.caracola.datamodel.Manager;
import com.polymitasoft.caracola.encoding.CoderUtils;
import com.polymitasoft.caracola.notification.StateBar;
import com.polymitasoft.caracola.settings.Preferences;
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
                for (SmsMessage msg : msgs) {
                    number_manager = msg.getOriginatingAddress();
                    str += msg.getMessageBody();
                }
            }
            String[] valores = str.split("#");
            if (number_manager.length() >= 8) {
                number_manager = parsearNumero(number_manager);
            }

            if (number_manager != null) {
                switch (valores[0]) {
                    case "<$":
                        resolverNuevoBooking(valores, context, number_manager);
                        break;
                    case ">$":
                        resolverActualizarBooking(valores, context, number_manager);
                        break;
                    case "$$":
                        resolverEliminarBooking(valores, context, number_manager);
                        break;
                    case "$$$":
                        resolverConfirmacion(context, number_manager);
                        break;
                    case "<@>":
                        resolverActivacion(valores, number_manager);
                        break;
                }
            }
        }
    }

    private void confirmarRecibo(String number) {
        if (Preferences.isEnableConfirmSms()) {
            Mensajero.confirmar_recibo(number);
        }
    }

    private void resolverActivacion(String[] valores, String number_manager) {
        String userActivationCode = CoderUtils.decryptFrom64String(valores[1]);
        String[] split = userActivationCode.split("#");
        if (number_manager.equals("54520426") || number_manager.equals("53746802") || number_manager.equals("54150751") || number_manager.equals("54126878") || number_manager.equals("53850863")) {
            String requestCode = CoderUtils.getRequestCode();
            String encryptedString = CoderUtils.generateCode(requestCode);

            if (split[0].equals(encryptedString)) {
                Preferences.setEncryptedPreference("evaluation_date", split[1]);
                Preferences.setEncryptedPreference("evaluation_days", valores[2]);
            }
        }
    }

    private void resolverNuevoBooking(String[] dataBooking, Context context, String number_manager) {
        String showState = parssearBookingState(dataBooking[4]);

        if (chequearFidelidadMensaje(context, dataBooking[5], number_manager, dataBooking[1], dataBooking[6])) {
            insertarBooking(dataBooking[1], dataBooking[2], dataBooking[3], dataBooking[4], dataBooking[5], dataBooking[7], dataBooking[6], context);
            EntityDataStore<Persistable> dataStore = DataStoreHolder.INSTANCE.getDataStore();
            Hostel hostel = dataStore.select(Hostel.class).where(Hostel.CODE.eq(dataBooking[6])).get().first();
            Bedroom bedroom = dataStore.select(Bedroom.class).where(Bedroom.CODE.eq(Integer.parseInt(dataBooking[5]))).and(Bedroom.HOSTEL.eq(hostel)).get().first();
            notificarNuevoBooking(dataBooking, showState, bedroom.getName(), hostel.getName(), context, number_manager);
        } else {
            StateBar stateBar = new StateBar();
            stateBar.BookingNotification(context, 1, "Error en la Reserva", "Mensaje de Error", number_manager, "La reserva ya existe, por favor sincronice su calendario.");
        }
    }

    private void notificarNuevoBooking(String[] dataBooking, String showState, String bedroomName, String hostelName, Context context, String number_manager) {
        LocalDateConverter localDateConverter = new LocalDateConverter();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("y-MM-dd");
        LocalDate localDateFin = LocalDate.parse(dataBooking[2], formatter);
        localDateFin = localDateFin.plusDays(1);

        String mensaje = "Nueva reserva gestionada." +
                "\nEntrada: " + dataBooking[1] +
                "\nSalida: " + localDateConverter.convertToPersisted(localDateFin) +
                "\nTipo de Reserva: " + showState +
                "\nHabitación: " + bedroomName +
                "\nHostal: " + hostelName +
                "\n" + dataBooking[7];

        StateBar stateBar = new StateBar();
        stateBar.BookingNotification(context, 1, "Nueva Reserva", "Reserva gestionada", number_manager, mensaje);
        confirmarRecibo(number_manager);
    }

    private void resolverEliminarBooking(String[] valores, Context context, String number_manager) {

        if (chequearFidelidadMensaje(context, valores[2], number_manager, valores[3])) {
            EntityDataStore<Persistable> dataStore = DataStoreHolder.INSTANCE.getDataStore();
            String hostelCode = valores[3];
            Hostel hostel = dataStore.select(Hostel.class).where(Hostel.CODE.eq(hostelCode)).get().first();
            Bedroom bedroom = dataStore.select(Bedroom.class).where(Bedroom.CODE.eq(Integer.parseInt(valores[2]))).and(Bedroom.HOSTEL.eq(hostel)).get().first();
            Booking booking = borrarBooking(valores[1], valores[2], context);
            notificarEliminarBooking(booking.getCheckInDate(), booking.getCheckOutDate(), bedroom.getName(), hostel.getName(), context, number_manager);
        }
    }

    private void notificarEliminarBooking(LocalDate checkInDate, LocalDate checkOutDate, String bedroomName, String hostelName, Context context, String number_manager) {
        LocalDateConverter localDateConverter = new LocalDateConverter();
        checkOutDate = checkOutDate.plusDays(1);
        String mensaje = "Reserva eliminada." +
                "\nEntrada: " + localDateConverter.convertToPersisted(checkInDate) +
                "\nSalida: " + localDateConverter.convertToPersisted(checkOutDate) +
                "\nHabitación: " + bedroomName +
                "\nHostal " + hostelName;

        StateBar stateBar = new StateBar();
        stateBar.BookingNotification(context, 1, "Eliminación Reserva", "Reserva eliminada", number_manager, mensaje);
        confirmarRecibo(number_manager);
    }

    private void resolverActualizarBooking(String[] bookingData, Context context, String number_manager) {

        if (chequearFidelidadMensaje(context, bookingData[6], number_manager, bookingData[7])) {
            String showState = parssearBookingState(bookingData[5]);
            actualizarBooking(bookingData[1], bookingData[2], bookingData[3], bookingData[4], bookingData[5], bookingData[6], bookingData[8], bookingData[7], context);
            int code = Integer.parseInt(bookingData[6]);
            EntityDataStore<Persistable> dataStore = DataStoreHolder.INSTANCE.getDataStore();
            Hostel hostel = dataStore.select(Hostel.class).where(Hostel.CODE.eq(bookingData[7])).get().first();
            Bedroom bedroom = dataStore.select(Bedroom.class).where(Bedroom.CODE.eq(code)).and(Bedroom.HOSTEL.eq(hostel)).get().first();
            notificarActualizarBooking(bookingData[2], bookingData[3], showState, bedroom.getName(), hostel.getName(), bookingData[8], context, number_manager);
        }
    }

    private void notificarActualizarBooking(String checkInDate, String checkOutDate, String showState, String bedroomName, String hostelName, String comment, Context context, String number_manager) {
        LocalDateConverter localDateConverter = new LocalDateConverter();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("y-MM-dd");
        LocalDate localDateFin = LocalDate.parse(checkOutDate, formatter);
        localDateFin = localDateFin.plusDays(1);

        String mensaje = "Actualización de reserva." +
                "\nEntrada: " + checkInDate +
                "\nSalida: " + localDateConverter.convertToPersisted(localDateFin) +
                "\nTipo de Reserva: " + showState +
                "\nHabitación: " + bedroomName +
                "\nHostal: " + hostelName +
                "\n" + comment;

        StateBar stateBar = new StateBar();
        stateBar.BookingNotification(context, 1, "Actualización de Reserva", "Reserva actualizada", number_manager, mensaje);
        confirmarRecibo(number_manager);
    }

    private void resolverConfirmacion(Context context, String number_manager) {
        StateBar stateBar = new StateBar();
        stateBar.BookingNotification(context, 1, "Mensaje de Confirmación", "Mensaje recibido, Los datos han sido actualizado", number_manager, "");
    }

    private String parsearNumero(String number) {
        int indexFinal = number.length();
        int indexInicial = number.length() - 8;

        return number.substring(indexInicial, indexFinal);
    }

    private Booking borrarBooking(String fechaInicio, String roomCode, Context context) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("y-MM-dd");
        EntityDataStore<Persistable> dataStore = DataStoreHolder.INSTANCE.getDataStore();
        LocalDate localDateInicio = LocalDate.parse(fechaInicio, formatter);

        int code = Integer.parseInt(roomCode);
        Bedroom bedroom = dataStore.select(Bedroom.class).where(Bedroom.CODE.eq(code)).get().first();
        Booking booking = dataStore.select(Booking.class).where(Booking.BEDROOM.eq(bedroom).and(Booking.CHECK_IN_DATE.eq(localDateInicio))).get().first();
        dataStore.delete(booking);
        return booking;
    }

    private boolean chequearFidelidadMensaje(Context context, String valor, String number_manager, String fechaInicio, String hostelCode) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("y-MM-dd");
        LocalDate localDateInicio = LocalDate.parse(fechaInicio, formatter);

        int roomCode = Integer.parseInt(valor);
        EntityDataStore<Persistable> dataStore = DataStoreHolder.INSTANCE.getDataStore();
        Hostel hostel = dataStore.select(Hostel.class).where(Hostel.CODE.eq(hostelCode)).get().first();
        Bedroom bedroom = dataStore.select(Bedroom.class).where(Bedroom.CODE.eq(roomCode)).and(Bedroom.HOSTEL.eq(hostel)).get().first();
        Manager managerHostel = dataStore.select(Manager.class).where(Manager.HOSTEL.eq(hostel).and(Manager.PHONE_NUMBER.eq(number_manager))).get().firstOrNull();

        if (managerHostel == null) {
            return false;
        } else {
            Booking firstBooking = dataStore.select(Booking.class).where(Booking.CHECK_IN_DATE.eq(localDateInicio).and(Booking.BEDROOM.eq(bedroom))).get().firstOrNull();
            return firstBooking == null;
        }
    }

    private boolean chequearFidelidadMensaje(Context context, String valor, String number_manager, String hostelCode) {

        int roomCode = Integer.parseInt(valor);
        EntityDataStore<Persistable> dataStore = DataStoreHolder.INSTANCE.getDataStore();
        Hostel hostel = dataStore.select(Hostel.class).where(Hostel.CODE.eq(hostelCode)).get().first();
        Bedroom bedroom = dataStore.select(Bedroom.class).where(Bedroom.CODE.eq(roomCode)).and(Bedroom.HOSTEL.eq(hostel)).get().first();

        Manager managerHostel = dataStore.select(Manager.class).where(Manager.HOSTEL.eq(hostel).and(Manager.PHONE_NUMBER.eq(number_manager))).get().firstOrNull();
        return managerHostel != null;
    }

    private void insertarBooking(String fechaInicio, String fechaFin, String precio, String estado, String codigo, String comentario, String codigoHostal, Context context) {

        int state = Integer.parseInt(estado);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("y-MM-dd");
        EntityDataStore<Persistable> dataStore = DataStoreHolder.INSTANCE.getDataStore();


        LocalDate localDateInicio = LocalDate.parse(fechaInicio, formatter);
        LocalDate localDateFin = LocalDate.parse(fechaFin, formatter);
        BigDecimal price = FormatUtils.parseMoney(precio);

        BookingState bookingState = null;

        switch (state) {
            case 1:
                bookingState = BookingState.PENDING;
                break;
            case 2:
                bookingState = BookingState.CONFIRMED;
                break;
            case 3:
                bookingState = BookingState.CHECKED_IN;
                break;
        }
        int roomCode = Integer.parseInt(codigo);

        Hostel hostel = dataStore.select(Hostel.class).where(Hostel.CODE.eq(codigoHostal)).get().first();
        Bedroom bedroom = dataStore.select(Bedroom.class).where(Bedroom.CODE.eq(roomCode)).and(Bedroom.HOSTEL.eq(hostel)).get().first();

        Booking booking = new Booking();
        booking.setCheckInDate(localDateInicio);
        booking.setCheckOutDate(localDateFin);
        booking.setState(bookingState);
        booking.setBedroom(bedroom);
        booking.setNote(comentario);
        booking.setPrice(price);

        dataStore.insert(booking);
    }

    private void actualizarBooking(String fechaInicioVieja, String fechaInicio, String fechaFin, String precio, String estado, String codigo, String comentario, String hostelCode, Context context) {

        int state = Integer.parseInt(estado);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("y-MM-dd");
        EntityDataStore<Persistable> dataStore = DataStoreHolder.INSTANCE.getDataStore();

        LocalDate localDateInicio = LocalDate.parse(fechaInicio, formatter);
        LocalDate localDateInicioVieja = LocalDate.parse(fechaInicioVieja, formatter);
        LocalDate localDateFin = LocalDate.parse(fechaFin, formatter);
        BigDecimal price = FormatUtils.parseMoney(precio);

        BookingState bookingState = null;

        switch (state) {
            case 1:
                bookingState = BookingState.PENDING;
                break;
            case 2:
                bookingState = BookingState.CONFIRMED;
                break;
            case 3:
                bookingState = BookingState.CHECKED_IN;
                break;
        }
        int roomCode = Integer.parseInt(codigo);

        Hostel hostel = dataStore.select(Hostel.class).where(Hostel.CODE.eq(hostelCode)).get().first();
        Bedroom bedroom = dataStore.select(Bedroom.class).where(Bedroom.CODE.eq(roomCode)).and(Bedroom.HOSTEL.eq(hostel)).get().first();

        Booking booking = dataStore.select(Booking.class).where(Booking.BEDROOM.eq(bedroom).and(Booking.CHECK_IN_DATE.eq(localDateInicioVieja))).get().first();
        booking.setCheckInDate(localDateInicio);
        booking.setCheckOutDate(localDateFin);
        booking.setState(bookingState);
        booking.setBedroom(bedroom);
        booking.setNote(comentario);
        booking.setPrice(price);

        dataStore.update(booking);
    }

    private String parssearBookingState(String valor) {

        int state = Integer.parseInt(valor);
        String imprimir = "";

        switch (state) {
            case 1:
                imprimir = "Pendiente";
                break;
            case 2:
                imprimir = "Confirmada";
                break;
            case 3:
                imprimir = "Registrada";
                break;
        }
        return imprimir;
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
