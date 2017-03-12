package com.polymitasoft.caracola.view.publicity;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.components.Colors;
import com.polymitasoft.caracola.util.Metrics;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Publicity extends AppCompatActivity {

    @BindView(R.id.publicity_main_activity) ListView publicitys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publicity_main_activity);
        init();
    }

    private void init() {
        ButterKnife.bind(this);
        ArrayList<String> marcas = new ArrayList<>();
        marcas.add("ProdYandy");
        final ListPublictyAdapter adaptador = new ListPublictyAdapter(this, R.layout.simple_list_item, marcas);
        publicitys.setAdapter(adaptador);
    }



    class ListPublictyAdapter extends ArrayAdapter<String> {
        List<String> marcas = new ArrayList<>();

        public ListPublictyAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            this.marcas = objects;
        }

        @Override
        public View getView(final int posision, View convertView, ViewGroup parent) {
            View elemento;
            LayoutInflater contexto_inflado = ((Activity) getContext()).getLayoutInflater();
            elemento = contexto_inflado.inflate(R.layout.simple_list_item_two_text, null);
            TextView primaryText = (TextView) elemento.findViewById(R.id.primary_text);
            primaryText.setTextSize(Metrics.dp(27));
            View colorStrip = (View) elemento.findViewById(R.id.color_strip);
            colorStrip.setBackgroundColor(Colors.INSTANCE.getColor(posision));
            final String marca = marcas.get(posision);
            primaryText.setText(marca);
            elemento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickItem(view);
                }
            });

            return (elemento);
        }
    }

    private void clickItem(View view) {

    }
}
