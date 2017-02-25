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
import com.polymitasoft.caracola.datamodel.BookingState;

import java.sql.SQLException;

import static android.provider.Telephony.Sms.Intents.getMessagesFromIntent;

/**
 * Created by asio on 2/24/2017.
 */

public class Reciever extends BroadcastReceiver {

    private static final String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private Intent intent;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.intent = intent;
        String action = intent.getAction();

        if (action.equals(ACTION_SMS_RECEIVED)) {
            String gestor_number = null, str = "";
            SmsMessage[] msgs = getMessagesFromIntent(intent);

            if (msgs != null) {
                for (int i = 0; i < msgs.length; i++) {
                    gestor_number = msgs[i].getOriginatingAddress();
                    str += msgs[i].getMessageBody().toString();
                    str += "\n";
                }
            }
            String[] valores = str.split("#");
            if (gestor_number != null) {
                //  mensaje = "<$#17-01-30#17-02-05#1#1#Aqui va una notica de prerreserva. Esto esta de pinga asere, estoy loco por irme y no me quieren dejar irrrrr.";

                if (valores[0].equals("<$")) // este simbolo significa que es prerreserva...
                {
                    int estado = Integer.parseInt(valores[3]);
                    BookingState bookingState = BookingState.values()[estado];
                    String showState = "";

                    if (bookingState == BookingState.CHECKED_IN) {
                        showState = "Registrado";
                    } else if (bookingState == BookingState.PENDING) {
                        showState = "Pendiente";
                    } else {
                        showState = "Confirmado";
                    }
                    String bodyMessage = "Fecha de Inicio: " + valores[1] + "Fecha Fin: " + valores[2] + " " + showState + " " + valores[4] + " " + valores[5];

                    StateBar stateBar = new StateBar();
                    stateBar.notificar(context, CaracolaApplication.class, "Nueva Prerreserva", bodyMessage, "Info", "Tickerrrr", "Notaaaaa");

                } else if (valores[0].equals(">$")) // este simbolo significa que es reserva
                {
//                    mensaje = ">$#17-01-30#17-02-05#1#1#10#20#Asiel&Alonso Chaviano&90062538346&Cuba&90-06-25&1#Oscar&Alonso Rodriguez&$90062538347&Cuba&56-06-25&1#Magdiel&Alonso Chaviano&90062538348&Cuba&56-06-25&1";

                }
            }
        }
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
