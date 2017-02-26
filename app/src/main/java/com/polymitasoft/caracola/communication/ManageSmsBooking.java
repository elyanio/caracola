package com.polymitasoft.caracola.communication;

import android.content.Context;
import android.telephony.SmsManager;

import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.datamodel.Bedroom;
import com.polymitasoft.caracola.datamodel.BookingState;
import com.polymitasoft.caracola.datamodel.Hostel;
import com.polymitasoft.caracola.datamodel.Manager;

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

    private String fecha_inicio;
    private String fecha_fin;
    private BookingState estado;
    private String nota;
    private int roomCode;

    private Bedroom bedroom;
    private List<Manager> managers;

    private Context context;

    private EntityDataStore<Persistable> dataStore;

    public ManageSmsBooking(String fecha_inicio, String fecha_fin, BookingState estado, String nota, String roomCode, Context context) {

        this.context = context;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.estado = estado;
        this.nota = nota;
        this.roomCode = Integer.parseInt(roomCode);
        dataStore = DataStoreHolder.getInstance().getDataStore(context);

    }

    public LocalDate getFechaInicio() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("y-MM-dd");
        return LocalDate.parse(fecha_inicio, formatter);
    }

    public LocalDate getFechaFin() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("y-MM-dd");
        return LocalDate.parse(fecha_fin, formatter);
    }

    public Bedroom getBedroom() throws SQLException {
        bedroom = dataStore.select(Bedroom.class).where(Bedroom.CODE.eq(roomCode)).get().first();
        return bedroom;
    }

    public void buildMessage() {
        //  mensaje = "<$#17-01-30#17-02-05#1#1#Aqui va una notica de prerreserva. Esto esta de pinga asere, estoy loco por irme y no me dejaaaannn";
        int state=1;
        switch (estado) {
            case PENDING:
                state = PENDING;
                break;
            case CONFIRMED:
                state=CONFIRMED;
                break;
            case CHECKED_IN:
                state=CHECKED_IN;
                break;
        }
        mensaje = "<$#" + fecha_inicio + "#" + fecha_fin + "#" +state+"#"+roomCode+"#"+nota;
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

    public void enviar_mensaje(String mensaje) {
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
}
