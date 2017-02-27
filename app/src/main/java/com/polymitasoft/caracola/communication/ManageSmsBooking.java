package com.polymitasoft.caracola.communication;

import android.content.Context;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

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

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

import static com.polymitasoft.caracola.datamodel.BookingState.*;

/**
 * Created by asio on 2/25/2017.
 */

public class ManageSmsBooking {

    private static final int PENDING = 1;
    private static final int CONFIRMED = 2;
    private static final int CHECKED_IN = 3;

    private static final String MENSAJE_CONFIRMACION = "Mensaje Prerreserva Recibido";

    private String mensaje;
    private LocalDate oldFecha_inicio;
    private LocalDate fecha_inicio;
    private LocalDate fecha_fin;
    private BookingState estado;
    private String nota;
    private int roomCode;
    private String price;

    private Bedroom bedroom;
    private List<Manager> managers;

    private Context context;

    private EntityDataStore<Persistable> dataStore;

    public ManageSmsBooking(LocalDate fecha_inicio, LocalDate fecha_fin, BookingState estado, String nota, int roomCode, String price, Context context) {

        this.context = context;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.estado = estado;
        this.nota = nota;
        this.price = price;
        this.roomCode = roomCode;

        dataStore = DataStoreHolder.getInstance().getDataStore(context);
    }

    public ManageSmsBooking(Booking booking, Context context) {

        this.context = context;
        this.fecha_inicio = booking.getCheckInDate();
        this.fecha_fin = booking.getCheckOutDate();
        this.estado = booking.getState();
        this.nota = booking.getNote();
        this.price = FormatUtils.formatMoney(booking.getPrice());
        this.roomCode = booking.getBedroom().getCode();

        dataStore = DataStoreHolder.getInstance().getDataStore(context);
    }

    public ManageSmsBooking(Booking oldBooking, Booking newBooking, Context context) {

        this.context = context;
        this.fecha_inicio = newBooking.getCheckInDate();
        this.fecha_fin = newBooking.getCheckOutDate();
        this.estado = newBooking.getState();
        this.nota = newBooking.getNote();
        this.price = FormatUtils.formatMoney(newBooking.getPrice());
        this.roomCode = oldBooking.getBedroom().getCode();
        oldFecha_inicio = oldBooking.getCheckInDate();

        dataStore = DataStoreHolder.getInstance().getDataStore(context);
    }

    public LocalDate getFechaInicio() {
        return fecha_inicio;
    }

    public LocalDate getFechaFin() {
        return fecha_fin;
    }

    public void findBedroom() {
        bedroom = dataStore.select(Bedroom.class).where(Bedroom.CODE.eq(roomCode)).get().first();
    }

    public void buildMessage() {

        int state = 0;
        switch (estado) {
            case PENDING:
                state = PENDING;
                break;
            case CONFIRMED:
                state = CONFIRMED;
                break;
            case CHECKED_IN:
                state = CHECKED_IN;
                break;
        }
        LocalDateConverter localDateConverter = new LocalDateConverter();
        mensaje = "<$#" + localDateConverter.convertToPersisted(fecha_inicio) + "#" + localDateConverter.convertToPersisted(fecha_fin) + "#" + price + "#" + state + "#" + roomCode + "#" + nota;
    }

    public void buildUpdateMessage() {
        //  mensaje = "<$#17-01-30#17-02-05#1#1#Aqui va una notica de prerreserva. Esto esta de pinga asere, estoy loco por irme y no me dejaaaannn";
        int state = 0;
        switch (estado) {
            case PENDING:
                state = PENDING;
                break;
            case CONFIRMED:
                state = CONFIRMED;
                break;
            case CHECKED_IN:
                state = CHECKED_IN;
                break;
        }
        LocalDateConverter localDateConverter = new LocalDateConverter();
        mensaje = ">$#" + localDateConverter.convertToPersisted(oldFecha_inicio) + "#" + localDateConverter.convertToPersisted(fecha_inicio) + "#" + localDateConverter.convertToPersisted(fecha_fin) + "#" + price + "#" + state + "#" + roomCode + "#" + nota;
//        Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show();
//        Log.e("asio", "                          ddsfdsfsdfsdfsfsfsfsfsfsfsfsfsfsfwefffghrfksdjskdfhsfkhskdhk"+mensaje);
    }

    public void buildDeleteMessage() {
        //  mensaje = "<$#17-01-30#17-02-05#1#1#Aqui va una notica de prerreserva. Esto esta de pinga asere, estoy loco por irme y no me dejaaaannn";
        int state = 0;
        switch (estado) {
            case PENDING:
                state = PENDING;
                break;
            case CONFIRMED:
                state = CONFIRMED;
                break;
            case CHECKED_IN:
                state = CHECKED_IN;
                break;
        }

        LocalDateConverter localDateConverter = new LocalDateConverter();
        mensaje = "$$#" + localDateConverter.convertToPersisted(fecha_inicio) + "#" + roomCode;
//        Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show();
//        Log.e("asio", "                          ddsfdsfsdfsdfsfsfsfsfsfsfsfsfsfsfwefffghrfksdjskdfhsfkhskdhk"+mensaje);
    }

    public String getNota() {
        return nota;
    }

    public ManageSmsBooking(BookingState estado) {
        this.estado = estado;
    }

    public void findManager() {
        Hostel hostel = bedroom.getHostel();
        managers = dataStore.select(Manager.class).where(Manager.HOSTEL.eq(hostel)).get().toList();
    }

    public void enviar_mensaje(String numero, String mensaje) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(numero, null, mensaje, null, null);
    }

    public void enviar_mensaje() {
        SmsManager sms = SmsManager.getDefault();
        if (managers.size() != 0) {
            for (Manager manager : managers) {
                sms.sendTextMessage(manager.getPhoneNumber(), null, mensaje, null, null);
            }
        }
    }

    public void confirmar_recibo(String numero) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(numero, null, MENSAJE_CONFIRMACION, null, null);
    }

    public List<Manager> getManagers() {
        return managers;
    }

    public Bedroom getBedroom() {
        return bedroom;
    }
}
