package com.polymitasoft.caracola.view.booking;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.datamodel.Bedroom;
import com.polymitasoft.caracola.datamodel.BookingState;
import com.polymitasoft.caracola.util.FormatUtils;

import java.math.BigDecimal;

import static com.polymitasoft.caracola.view.booking.CalendarState.toCalendarState;


public class DialogoHacerPreReserva extends Dialog {

    private Context context;
    private Button bt_preReservar;
    private RadioButton rb_confirmado;
    private RadioButton rb_pendiente;
    private TextView text_nota;
    private TextView text_price;
    private ReservaPrincipal reservaPrincipal;
    private ReservaPanelHabitacion reservaPanelHabitacionActual;

    public DialogoHacerPreReserva(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.reserva_titulo_hacer_reserva);
        setContentView(R.layout.reserva_dialog_hacer_reserva);

        reservaPrincipal = (ReservaPrincipal) this.context;
        reservaPanelHabitacionActual = reservaPrincipal.getReservaEsenaPrincipal().getReservaPanelHabitacionActual();
        obtener_controles();
        eventos();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean highSeason = preferences.getBoolean("high_season", false);
        Bedroom bedroom = reservaPanelHabitacionActual.getHabitacion();
        // TODO Encapsular esta lógica en una función
        BigDecimal price = (highSeason)? bedroom.getPriceInHighSeason() : bedroom.getPriceInLowSeason();
        text_price.setText(FormatUtils.formatMoney(price));
    }

    private void obtener_controles() {
        bt_preReservar = (Button) findViewById(R.id.reserva_bt_hacer_pre_R);
        rb_confirmado = (RadioButton) findViewById(R.id.reserva_rb_confirmada);
        rb_pendiente = (RadioButton) findViewById(R.id.reserva_rb_pendiente);
        text_nota = (TextView) findViewById(R.id.booking_note);
        text_price = (TextView) findViewById(R.id.booking_price);
    }

    private void eventos() {
        bt_preReservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPreR();
            }
        });
    }

    private void clickPreR() {
        String nota = text_nota.getText().toString();
        BookingState estado;
        estado = rb_confirmado.isChecked() ? BookingState.CONFIRMED : BookingState.PENDING;
        VistaDia primerDiaSelec = reservaPanelHabitacionActual.getPrimerDiaSelec();
        VistaDia segundoDiaSelec = reservaPanelHabitacionActual.getSegundoDiaSelec();
        BigDecimal price = FormatUtils.parseMoney(text_price.getText().toString());
        reservaPanelHabitacionActual.salvarPreReservaYadicionarALosMeses(primerDiaSelec, segundoDiaSelec, estado, nota, price);

        reservaPanelHabitacionActual.actualizarColorRangoModoH(primerDiaSelec, segundoDiaSelec, toCalendarState(estado).color());
        reservaPanelHabitacionActual.limpiarTodo();

        dismiss();
    }
}

