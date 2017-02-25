package com.polymitasoft.caracola.view.service;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.datamodel.InternalService;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author rainermf
 * @since 22/2/2017
 */

public class InternalServiceListDialog extends Dialog {

    @BindView(R.id.service_list) ListView serviceListView;
    private InternalServiceListAdapter listAdapter;
    private ServiceClickListener serviceClickListener;

    public InternalServiceListDialog(Context context) {
        super(context);
    }

    public InternalServiceListDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_service_list);
        ButterKnife.bind(this);

        listAdapter = new InternalServiceListAdapter(this.getContext());
        serviceListView.setAdapter(listAdapter);
        listAdapter.queryAsync();

        serviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(serviceClickListener != null) {
                    serviceClickListener.onClick((InternalService) view.getTag());
                }
                dismiss();
            }
        });
    }

    public void setServiceClickListener(ServiceClickListener serviceClickListener) {
        this.serviceClickListener = serviceClickListener;
    }

    public interface ServiceClickListener {
        void onClick(InternalService service);
    }
}
