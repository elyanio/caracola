package com.polymitasoft.caracola.communication;

import android.telephony.SmsManager;

import com.polymitasoft.caracola.datamodel.Manager;

import java.util.List;

/**
 * Created by asio on 2/27/2017.
 */

public class Mensajero {

    private static final String MENSAJE_CONFIRMACION = "$$$#Mensaje Recibido";

    public static void enviar_mensaje(String numero, String mensaje) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(numero, null, mensaje, null, null);
    }

    public static void enviar_mensaje(List<Manager> managers, String mensaje) {
        SmsManager sms = SmsManager.getDefault();
        if (managers.size() != 0) {
            for (Manager manager : managers) {
                sms.sendTextMessage(manager.getPhoneNumber(), null, mensaje, null, null);
            }
        }
    }

    public static void  confirmar_recibo(String numero) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(numero, null, MENSAJE_CONFIRMACION, null, null);
    }
}
