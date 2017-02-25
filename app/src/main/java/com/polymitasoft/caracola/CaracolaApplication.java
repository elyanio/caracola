package com.polymitasoft.caracola;

import android.app.Application;
import android.content.res.Resources;
import android.content.res.TypedArray;

import com.jakewharton.threetenabp.AndroidThreeTen;
import com.polymitasoft.caracola.components.Colors;

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
        Colors.INSTANCE.setColors(getRibbonColors());
    }

    private int[] getRibbonColors(){
        Resources res = getResources();
        TypedArray colorTypedArray = res.obtainTypedArray(R.array.account_colors);
        int[] colorOptions = new int[colorTypedArray.length()];
        for (int i = 0; i < colorTypedArray.length(); i++) {
            int color = colorTypedArray.getColor(i, getResources().getColor(R.color.colorPrimary));
            colorOptions[i] = color;
        }
        colorTypedArray.recycle();
        return colorOptions;
    }
}
