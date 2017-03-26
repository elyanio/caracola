package com.polymitasoft.caracola.view.booking;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.threeten.bp.LocalDate;

import static com.polymitasoft.caracola.view.booking.CalendarState.NO_DAY;
import static com.polymitasoft.caracola.view.booking.CalendarState.SELECTED;

public class VistaDia extends TextView implements Comparable<VistaDia> {
    private final LocalDate dia;
    @ColorInt private int color;
    @ColorInt private int textColor;

    public VistaDia(Context context, LocalDate dia, int color) {
        this(context, dia, color, Color.BLACK, CellLocation.MIDDLE);

    }

    public VistaDia(Context context, LocalDate dia, int color, CellLocation cellLocation) {
        this(context, dia, color, Color.BLACK, cellLocation);

    }

    public VistaDia(Context context, LocalDate date, int bgColor, int textColor, CellLocation cellLocation) {
        super(context);
        this.dia = date;
        this.color = bgColor;
        this.textColor = textColor;
        configIni(cellLocation);
        eventos();
    }

    public static boolean mismoMes(VistaDia dia1, VistaDia dia2) {
        return dia1.getCalendar().getYear() == dia2.getCalendar().getYear() && dia1.getCalendar().getMonth() == dia2.getCalendar().getMonth();
    }

    public void pintarColor(int bgColor, int textColor, CellLocation cellLocation) {
        this.color = bgColor;
        this.textColor = textColor;
        rellenarColor(bgColor, textColor, cellLocation);
        marcarHoy();
    }

    public void pintarColor(int color, CellLocation cellLocation) {
        pintarColor(color, Color.BLACK, cellLocation);
        marcarHoy();
    }

    private void rellenarColor(int bgColor, int textColor, CellLocation cellLocation) {
        ShapeDrawable drawable = new ShapeDrawable();
        drawable.getPaint().setColor(bgColor);
        setTextColor(textColor);
        if (bgColor != NO_DAY.color()) {
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
                    setBackgroundColor(bgColor);
                    break;
                case END:
                    drawable.setShape(new RoundRectShape(new float[]{0, 0, 45, 45, 45, 45, 0, 0}, null, null));
                    setBackground(drawable);
                    break;
            }
        }
        marcarHoy();
    }

    public void seleccionar(CellLocation cellLocation) {
        rellenarColor(SELECTED.color(), Color.WHITE, cellLocation);
        marcarHoy();
    }

    public void deSeleccionar(CellLocation cellLocation) {
        if (color != NO_DAY.color()) {
            rellenarColor(color, textColor, cellLocation);
        }
        marcarHoy();
    }

    private void configIni(CellLocation cellLocation) {
        if (color == NO_DAY.color()) {
            setText("");
            setBackgroundColor(color);
        } else {
            setText(dia.getDayOfMonth() + "");
            pintarColor(color, textColor, cellLocation);
        }
        this.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        marcarHoy();
    }

    private void eventos() {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clickDia(v);
            }
        });
    }

    public void marcarHoy(){
        LocalDate now = LocalDate.now();

        if(dia.equals(now)){
            setTypeface(null, Typeface.BOLD);
        }else{
            setTypeface(null, Typeface.NORMAL);
        }
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
