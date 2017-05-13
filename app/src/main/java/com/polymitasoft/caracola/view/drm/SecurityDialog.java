package com.polymitasoft.caracola.view.drm;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.numberpicker.NumberPickerBuilder;
import com.codetroopers.betterpickers.numberpicker.NumberPickerDialogFragment;
import com.polymitasoft.caracola.BuildConfig;
import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.drm.Drm;
import com.polymitasoft.caracola.settings.Preferences;
import com.polymitasoft.caracola.util.FormatUtils;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;
import java.math.BigInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author rainermf
 * @since 18/3/2017
 */

public class SecurityDialog {

    private AppCompatActivity activity;
    private AlertDialog dialog;
    @BindView(R.id.activationCodeText) TextInputEditText activationCodeText;
    @BindView(R.id.requestCodeText) TextView requestCodeText;
    private final String requestCode;

    public SecurityDialog(AppCompatActivity activity) {
        this.activity = activity;
        View view = activity.getLayoutInflater().inflate(R.layout.dialog_activation_code, null);
        dialog = new AlertDialog.Builder(activity)
                .setTitle("Activación")
                .setView(view)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        notifyActivation(false);
                    }
                })
                .create();
        ButterKnife.bind(this, view);
        requestCode = Drm.getRequestCode();
        requestCodeText.setText(requestCode);
    }

    public void show() {
        dialog.show();
    }

    @OnClick(R.id.requestCodeText)
    void copyCode() {
        ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Activation request code", requestCode);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(activity, "Se ha copiado el código al portapapeles", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.okButton)
    void okAction() {
        String userActivationCode = activationCodeText.getText().toString();
        String encryptedString = Drm.generateCode(requestCode);
        if (userActivationCode.equals(encryptedString)) {
            dialog.dismiss();
            requestActivationTime();
        } else {
            if (BuildConfig.DEBUG) {
                Log.e(SecurityDialog.class.getName(), String.format("Request code: %s.", requestCode));
                Log.e(SecurityDialog.class.getName(), String.format("Expected: %s, Found: %s.", encryptedString, userActivationCode));
            }
            activationCodeText.setText("");
        }
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
                        notifyActivation(true);
                    }
                });
        builder.show();
    }

    protected void notifyActivation(boolean activated) {
    }
}
