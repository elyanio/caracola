package com.polymitasoft.caracola.view.drm;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.settings.Preferences;
import com.polymitasoft.caracola.util.FormatUtils;

import org.threeten.bp.LocalDate;

import static org.threeten.bp.temporal.ChronoUnit.DAYS;

/**
 * @author rainermf
 * @since 23/3/2017
 */

public class CheckActivation extends AsyncTask<Void, Void, Long> {

    private static final Long INVALID = -1L;
    private final AppCompatActivity activity;

    public CheckActivation(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Long doInBackground(Void... params) {
        return INVALID;
    }

    private Long shit() {
        String evalDateString = Preferences.getEncryptedPreference("evaluation_date");
        String evalDaysString = Preferences.getEncryptedPreference("evaluation_days");
        Log.e(CheckActivation.class.getName(), "eval Date ---------> "  + evalDateString + "      " + evalDaysString);
        if (evalDateString.trim().isEmpty()) {
            return INVALID;
        }
        if (evalDaysString.trim().isEmpty()) {
            return INVALID;
        }
        try {
            LocalDate startEvalDate = FormatUtils.parseDate(evalDateString);
            long evalDays = Long.parseLong(evalDaysString);
            LocalDate endEvalDate = startEvalDate.plusDays(evalDays);
            LocalDate today = LocalDate.now();

            if ((today.isAfter(startEvalDate) || today.isEqual(startEvalDate)) &&
                    (today.isBefore(endEvalDate) || today.isEqual(endEvalDate))) {
                return DAYS.between(today, endEvalDate);
            }
            return INVALID;
        } catch (Exception e) {
            return INVALID;
        }
    }

    @Override
    protected void onPostExecute(Long res) {
        Long result = shit();
        if (result.equals(INVALID)) {
            new AlertDialog.Builder(activity).setMessage("La aplicación no está activada.")
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            startActivationProcess();
                        }
                    })
                    .setPositiveButton(R.string.ok_action_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivationProcess();
                        }
                    })
                    .show();
        } else if (result <= 3L && Preferences.shouldAlertActivationCountdown()) {
            Preferences.updateAlertActivationCountdown();
            new AlertDialog.Builder(activity).setMessage("El tiempo de activación está a punto de expirar")
                    .setPositiveButton(R.string.ok_action_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        }
    }

    private void startActivationProcess() {
        SecurityDialog securityDialog = new SecurityDialog(activity) {
            @Override
            protected void notifyActivation(boolean activated) {
                if(!activated) {
                    activity.finishAffinity();
                }
            }
        };
        securityDialog.requestActivationCode();
    }
}
