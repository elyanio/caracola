package com.polymitasoft.caracola.components;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.itextpdf.text.DocumentException;
import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.about.AboutActivity;
import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.report.Report;
import com.polymitasoft.caracola.report.pdf.PdfReport;
import com.polymitasoft.caracola.settings.SettingsActivity;
import com.polymitasoft.caracola.view.bedroom.BedroomListActivity;
import com.polymitasoft.caracola.view.booking.BookingButtonBar;
import com.polymitasoft.caracola.view.booking.CurrentBookingsActivity;
import com.polymitasoft.caracola.view.drm.SecurityDialog;
import com.polymitasoft.caracola.view.hostel.HostelActivity;
import com.polymitasoft.caracola.view.service.InternalServiceListActivity;
import com.polymitasoft.caracola.view.supplier.ContactsActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.internal.observers.FutureObserver;
import io.reactivex.schedulers.Schedulers;

import static butterknife.ButterKnife.findById;

/**
 * @author yanier.alfonso
 */
public class DrawerActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.reserva_esenas)
    protected LinearLayout esenas_frameLayout;
    @BindView(R.id.reserva_layout_base)
    protected BookingButtonBar bookingButtonBar;

    int touchIcons = 0;

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

        ImageView drawerIcon = (ImageView) (navigationView.getHeaderView(0)).findViewById(R.id.drawerAppIcon);
        drawerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touchIcons++;
                if ((touchIcons + 1) % 5 == 0) {
                    Toast.makeText(DrawerActivity.this, "Vas a entrar al menú de activación", Toast.LENGTH_SHORT).show();
                }
                if (touchIcons % 5 == 0) {
                    new SecurityDialog(DrawerActivity.this).requestActivationCode();
                }
            }
        });
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
                startActivity(InternalServiceListActivity.class);
                break;
            case R.id.nav_contacts:
                startActivity(ContactsActivity.class);
                break;
            case R.id.nav_current_bookings:
                startActivity(CurrentBookingsActivity.class);
                break;
            case R.id.nav_share:
                startActivity(BedroomListActivity.class);
                break;
            case R.id.nav_settings:
                startActivity(SettingsActivity.class);
                break;
            case R.id.nav_hostal:
                startActivity(HostelActivity.class);
                break;
            case R.id.nav_about:
                startActivity(AboutActivity.class);
                break;
            case R.id.nav_export:
                Toast.makeText(DrawerActivity.this, "Elaborando el calendario...", Toast.LENGTH_LONG).show();
                Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> em) throws FileNotFoundException, DocumentException {
                        File file = new File(DataStoreHolder.INSTANCE.getDbFile().getParent(),
                                "report" + Calendar.getInstance().getTimeInMillis() + ".pdf");
                        new PdfReport().manipulatePdf(file.getAbsolutePath());
                        em.onNext(file.getAbsolutePath());
                        em.onComplete();
                    }
                }).subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromFile(new File(s)));
                                try {
                                    startActivity(intent);
                                } catch(ActivityNotFoundException e) {
                                    Toast.makeText(DrawerActivity.this, "No existe visor de pdf para visualizar el calendario.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
        }

        DrawerLayout drawer = findById(this, R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private <T> void startActivity(Class<T> clazz) {
        startActivity(new Intent(this, clazz));
    }
}
