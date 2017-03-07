package com.polymitasoft.caracola.components;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

import com.polymitasoft.caracola.CaracolaApplication;
import com.polymitasoft.caracola.R;

/**
 * @author rainermf
 * @since 24/2/2017
 */
public enum Colors {
    INSTANCE;

    private final int[] colors;
    private final int size;

    @ColorInt
    public int getColor(int index) {
        return colors[index % size];
    }

    @NonNull
    public int[] getColors() {
        return colors;
    }

    Colors() {
        Resources res = CaracolaApplication.instance().getResources();
        TypedArray colorTypedArray = res.obtainTypedArray(R.array.account_colors);
        colors = new int[colorTypedArray.length()];
        for (int i = 0; i < colorTypedArray.length(); i++) {
            int color = colorTypedArray.getColor(i, Color.BLACK);
            colors[i] = color;
        }
        colorTypedArray.recycle();
        size = colors.length;
    }
}
