package com.polymitasoft.caracola.view.booking;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.datamodel.Bedroom;

import java.util.ArrayList;
import java.util.List;


public class Disponibilidad extends LinearLayout {
    private ListView listDisponibilidad;
    private ListDisponibilidadAdapter adaptador;
    private boolean visibleTextNota = false;

    public Disponibilidad(Context context) {
        super(context);
        init();
    }

    public Disponibilidad(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Disponibilidad(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
//        String infladorServicio = Context.LAYOUT_INFLATER_SERVICE;
//        LayoutInflater asioInflador = (LayoutInflater) getContext().getSystemService(infladorServicio);
        inflate(getContext(), R.layout.reserva_disponibilidad, this);
//        asioInflador.inflate(R.layout.reserva_disponibilidad, this, true);
        obtenerControles();
        ListDisponibilidadAdapter adaptador = new ListDisponibilidadAdapter(getContext(), android.R.layout.simple_list_item_1, new ArrayList<Bedroom>());
        listDisponibilidad.setAdapter(adaptador);
        this.adaptador = adaptador;
        setY(getY() - 300);
    }

    private void obtenerControles() {
        listDisponibilidad = (ListView) findViewById(R.id.reserva_list_disponibilidad);
    }

//    public void addHabitacionDisponible(Bedroom habitacion)
//    {
//        adaptador.add(habitacion);
//    }

    public void showDisponibilidad(List<Bedroom> bedrooms) {
        addDisponibilidad(bedrooms);
        showAnimate(1);
    }

    public void hideDisponibilidad() {
        adaptador.clear();
        showAnimate(0);
    }

    private void showAnimate(int modo) {
        if (modo == 0) { //encoger
            if (visibleTextNota) {
                ViewPropertyAnimator vpa = ((ReservaPrincipal)getContext()).getReservaEsenaPrincipal().getLayoutCabecera().animate();
                vpa.translationY(-300);
                vpa.setDuration(200);
                vpa.setInterpolator(new AccelerateDecelerateInterpolator());
                vpa.start();
                visibleTextNota = false;
            }
        } else {  // alargar
            if (!visibleTextNota) {
                ViewPropertyAnimator vpa = ((ReservaPrincipal)getContext()).getReservaEsenaPrincipal().getLayoutCabecera().animate();
                vpa.translationY(0);
                vpa.setDuration(200);
                vpa.setInterpolator(new AccelerateDecelerateInterpolator());
                vpa.start();
                visibleTextNota = true;
            }
        }
    }

    private void addDisponibilidad(List<Bedroom> bedrooms) {
        for (Bedroom bedroom : bedrooms) {
            TextView textView = createTextDisponibilidad(bedroom);
            adaptador.add(bedroom);
        }
    }

    private TextView createTextDisponibilidad(Bedroom bedroom) {
        TextView textView = new TextView(getContext());
        textView.setText(bedroom.getName() + " Tamaño " + bedroom.getCapacity());
        return textView;
    }

    class ListDisponibilidadAdapter extends ArrayAdapter<Bedroom> {

        List<Bedroom> habitaciones = new ArrayList<>();

        public ListDisponibilidadAdapter(Context context, int resource, List<Bedroom> objects) {
            super(context, resource, objects);
            this.habitaciones = objects;
        }

        @Override
        public View getView(int posision, View convertView, ViewGroup parent) {
            View elemento = convertView;
            LayoutInflater contexto_inflado = ((Activity) getContext()).getLayoutInflater();
            elemento = contexto_inflado.inflate(android.R.layout.simple_list_item_1, null);
            TextView text1 = (TextView) elemento.findViewById(android.R.id.text1);
            Bedroom habitacion = habitaciones.get(posision);
            String textoMostrado = habitacion.getName() + " Disponible";
            text1.setText(textoMostrado);
            return (elemento);
        }
    }
}
