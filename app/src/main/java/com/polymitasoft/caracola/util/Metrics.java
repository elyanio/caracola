package com.polymitasoft.caracola.util;

import android.content.Context;

import com.polymitasoft.caracola.CaracolaApplication;

/**
 * @author rainermf
 * @since 22/2/2017
 */

public class Metrics {

    private Metrics() {
        throw new AssertionError();
    }

    /**
     * @deprecated Use dp(int)
     */
    @Deprecated
    public static int dp(Context context, int value) {
        return (int) (value * context.getResources().getDisplayMetrics().density);
    }

    public static int dp(int value) {
        return (int) (value * CaracolaApplication.instance().getResources().getDisplayMetrics().density);
    }
}
