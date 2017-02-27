package com.polymitasoft.caracola.view.booking;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.datamodel.Bedroom;
import com.polymitasoft.caracola.util.Metrics;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        for (Bedroom bedroom : bedrooms) {
            adaptador.addHabitacion(bedroom);
        }
    }


    public void clickEnListaDisponibilidad(Bedroom bedroom) {
        mCallback.clickEnListaDisponibilidad(dia1, dia2, bedroom);
        dismiss();
    }

    // Container Activity must implement this interface
    public interface OnDisponibilidadListener {
        public List<Bedroom> obtenerDisponibilidad(LocalDate dia1, LocalDate dia2);

        public void actualizarSeleccionEnCalendaio(LocalDate dia1, LocalDate dia2);

        public void clickEnListaDisponibilidad(LocalDate dia1, LocalDate dia2, Bedroom bedroom);
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
        private final int[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA};
        List<Bedroom> habitaciones = new ArrayList<>();
        private final Random random = new Random();

        public ListDisponibilidadAdapter(Context context, int resource, List<Bedroom> objects) {
            super(context, resource, objects);
            this.habitaciones = objects;
        }

        @Override
        public View getView(final int posision, View convertView, ViewGroup parent) {
            View elemento = convertView;
            LayoutInflater contexto_inflado = ((Activity) getContext()).getLayoutInflater();
            elemento = contexto_inflado.inflate(R.layout.simple_list_item_disponibilidad, null);
            TextView primaryText = (TextView) elemento.findViewById(R.id.primary_text);
            final Bedroom habitacion = habitaciones.get(posision);
            String textoMostrado = habitacion.getName();
            primaryText.setText(textoMostrado);

            TextView secondaryText = (TextView) elemento.findViewById(R.id.secondary_text);
            secondaryText.setText("Capacidad " + habitacion.getCapacity());

            elemento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickEnListaDisponibilidad(habitaciones.get(posision));
                }
            });

            View colorStrip = (View) elemento.findViewById(R.id.color_strip);
            colorStrip.setBackgroundColor(colors[random.nextInt(colors.length)]);

            return (elemento);
        }

        public void addHabitacion(Bedroom bedroom) {
//            add(bedroom);
            habitaciones.add(bedroom);
        }
    }
}
