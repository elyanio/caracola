package com.polymitasoft.caracola.view.service;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.polymitasoft.caracola.datamodel.InternalService;

/**
 * @author rainermf
 * @since 22/2/2017
 */

public class ServiceSelectorView extends TextView implements View.OnClickListener {

    InternalService service;

    public ServiceSelectorView(Context context) {
        super(context);
        init();
    }

    public ServiceSelectorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ServiceSelectorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnClickListener(this);
    }

    public void setService(InternalService service) {
        this.service = service;
        if(service != null) {
            setText(service.getName());
        } else {
            setText("Seleccione un servicio");
        }
    }

    public InternalService getService() {
        return service;
    }

    @Override
    public void onClick(View v) {
        ServiceListDialog dialog = new ServiceListDialog(getContext());
        dialog.setServiceClickListener(new ServiceListDialog.ServiceClickListener() {
            @Override
            public void onClick(InternalService service) {
                setService(service);
            }
        });
        dialog.show();
    }
}
