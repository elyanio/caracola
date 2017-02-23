package com.polymitasoft.caracola.util;

import android.content.Context;

/**
 * @author rainermf
 * @since 22/2/2017
 */

public class Metrics {

    private Metrics() {
        throw new AssertionError();
    }

    public static int dp(Context context, int value) {
        return (int) (value * context.getResources().getDisplayMetrics().density);
    }
}
