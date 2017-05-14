package com.polymitasoft.caracola.components;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.view.about.AboutActivity;
import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.report.pdf.PdfReport;
import com.polymitasoft.caracola.settings.SettingsActivity;
import com.polymitasoft.caracola.view.bedroom.BedroomListActivity;
import com.polymitasoft.caracola.view.booking.CurrentBookingsActivity;
import com.polymitasoft.caracola.view.hostel.HostelActivity;
import com.polymitasoft.caracola.view.service.InternalServiceListActivity;
import com.polymitasoft.caracola.view.supplier.ContactsActivity;

import java.io.File;
import java.util.Calendar;

import butterknife.ButterKnife;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static butterknife.ButterKnife.findById;

/**
 * @author yanier.alfonso
 */
public abstract class DrawerActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    protected final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
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

    protected abstract @LayoutRes int getLayout();

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
                disposables.add(exportPdf().subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnError(onPdfCreationError())
                        .subscribe(displayPdf()));
        }

        DrawerLayout drawer = findById(this, R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private Single<File> exportPdf() {
        return Single.create(new SingleOnSubscribe<File>() {
            @Override
            public void subscribe(SingleEmitter<File> em) throws Exception {
                File file = new File(DataStoreHolder.INSTANCE.getDbFile().getParent(),
                        "report" + Calendar.getInstance().getTimeInMillis() + ".pdf");
                new PdfReport().manipulatePdf(file.getAbsolutePath());
                em.onSuccess(file);
            }
        });
    }

    private Consumer<File> displayPdf() {
        return new Consumer<File>() {
            @Override
            public void accept(File file) throws Exception {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromFile(file));
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(DrawerActivity.this, "No existe visor de pdf para visualizar el calendario.", Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    private Consumer<Throwable> onPdfCreationError() {
        return new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Toast.makeText(DrawerActivity.this, "Error al crear el calendario.", Toast.LENGTH_LONG).show();
            }
        };
    }

    private <T> void startActivity(Class<T> clazz) {
        startActivity(new Intent(this, clazz));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }
}
