package com.polymitasoft.caracola.view.drm;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.polymitasoft.caracola.BuildConfig;
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
    private static final String EVALUATION_DATE_KEY = "evaluation_date";
    private static final String ACTIVATION_DATE_KEY = "evaluation_date";
    private static final String ACTIVATION_DAYS_KEY = "evaluation_days";
    private static final String EVALUATION_DAYS_VALUE = "2";

    public CheckActivation(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Long doInBackground(Void... params) {
        String evalDateString = Preferences.getEncryptedPreference(ACTIVATION_DATE_KEY);
        String evalDaysString = Preferences.getEncryptedPreference(ACTIVATION_DAYS_KEY);
        if (evalDateString.trim().isEmpty() || evalDaysString.trim().isEmpty()) {
            String defaultValue = FormatUtils.formatDate(LocalDate.now());
            if(!Preferences.existsDbPreference(EVALUATION_DATE_KEY)) {
                Preferences.setDbPreference(EVALUATION_DATE_KEY, defaultValue);
            }
            evalDateString = Preferences.getDbPreference(EVALUATION_DATE_KEY, defaultValue);
            evalDaysString = EVALUATION_DAYS_VALUE;
        }
        if(BuildConfig.DEBUG) {
            Log.e(CheckActivation.class.getName(), "eval Date ---------> "  + evalDateString + "      " + evalDaysString);
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
    protected void onPostExecute(Long result) {
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
            new AlertDialog.Builder(activity).setMessage("Queda " + result + " días para que expire el tiempo de activación.")
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
