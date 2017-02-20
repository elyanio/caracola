package com.polymitasoft.caracola;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;

import java.util.Locale;

/**
 * @author rainermf
 * @since 20/2/2017
 */
public class CaracolaApplication extends Application {

    @Override
    public void onCreate() {
        AndroidThreeTen.init(this);
        Locale.setDefault(new Locale("es"));
    }
}
