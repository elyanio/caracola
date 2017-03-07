package com.polymitasoft.caracola.view.service;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
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

public class InternalServiceSelectorView extends TextInputLayout implements View.OnClickListener {

    InternalService service;
    @BindView(R.id.selector) TextInputEditText selector;

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
        selector.setOnClickListener(this);
    }

    public void setService(InternalService service) {
        this.service = service;
        if(service != null) {
            selector.setText(service.getName());
        }
    }

    public InternalService getService() {
        return service;
    }

    public interface OnSelectedServiceListener {
        void onSelectedService(InternalService service);
    }

    private OnSelectedServiceListener listener;

    public void setOnSelectedServiceListener(OnSelectedServiceListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        EntityDataStore<Persistable> dataStore = DataStoreHolder.INSTANCE.getDataStore();
        final List<InternalService> serviceList = dataStore
                .select(InternalService.class)
                .orderBy(InternalService.NAME.lower())
                .get()
                .toList();
        String[] services = new String[serviceList.size()];
        for(int i = 0; i < services.length; i++) {
            services[i] = serviceList.get(i).getName();
        }
        new AlertDialog.Builder(getContext())
                .setItems(services, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setService(serviceList.get(which));
                        if(listener != null) {
                            listener.onSelectedService(service);
                        }
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
