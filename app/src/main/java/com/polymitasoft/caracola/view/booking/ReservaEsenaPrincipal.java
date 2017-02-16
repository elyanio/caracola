package com.polymitasoft.caracola.view.booking;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.polymitasoft.caracola.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReservaEsenaPrincipal extends LinearLayout {

    public final static int CANTIDAD_MESES_CARGADOS = 6;
    private final ReservaPrincipal reservaPrincipal;

    //controles
    private FrameLayout layoutCabecera;
    private GridView diasSemanasGrid;
    private ViewPager deslizador;
    private ArrayList<ReservaPanelHabitacion> panelesHabitaciones = new ArrayList<>(ReservaPrincipal.BEDROOM_AMOUNT + 1);
    private ReservaPanelHabitacion reservaPanelHabitacionActual;

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
        layoutCabecera = (FrameLayout) findViewById(R.id.reserva_cabecera);
        deslizador = (ViewPager) findViewById(R.id.reserva_panel_deslizador);
        diasSemanasGrid = (GridView) findViewById(R.id.reserva_dia_semanas);
    }

    private void configurarControles() {
        //poner dias de la semana encima
        List<String> list = Arrays.asList("L", "M", "M", "J", "V", "S", "D");
        ArrayAdapter ada1 = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, list);
        diasSemanasGrid.setAdapter(ada1);
        diasSemanasGrid.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        deslizador.setAdapter(new Adaptador_Pag_Vista());
    }

    public void seleccionarPanelManual(int pos) {
        deslizador.setCurrentItem(pos, true);
        reservaPanelHabitacionActual = panelesHabitaciones.get(pos);
        reservaPanelHabitacionActual.limpiarTodo();
    }

    //eventos
    public void click_fisicaR() {
        reservaPanelHabitacionActual.click_fisicaR();
    }

    public void clickEliminarR() {
        reservaPanelHabitacionActual.clickEliminarR();
    }

    public FrameLayout getLayoutCabecera() {
        return layoutCabecera;
    }

    public ReservaPanelHabitacion getReservaPanelHabitacionActual() {
        return reservaPanelHabitacionActual;
    }

    public class Adaptador_Pag_Vista extends PagerAdapter
    {

        @Override
        public int getCount() {
//            return reservaPrincipal.BEDROOM_AMOUNT + 1;
            return 1;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {

            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView;
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
}
