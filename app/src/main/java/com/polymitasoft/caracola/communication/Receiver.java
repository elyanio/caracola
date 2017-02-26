package com.polymitasoft.caracola.communication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsMessage;

import com.polymitasoft.caracola.CaracolaApplication;
import com.polymitasoft.caracola.Notification.StateBar;
import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.datamodel.Bedroom;
import com.polymitasoft.caracola.datamodel.Booking;
import com.polymitasoft.caracola.datamodel.BookingState;
import com.polymitasoft.caracola.datamodel.Hostel;
import com.polymitasoft.caracola.datamodel.LocalDateConverter;
import com.polymitasoft.caracola.datamodel.Manager;
import com.polymitasoft.caracola.util.FormatUtils;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.math.BigDecimal;
import java.sql.SQLException;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

import static android.provider.Telephony.Sms.Intents.getMessagesFromIntent;

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
                    str += "\n";
                }
            }
            String[] valores = str.split("#");
            //<$#2017-02-16#2017-02-24#30,00#2#0#Holaaaaaaaaa
            if (number_manager != null) {

                if (valores[0].equals("<$")) // este simbolo significa que es booking
                {
                    String showState = parssearBookingState(valores[4]);
                    if (chequearFidelidadMensaje(context, valores[5], number_manager)) {

                        actualizarCalendario(valores[1], valores[2], valores[3], valores[4], valores[5], valores[6], context);
                        StateBar stateBar = new StateBar();
                        stateBar.notificar(context, CaracolaApplication.class, "Nueva Prerreserva", "Hay vejoooooo", "Info", "Tickerrrr", "Notaaaaa");
                    }
                } else if (valores[0].equals(">$")) // este simbolo significa que es reserva
                {
                    //mensaje = ">$#17-01-30#17-02-05#1#1#10#20#Asiel&Alonso Chaviano&90062538346&Cuba&90-06-25&1#Oscar&Alonso Rodriguez&$90062538347&Cuba&56-06-25&1#Magdiel&Alonso Chaviano&90062538348&Cuba&56-06-25&1";
                }
            }
        }
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

    private void actualizarCalendario(String fechaInicio, String fechaFin, String precio, String estado, String codigo, String comentario, Context context) {

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
