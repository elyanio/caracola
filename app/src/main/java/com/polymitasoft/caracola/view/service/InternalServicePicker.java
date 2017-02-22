package com.polymitasoft.caracola.view.service;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * @author rainermf
 * @since 21/2/2017
 */

public class InternalServicePicker extends TextView implements View.OnClickListener {

    public InternalServicePicker(Context context) {
        super(context);
        init();
    }

    public InternalServicePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InternalServicePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    @Override
    public void onClick(View v) {

    }
}
