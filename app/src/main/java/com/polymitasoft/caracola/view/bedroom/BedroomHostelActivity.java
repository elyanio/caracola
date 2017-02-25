package com.polymitasoft.caracola.view.bedroom;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.polymitasoft.caracola.R;

/**
 * Created by asio on 2/25/2017.
 */

public class BedroomHostelActivity extends AppCompatActivity {

    private ListView bedroomHostelList;
    private BedroomHostelAdapter bedroomHostelAdapter;
    private String codeHostel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bedroom_hostel);

        codeHostel = getIntent().getExtras().getString("CODE");
        bedroomHostelAdapter = new BedroomHostelAdapter(this, codeHostel);

        bedroomHostelList = (ListView) findViewById(R.id.lista_bedroom_hostel);
        bedroomHostelList.setAdapter(bedroomHostelAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_plus:
//                abrirDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
