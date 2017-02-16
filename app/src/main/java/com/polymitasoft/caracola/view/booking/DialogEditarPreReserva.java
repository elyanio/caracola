package com.polymitasoft.caracola.view.booking;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.polymitasoft.caracola.DataStoreHolder;
import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.components.DateSpinner;
import com.polymitasoft.caracola.dao.BookingDao;
import com.polymitasoft.caracola.datamodel.Booking;
import com.polymitasoft.caracola.datamodel.BookingState;
import com.polymitasoft.caracola.util.FormatUtils;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

import static com.polymitasoft.caracola.view.booking.CalendarState.toCalendarState;

public class DialogEditarPreReserva extends Dialog {
    private final ReservaPanelHabitacion reservaPanelHabitacionActual;
    private Context context;
    private Booking preReserva;
    private Button bt_preReservar;
    private RadioButton rb_confirmado;
    private RadioButton rb_pendiente;
    private TextView text_nota;
    private TextView textPrice;
    private DateSpinner checkInDateSpinner;
    private DateSpinner checkOutSpinner;
    private EntityDataStore<Persistable> dataStore;
    private BookingDao bookingDao;

    public DialogEditarPreReserva(Context context) {
        super(context);
        this.context = context;
        dataStore = DataStoreHolder.getInstance().getDataStore(context);
        bookingDao = new BookingDao(dataStore);
        setTitle(R.string.reserva_titulo_hacer_reserva);
        setContentView(R.layout.reserva_dialog_editar_reserva);
        ReservaPrincipal reservaPrincipal = (ReservaPrincipal) this.context;
        reservaPanelHabitacionActual = reservaPrincipal.getReservaEsenaPrincipal().getReservaPanelHabitacionActual();
        preReserva = reservaPanelHabitacionActual.getPreReservaSelecc();

        obtener_controles();
        configurarControles();
        eventos();
    }

    private void obtener_controles() {
        bt_preReservar = (Button) findViewById(R.id.reserva_bt_hacer_pre_R);
        rb_confirmado = (RadioButton) findViewById(R.id.reserva_rb_confirmada);
        rb_pendiente = (RadioButton) findViewById(R.id.reserva_rb_pendiente);
        text_nota = (TextView) findViewById(R.id.booking_note);
        textPrice = (TextView) findViewById(R.id.booking_price);
        checkInDateSpinner = (DateSpinner) findViewById(R.id.booking_check_in);
        checkOutSpinner = (DateSpinner) findViewById(R.id.booking_check_out);
    }


    private void configurarControles() {
        text_nota.setText(preReserva.getNote());
        textPrice.setText(FormatUtils.formatMoney(preReserva.getPrice()));
        if ((preReserva.getState() == BookingState.CONFIRMED)) {
            rb_confirmado.setChecked(true);
        } else {
            rb_confirmado.setChecked(false);
            rb_pendiente.setChecked(true);
        }
        checkInDateSpinner.setDate(preReserva.getCheckInDate());
        checkOutSpinner.setDate(preReserva.getCheckOutDate());
        LocalDate minDate = bookingDao.previousBookedDay(preReserva.getBedroom(), preReserva.getCheckInDate()).plusDays(1);
        LocalDate maxDate = bookingDao.nextBookedDay(preReserva.getBedroom(), preReserva.getCheckOutDate()).minusDays(1);
        checkInDateSpinner.setMinDate(minDate);
        checkInDateSpinner.setMaxDate(maxDate);
        checkOutSpinner.setMinDate(minDate);
        checkOutSpinner.setMaxDate(maxDate);
    }

    private void eventos() {
        bt_preReservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickeditR();
            }
        });
    }

    private void clickeditR() {
        String nota = text_nota.getText().toString();
        BookingState estado = rb_confirmado.isChecked() ? BookingState.CONFIRMED : BookingState.PENDING;
        BigDecimal price = FormatUtils.parseMoney(textPrice.getText().toString());

        LocalDate oldCheckInDate = preReserva.getCheckInDate();
        LocalDate oldCheckOutDate = preReserva.getCheckOutDate();

        // TODO Vincular los calendarios
        preReserva.setCheckInDate(checkInDateSpinner.getDate());
        preReserva.setCheckOutDate(checkOutSpinner.getDate());
        preReserva.setPrice(price);
        preReserva.setState(estado);
        preReserva.setNote(nota);

        VistaDia vistaDiaFictIni = reservaPanelHabitacionActual.obtenerVistaDiaFict(oldCheckInDate);
        VistaDia vistaDiaFictFin = reservaPanelHabitacionActual.obtenerVistaDiaFict(oldCheckOutDate);
        VistaDia vistaDiaFictIniNew = reservaPanelHabitacionActual.obtenerVistaDiaFict(preReserva.getCheckInDate());
        VistaDia vistaDiaFictFinNew = reservaPanelHabitacionActual.obtenerVistaDiaFict(preReserva.getCheckOutDate());

        dataStore.update(preReserva);
        reservaPanelHabitacionActual.actualizarColorRangoModoH(vistaDiaFictIni, vistaDiaFictFin, CalendarState.EMPTY.color());
        reservaPanelHabitacionActual.actualizarColorRangoModoH(vistaDiaFictIniNew, vistaDiaFictFinNew, toCalendarState(estado).color());
        reservaPanelHabitacionActual.limpiarTodo();
        dismiss();
    }
}
