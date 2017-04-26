package com.polymitasoft.caracola.about;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.drm.ActivationData;
import com.polymitasoft.caracola.util.FormatUtils;
import com.polymitasoft.caracola.view.drm.SecurityDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static butterknife.ButterKnife.findById;
import static com.polymitasoft.caracola.util.MailUtils.sendEmail;
import static com.polymitasoft.caracola.util.PhoneUtils.dial;
import static com.polymitasoft.caracola.util.PhoneUtils.sendSms;

public class AboutActivity extends AppCompatActivity {

    @BindView(R.id.text_phone1_activar) TextView phone1;
    @BindView(R.id.text_phone2_activar) TextView phone2;
    @BindView(R.id.text_activation) TextView activationText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        Toolbar toolbar = findById(this, R.id.toolbar);
        toolbar.setTitle("Caracola");
        setSupportActionBar(toolbar);

        updateActivationText();
    }

    private void updateActivationText() {
        ActivationData activationData = ActivationData.current();

        if (activationData.isActivated()) {
            activationText.setText("Su licencia vence el " + FormatUtils.formatDate(activationData.getEndDate()));
        } else {
            activationText.setText("Su aplicación no está activada.");
        }
    }

    @OnClick(R.id.call_phone1_activar)
    public void onPhone1Click() {
        dial(this, phone1.getText().toString());
    }

    @OnClick(R.id.call_phone2_activar)
    public void onPhone2Click() {
        dial(this, phone2.getText().toString());
    }

    @OnClick(R.id.send_sms_phone1_activar)
    public void onSms1Click() {
        sendSms(this, phone1.getText().toString());
    }

    @OnClick(R.id.send_sms_phone2_activar)
    public void onSms2Click() {
        sendSms(this, phone2.getText().toString());
    }

    @OnClick(R.id.fab)
    public void onFabClick() {
        sendEmail(AboutActivity.this, "caracola@polymitasoft.com");
    }

    @OnClick(R.id.button_activation)
    public void onActivationButtonClick() {
        new SecurityDialog(this) {
            @Override
            protected void notifyActivation(boolean activated) {
                updateActivationText();
            }
        }.show();
    }
}
