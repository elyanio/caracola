package com.polymitasoft.caracola.components;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.settings.SettingsActivity;
import com.polymitasoft.caracola.view.bedroom.BedroomListActivity;
import com.polymitasoft.caracola.view.booking.BookingButtonBar;
import com.polymitasoft.caracola.view.booking.CurrentBookingsActivity;
import com.polymitasoft.caracola.view.hostel.HostelActivity;
import com.polymitasoft.caracola.view.service.InternalServiceListActivity;
import com.polymitasoft.caracola.view.supplier.ExternalServiceListActivity;
import com.polymitasoft.caracola.view.supplier.SupplierListActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static butterknife.ButterKnife.findById;

/**
 * @author yanier.alfonso
 */
public class DrawerActivity extends AppCompatActivity implements
    NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.reserva_esenas) protected LinearLayout esenas_frameLayout;
    @BindView(R.id.reserva_layout_base) protected BookingButtonBar bookingButtonBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserva_principal_activity);
        setTitle("");
        ButterKnife.bind(this);

        Toolbar toolbar = findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findById(this, R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findById(this, R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findById(this, R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_internal_service:
                startActivity(new Intent(this, InternalServiceListActivity.class));
                break;
            case R.id.nav_contacts:
                startActivity(new Intent(this, ExternalServiceListActivity.class));
                break;
            case R.id.nav_current_bookings:
                startActivity(new Intent(this, CurrentBookingsActivity.class));
                break;
            case R.id.nav_manage:
                break;
            case R.id.nav_share:
                startActivity(new Intent(this, BedroomListActivity.class));
                break;
            case R.id.nav_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.nav_hostal:
                startActivity(new Intent(this, HostelActivity.class));
        }

        DrawerLayout drawer = findById(this, R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
