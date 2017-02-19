package com.polymitasoft.caracola.view.booking;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import org.threeten.bp.LocalDate;

import static com.polymitasoft.caracola.view.booking.CalendarState.NO_DAY;
import static com.polymitasoft.caracola.view.booking.CalendarState.SELECTED;


public class VistaDia extends Button implements Comparable<VistaDia> {
    private final LocalDate dia;
    private final int strokeWidth;
    private int color;

    public VistaDia(Context context, LocalDate dia, int color) {
        super(context);
        this.dia = dia;
        this.color = color;
        strokeWidth = (int) (getContext().getResources().getDisplayMetrics().density * 2);
        configIni();
        eventos();
    }

    public static boolean mismoMes(VistaDia dia1, VistaDia dia2) {
        return dia1.getCalendar().getYear() == dia2.getCalendar().getYear() && dia1.getCalendar().getMonth() == dia2.getCalendar().getMonth();
    }

    public void pintarColor(int color, CellLocation cellLocation) {
        this.color = color;
        rellenarColor(color, cellLocation);
    }

    private void rellenarColor(int color, CellLocation cellLocation) {
        ShapeDrawable drawable = new ShapeDrawable();
        drawable.getPaint().setColor(color);
//        setTextColor(Color.WHITE);
        if (color != NO_DAY.color()) {
            switch (cellLocation) {
                case ALONE:
                    drawable.setShape(new OvalShape());
                    setBackground(drawable);
                    break;
                case BEGINING:
                    drawable.setShape(new RoundRectShape(new float[]{45, 45, 0, 0, 0, 0, 45, 45}, null, null));
                    setBackground(drawable);
                    break;
                case MIDDLE:
                    setBackgroundColor(color);
                    break;
                case END:
                    drawable.setShape(new RoundRectShape(new float[]{0, 0, 45, 45, 45, 45, 0, 0}, null, null));
                    setBackground(drawable);
                    break;
            }
        }
    }

    public void seleccionar(CellLocation cellLocation) {
        rellenarColor(SELECTED.color(), cellLocation);
    }

    public void deSeleccionar(CellLocation cellLocation) {
        if (color != NO_DAY.color()) {
            rellenarColor(color, cellLocation);
        }
    }

    private void configIni() {
        if (color == NO_DAY.color()) {
            setText("");
        } else {
            setText(dia.getDayOfMonth() + "");
        }
        setBackgroundColor(color);
        this.setGravity(TEXT_ALIGNMENT_CENTER);
    }

    private void eventos() {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickDia(v);
            }
        });
    }

    private void clickDia(View v) {
        if (color != NO_DAY.color()) {
            ReservaPrincipal reservaPrincipal = (ReservaPrincipal) getContext();
            reservaPrincipal.getReservaEsenaPrincipal().getReservaPanelHabitacionActual().clickDia(this);
        }
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public LocalDate getCalendar() {
        return dia;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof VistaDia && dia.equals(((VistaDia) o).dia);
    }

    @Override
    public int hashCode() {
        return dia.hashCode();
    }

    @Override
    public int compareTo(@NonNull VistaDia another) {
        return dia.compareTo(another.getCalendar());
    }
}
