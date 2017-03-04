package com.polymitasoft.caracola.view.booking;


import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.polymitasoft.caracola.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReservaEsenaPrincipal extends LinearLayout {

    public final static int CANTIDAD_MESES_CARGADOS = 6;
    private final ReservaPrincipal reservaPrincipal;

    //controles
    private LinearLayout layoutCabecera;
    private GridView diasSemanasGrid;
    private ViewPager deslizador;
    private ArrayList<ReservaPanelHabitacion> panelesHabitaciones = new ArrayList<>();
    private ReservaPanelHabitacion reservaPanelHabitacionActual;
    private TextView notaDeslizante;
    private int currentPosition = 0;

    public ReservaEsenaPrincipal(Context context) {
        super(context);
        reservaPrincipal = (ReservaPrincipal) getContext();
        inicializar();
        obtenerControles();
        configurarControles();
    }

    private void inicializar() {
        String infladorServicio = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater asioInflador = (LayoutInflater) getContext().getSystemService(infladorServicio);
        asioInflador.inflate(R.layout.reserva_principal_escena, this, true);
    }

    private void obtenerControles() {
        notaDeslizante = (TextView) findViewById(R.id.reserva_nota_deslizante);
        layoutCabecera = (LinearLayout) findViewById(R.id.reserva_cabecera);
        deslizador = (ViewPager) findViewById(R.id.reserva_panel_deslizador);
        diasSemanasGrid = (GridView) findViewById(R.id.reserva_dia_semanas);
    }

    private void configurarControles() {
        //poner dias de la semana encima
        List<String> list = Arrays.asList("LUN", "MAR", "MIÉ", "JUE", "VIE", "SÁB", "DOM");
        ArrayAdapter<String> ada1 = new GridDiasAdapter(getContext(), R.layout.simple_item_text_center, list);
        diasSemanasGrid.setAdapter(ada1);
        diasSemanasGrid.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        deslizador.setAdapter(new Adaptador_Pag_Vista());
        layoutCabecera.setY(getY() - 60);
    }

    public void seleccionarPanelManual(int pos) {
        deslizador.setCurrentItem(pos, true);
        reservaPanelHabitacionActual = panelesHabitaciones.get(pos);
        reservaPanelHabitacionActual.limpiarTodo();
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    //eventos
    public void click_fisicaR() {
        reservaPanelHabitacionActual.click_fisicaR();
    }

    public void clickEliminarR() {
        reservaPanelHabitacionActual.clickEliminarR();
    }

    public LinearLayout getLayoutCabecera() {
        return layoutCabecera;
    }

    public ReservaPanelHabitacion getReservaPanelHabitacionActual() {
        return reservaPanelHabitacionActual;
    }

    public TextView getNotaDeslizante() {
        return notaDeslizante;
    }


    public class Adaptador_Pag_Vista extends PagerAdapter {

        @Override
        public int getCount() {
//            return reservaPrincipal.BEDROOM_AMOUNT + 1;
            return 1;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView;
            currentPosition = position;
            if (position == 0) {
                itemView = new ReservaPanelHabitacion(getContext(), null);
                container.addView(itemView);
                panelesHabitaciones.add(position, (ReservaPanelHabitacion) itemView);
                reservaPanelHabitacionActual = (ReservaPanelHabitacion) itemView;
            } else {
                itemView = new ReservaPanelHabitacion(getContext(), reservaPrincipal.getBedrooms().get(position));
                container.addView(itemView);
                panelesHabitaciones.add(position, (ReservaPanelHabitacion) itemView);
                reservaPanelHabitacionActual = (ReservaPanelHabitacion) itemView;
            }
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }


    class GridDiasAdapter extends ArrayAdapter<String> {

        private final List<String> listDias;

        public GridDiasAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            this.listDias = objects;
        }

        @Override
        public View getView(final int posision, View convertView, ViewGroup parent) {
            View elemento ;
            LayoutInflater contexto_inflado = ((Activity) getContext()).getLayoutInflater();
            elemento = contexto_inflado.inflate(R.layout.simple_item_text_center, null);
            TextView textGrid1 = (TextView) elemento.findViewById(R.id.textGrid1);
            String dia = listDias.get(posision);
            String textoMostrado = dia;
            textGrid1.setText(textoMostrado);
            return elemento;
        }
    }
}
