package com.polymitasoft.caracola.view.booking;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.components.Colors;
import com.polymitasoft.caracola.datamodel.Bedroom;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DisponibilidadDialogFragment extends DialogFragment {
    @BindView(R.id.reserva_list_disponibilidad)
    ListView listDisponibilidad;
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
        getDialog().setTitle(R.string.title_dialog_disponibilidad);
        ButterKnife.bind(this, view);
        final ListDisponibilidadAdapter adaptador = new ListDisponibilidadAdapter(getContext(), R.layout.simple_list_item, new ArrayList<Bedroom>());

        listDisponibilidad.setAdapter(adaptador);
        this.adaptador = adaptador;

        dia1 = LocalDate.of(getArguments().getInt("anno1"), getArguments().getInt("mes1"), getArguments().getInt("dia1"));
        dia2 = LocalDate.of(getArguments().getInt("anno2"), getArguments().getInt("mes2"), getArguments().getInt("dia2"));
        addDisponibilidad();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCallback.actualizarSeleccionEnCalendaio(dia1, dia2);
    }

    private void addDisponibilidad() {
        List<Bedroom> bedrooms = mCallback.obtenerDisponibilidad(dia1, dia2);
        if(bedrooms.isEmpty()){
            adaptador.setVacio(true);
            adaptador.addHabitacion(null);
        }else{
            for (Bedroom bedroom : bedrooms) {
                adaptador.addHabitacion(bedroom);
            }
        }

    }


    public void clickEnListaDisponibilidad(Bedroom bedroom) {
        dismiss();
        mCallback.clickEnListaDisponibilidad(dia1, dia2, bedroom);
    }

    // Container Activity must implement this interface
    public interface OnDisponibilidadListener {
        List<Bedroom> obtenerDisponibilidad(LocalDate dia1, LocalDate dia2);

        void actualizarSeleccionEnCalendaio(LocalDate dia1, LocalDate dia2);

        void clickEnListaDisponibilidad(LocalDate dia1, LocalDate dia2, Bedroom bedroom);
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
        private boolean vacio = false;

        public ListDisponibilidadAdapter(Context context, int resource, List<Bedroom> objects) {
            super(context, resource, objects);
            this.habitaciones = objects;
        }

        @Override
        public View getView(final int posision, View convertView, ViewGroup parent) {
            View elemento ;
            LayoutInflater contexto_inflado = ((Activity) getContext()).getLayoutInflater();
            elemento = contexto_inflado.inflate(R.layout.simple_list_item_two_text, null);
            TextView primaryText = (TextView) elemento.findViewById(R.id.primary_text);
            TextView secondaryText = (TextView) elemento.findViewById(R.id.secondary_text);
            View colorStrip = elemento.findViewById(R.id.color_strip);


            if(!vacio){
                final Bedroom habitacion = habitaciones.get(posision);
                colorStrip.setBackgroundColor(Colors.INSTANCE.getColor(habitacion.getId()));
                String textoMostrado = habitacion.getName();
                primaryText.setText(textoMostrado);
                int capacity = habitacion.getCapacity();
                if(capacity == 1){
                    secondaryText.setText("Capacidad " + capacity + " persona");
                }else{
                    secondaryText.setText("Capacidad " + capacity + " personas");
                }
                elemento.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickEnListaDisponibilidad(habitaciones.get(posision));
                    }
                });
            }else{
                primaryText.setText("No tiene cuartos disponibles");
//                primaryText.setGravity(Gravity.CENTER);
            }
            return (elemento);
        }

        public void addHabitacion(Bedroom bedroom) {
//            add(bedroom);
            habitaciones.add(bedroom);
        }

        public void setVacio(boolean b){
            vacio = b;
        }
    }
}
