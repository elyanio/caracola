package com.polymitasoft.caracola.view.supplier;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.polymitasoft.caracola.CaracolaApplication;
import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.datamodel.Supplier;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static butterknife.ButterKnife.apply;
import static butterknife.ButterKnife.findById;
import static java.util.Arrays.asList;

public class SupplierInfoFragment extends Fragment {

    public static final String ARG_SUPPLIER_ID = "supplierId";
    static final ButterKnife.Setter<TextView, Boolean> EDITABLE = new ButterKnife.Setter<TextView, Boolean>() {
        @Override
        public void set(TextView view, Boolean value, int index) {
            view.setInputType(EditorInfo.TYPE_NULL);
            view.setFocusable(false);
        }
    };
    @BindView(R.id.supplier_name) TextInputEditText nameView;
    @BindView(R.id.supplier_phone1) TextInputEditText phone1View;
    @BindView(R.id.supplier_phone2) TextInputEditText phone2View;
    @BindView(R.id.supplier_email) TextInputEditText emailView;
    @BindView(R.id.supplier_address) TextInputEditText addressView;
    @BindView(R.id.supplier_description) TextInputEditText descriptionView;
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
        View view = inflater.inflate(R.layout.activity_edit_supplier, container, false);
        ButterKnife.bind(this, view);
        apply(asList(nameView, phone1View, phone2View, emailView, addressView, descriptionView), EDITABLE, false);
        findById(view, R.id.supplier_services).setVisibility(View.GONE);

        int supplierId = getArguments().getInt(ARG_SUPPLIER_ID);
        supplier = CaracolaApplication.instance().getDataStore().findByKey(Supplier.class, supplierId);

        nameView.setText(supplier.getName());
        List<String> phoneNumbers = supplier.getPhoneNumbers();
        if(phoneNumbers.size() > 0) {
            phone1View.setText(phoneNumbers.get(0));
        }
        if(phoneNumbers.size() > 1) {
            phone2View.setText(phoneNumbers.get(1));
        }
        emailView.setText(supplier.getEmailAddress());
        addressView.setText(supplier.getAddress());
        descriptionView.setText(supplier.getDescription());

        return view;
    }

    @OnClick(R.id.supplier_email)
    public void onEmailClick() {
        Toast.makeText(getContext(), "Send email", Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.supplier_phone1, R.id.supplier_phone2})
    public void onPhoneClick() {
        Toast.makeText(getContext(), "Call this guy", Toast.LENGTH_SHORT).show();
    }
}