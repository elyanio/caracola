package com.polymitasoft.caracola.view.booking;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.datamodel.Bedroom;
import com.polymitasoft.caracola.util.Metrics;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DisponibilidadDialogFragment extends DialogFragment {
    @BindView(R.id.reserva_list_disponibilidad) ListView listDisponibilidad;
    private ListDisponibilidadAdapter adaptador;
    private LocalDate dia1;
    private LocalDate dia2;

    public static DisponibilidadDialogFragment newInstance(LocalDate dia1, LocalDate dia2) {
        DisponibilidadDialogFragment fragment = new DisponibilidadDialogFragment();
        Bundle args = new Bundle();
        args.putInt("dia1", dia1.getDayOfMonth());
        args.putInt("mes1", dia1.getMonthValue());
        args.putInt("anno1", dia1.getYear());
        args.putInt("dia2", dia2.getDayOfMonth());
        args.putInt("mes2", dia2.getMonthValue());
        args.putInt("anno2", dia2.getYear());
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.reserva_disponibilidad, container);
        ButterKnife.bind(this,view);
        ListDisponibilidadAdapter adaptador = new ListDisponibilidadAdapter(getContext(), android.R.layout.simple_list_item_1, new ArrayList<Bedroom>());
        listDisponibilidad.setAdapter(adaptador);
        this.adaptador = adaptador;

        dia1 = LocalDate.of(getArguments().getInt("anno1"), getArguments().getInt("mes1"), getArguments().getInt("dia1"));
        dia2 = LocalDate.of(getArguments().getInt("anno2"), getArguments().getInt("mes2"), getArguments().getInt("dia2"));
        addDisponibilidad();
        listDisponibilidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return view;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        mCallback.actualizarSeleccionEnCalendaio(dia1,dia2);
    }
//    public void showDisponibilidad(List<Bedroom> bedrooms) {
//        addDisponibilidad(bedrooms);
//        showAnimate(1);
//    }
//
//    public void hideDisponibilidad() {
//        adaptador.clear();
//        showAnimate(0);
//    }

//    private void showAnimate(int modo) {
//        if (modo == 0) { //encoger
//            if (visibleTextNota) {
//                ViewPropertyAnimator vpa = ((ReservaPrincipal)getContext()).getReservaEsenaPrincipal().getLayoutCabecera().animate();
//                vpa.translationY(-600);
//                vpa.setDuration(200);
//                vpa.setInterpolator(new AccelerateDecelerateInterpolator());
//                vpa.start();
//                visibleTextNota = false;
//            }
//        } else {  // alargar
//            if (!visibleTextNota) {
//                ViewPropertyAnimator vpa = ((ReservaPrincipal)getContext()).getReservaEsenaPrincipal().getLayoutCabecera().animate();
//                vpa.translationY(0);
//                vpa.setDuration(200);
//                vpa.setInterpolator(new AccelerateDecelerateInterpolator());
//                vpa.start();
//                visibleTextNota = true;
//            }
//        }
//    }

    private void addDisponibilidad() {
        List<Bedroom> bedrooms = mCallback.obtenerDisponibilidad(dia1, dia2);
        for (Bedroom bedroom : bedrooms) {
            adaptador.add(bedroom);
        }
        adaptador.notifyDataSetInvalidated();
    }


    // Container Activity must implement this interface
    public interface OnDisponibilidadListener {
        public List<Bedroom> obtenerDisponibilidad(LocalDate dia1, LocalDate dia2);
        public void actualizarSeleccionEnCalendaio(LocalDate dia1, LocalDate dia2);
    }

    OnDisponibilidadListener mCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (DisponibilidadDialogFragment.OnDisponibilidadListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
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
            String textoMostrado = habitacion.getName();
            text1.setText(textoMostrado);
            text1.setPadding(Metrics.dp(getContext(),30),Metrics.dp(getContext(),2),Metrics.dp(getContext(),2),Metrics.dp(getContext(),5));
//            text1.setTextSize(Metrics.dp(getContext(),10));
            return (elemento);
        }
    }
}
