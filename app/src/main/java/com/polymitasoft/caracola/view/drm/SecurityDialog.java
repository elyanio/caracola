package com.polymitasoft.caracola.view.drm;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.codetroopers.betterpickers.numberpicker.NumberPickerBuilder;
import com.codetroopers.betterpickers.numberpicker.NumberPickerDialogFragment;
import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.drm.Drm;
import com.polymitasoft.caracola.settings.Preferences;
import com.polymitasoft.caracola.util.FormatUtils;
import com.polymitasoft.caracola.util.PhoneUtils;

import org.threeten.bp.LocalDate;
import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.math.BigInteger;

import static butterknife.ButterKnife.findById;
import static com.polymitasoft.caracola.util.PhoneUtils.dial;
import static com.polymitasoft.caracola.util.PhoneUtils.sendSms;

/**
 * @author rainermf
 * @since 18/3/2017
 */

public class SecurityDialog {

    private AppCompatActivity activity;

    public SecurityDialog(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void requestActivationCode() {
        final View view = activity.getLayoutInflater().inflate(R.layout.dialog_activation_code, null);
        final EditText activationCodeText = findById(view, R.id.inputText);
        final TextView requestCodeText = findById(view, R.id.codeText);
        final Button button = findById(view, R.id.okButton);
        final String requestCode = Drm.getRequestCode();
        final TextView phone1 = findById(view, R.id.supplier_phone1_activar);
        final TextView phone2 = findById(view, R.id.supplier_phone2_activar);

        final ImageButton call1 = findById(view, R.id.call_phone1_activar);
        final ImageButton call2 = findById(view, R.id.call_phone2_activar);
        final ImageButton sms1 = findById(view, R.id.send_sms_phone1_activar);
        final ImageButton sms2 = findById(view, R.id.send_sms_phone2_activar);

        call1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dial(activity, phone1.getText().toString());
            }
        });

        call2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dial(activity, phone2.getText().toString());
            }
        });

        sms1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSms(activity, phone1.getText().toString());
            }
        });

        sms2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSms(activity, phone2.getText().toString());
            }
        });

        requestCodeText.setText(requestCode);

        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle("Password")
                .setView(view)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        notifyActivation(false);
                    }
                })
                .show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userActivationCode = activationCodeText.getText().toString();
                String encryptionKey = "Toawtef_*blyWef2";
                String encryptedString = Drm.reduceToHalf(Drm.encryptTo64String(requestCode, encryptionKey));
                if (userActivationCode.equals(encryptedString)) {
                    dialog.dismiss();
                    requestActivationTime();
                    notifyActivation(true);
                } else {
                    activationCodeText.setText("");
                }
            }
        });
    }

    private void requestActivationTime() {
        NumberPickerBuilder builder = new NumberPickerBuilder()
                .setFragmentManager(activity.getSupportFragmentManager())
                .setStyleResId(R.style.BetterPickersDialogFragment_Light)
                .setPlusMinusVisibility(View.INVISIBLE)
                .setDecimalVisibility(View.INVISIBLE)
                .addNumberPickerDialogHandler(new NumberPickerDialogFragment.NumberPickerDialogHandlerV2() {
                    @Override
                    public void onDialogNumberSet(int reference, BigInteger number, double decimal, boolean isNegative, BigDecimal fullNumber) {
                        Preferences.setEncryptedPreference("evaluation_date", FormatUtils.formatDate(LocalDate.now()));
                        Preferences.setEncryptedPreference("evaluation_days", number.toString());
                    }
                });
        builder.show();
    }

    protected void notifyActivation(boolean activated) { }
}
