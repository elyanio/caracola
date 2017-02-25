package com.polymitasoft.caracola.view.bedroom;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.polymitasoft.caracola.R;

/**
 * Created by asio on 2/25/2017.
 */

public class BedroomHostelActivity extends AppCompatActivity {

    private ListView bedroomHostelList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        bedroomHostelList= (ListView) findViewById(R.id.lista_bedroom_hostel);
    }
}
