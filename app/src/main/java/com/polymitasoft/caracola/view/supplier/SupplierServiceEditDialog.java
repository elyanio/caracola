package com.polymitasoft.caracola.view.supplier;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.datamodel.SupplierService;
import com.polymitasoft.caracola.util.FormatUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

/**
 * @author rainermf
 * @since 5/3/2017
 */
public class SupplierServiceEditDialog {

    private final AlertDialog.Builder builder;
    private OnClickListener okListener;
    @BindView(R.id.supplier_service_price) EditText priceView;
    @BindView(R.id.supplier_service_commission) EditText comissionView;

    public SupplierServiceEditDialog(Context context, final SupplierService supplierService) {
        builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_edit_supplier_service, null);
        ButterKnife.bind(this, view);
        priceView.setText(FormatUtils.formatMoney(supplierService.getPrice()));
        comissionView.setText(FormatUtils.formatMoney(supplierService.getComission()));

        builder.setTitle(R.string.supplier_service_edit_title)
                .setView(view)
                .setPositiveButton(R.string.ok_action_button, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EntityDataStore<Persistable> dataStore = DataStoreHolder.INSTANCE.getDataStore();
                        supplierService.setPrice(FormatUtils.parseMoney(priceView.getText().toString()));
                        supplierService.setComission(FormatUtils.parseMoney(comissionView.getText().toString()));
                        dataStore.update(supplierService);
                        if(okListener != null) {
                            okListener.onClick(dialog, which);
                        }
                    }
                })
                .setNegativeButton(R.string.cancel_action_button, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    public SupplierServiceEditDialog setOkListener(OnClickListener listener) {
        okListener = listener;
        return this;
    }

    public void show() {
        builder.show();
    }
}
