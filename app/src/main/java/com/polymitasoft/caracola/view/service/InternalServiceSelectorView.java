package com.polymitasoft.caracola.view.service;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.polymitasoft.caracola.CaracolaApplication;
import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.datamodel.InternalService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

/**
 * @author rainermf
 * @since 22/2/2017
 */

public class InternalServiceSelectorView extends LinearLayout implements View.OnClickListener {

    InternalService service;
    @BindView(R.id.selector) TextView selector;

    public InternalServiceSelectorView(Context context) {
        super(context);
        init();
    }

    public InternalServiceSelectorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InternalServiceSelectorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.internal_service_selector, this);
        if(isInEditMode()) {
            return;
        }
        ButterKnife.bind(this);
        setOnClickListener(this);
    }

    public void setService(InternalService service) {
        this.service = service;
        if(service != null) {
            selector.setText(service.getName());
        } else {
            selector.setText(R.string.service_hint);
        }
    }

    public InternalService getService() {
        return service;
    }

    @Override
    public void onClick(View v) {
        EntityDataStore<Persistable> dataStore = CaracolaApplication.instance().getDataStore();
        final List<InternalService> serviceList = dataStore.select(InternalService.class).get().toList();
        String[] services = new String[serviceList.size()];
        for(int i = 0; i < services.length; i++) {
            services[i] = serviceList.get(i).getName();
        }
        new AlertDialog.Builder(getContext())
                .setSingleChoiceItems(services, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setService(serviceList.get(which));
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
