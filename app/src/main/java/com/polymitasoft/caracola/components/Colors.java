package com.polymitasoft.caracola.components;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

/**
 * @author rainermf
 * @since 24/2/2017
 */

public enum Colors {
    INSTANCE;

    private int[] colors = new int[] { Color.BLACK };
    private int size = 1;

    @ColorInt
    public int getColor(int index) {
        return colors[(index << 2) % size];
    }

    @NonNull
    public int[] getColors() {
        return colors;
    }

    public void setColors(@NonNull int[] colors) {
        this.colors = colors;
        this.size = colors.length;
    }
}
