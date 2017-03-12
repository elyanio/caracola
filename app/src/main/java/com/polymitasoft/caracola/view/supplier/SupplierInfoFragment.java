package com.polymitasoft.caracola.view.supplier;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.datamodel.Supplier;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static butterknife.ButterKnife.findById;

public class SupplierInfoFragment extends Fragment {

    public static final String ARG_SUPPLIER_ID = "supplierId";
    @BindView(R.id.supplier_name) TextView nameView;
    @BindView(R.id.supplier_phone1) TextView phone1View;
    @BindView(R.id.supplier_phone2) TextView phone2View;
    @BindView(R.id.supplier_email) TextView emailView;
    @BindView(R.id.supplier_address) TextView addressView;
    private Supplier supplier;

    public static SupplierInfoFragment newInstance(Supplier supplier) {

        Bundle args = new Bundle();
        args.putInt(ARG_SUPPLIER_ID, supplier.getId());

        SupplierInfoFragment fragment = new SupplierInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_supplier_info, container, false);
        ButterKnife.bind(this, view);
        int supplierId = getArguments().getInt(ARG_SUPPLIER_ID);
        supplier = DataStoreHolder.INSTANCE.getDataStore().findByKey(Supplier.class, supplierId);

        nameView.setText(supplier.getName());
        List<String> phoneNumbers = supplier.getPhoneNumbers();
        if (phoneNumbers.size() > 0 && !phoneNumbers.get(0).trim().isEmpty()) {
            phone1View.setText(phoneNumbers.get(0));
        } else {
            hide(findById(view, R.id.layout_phone1), findById(view, R.id.label_phone1));
        }
        if (phoneNumbers.size() > 1 && !phoneNumbers.get(1).trim().isEmpty()) {
            phone2View.setText(phoneNumbers.get(1));
        } else {
            hide(findById(view, R.id.layout_phone2), findById(view, R.id.label_phone2));
        }
        if (!supplier.getEmailAddress().trim().isEmpty()) {
            emailView.setText(supplier.getEmailAddress());
        } else {
            hide(findById(view, R.id.layout_email), findById(view, R.id.label_email));
        }
        if (!supplier.getAddress().trim().isEmpty()) {
            addressView.setText(supplier.getAddress());
        } else {
            hide(findById(view, R.id.label_address), addressView);
        }

        return view;
    }

    private void hide(View view1, View view2) {
        view1.setVisibility(View.GONE);
        view2.setVisibility(View.GONE);
    }

    @OnClick(R.id.send_email)
    public void onEmailClick() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{supplier.getEmailAddress()});
        emailIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(emailIntent, "Email"));
    }

    @OnClick(R.id.call_phone1)
    public void onPhone1Click() {
        openPhoneApp(supplier.getPhoneNumbers().get(0));
    }

    @OnClick(R.id.call_phone2)
    public void onPhone2Click() {
        openPhoneApp(supplier.getPhoneNumbers().get(1));
    }

    @OnClick(R.id.send_sms_phone1)
    public void onSms1Click() {
        openSmsApp(supplier.getPhoneNumbers().get(0));
    }

    @OnClick(R.id.send_sms_phone2)
    public void onSms2Click() {
        openSmsApp(supplier.getPhoneNumbers().get(1));
    }

    private void openPhoneApp(String number) {
        Intent dialIntent = new Intent();
        dialIntent.setAction(Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse("tel:" + number));
        startActivity(dialIntent);
    }

    private void openSmsApp(String number) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra("address", number);
        intent.setType("vnd.android-dir/mms-sms");
        startActivity(intent);
    }
}