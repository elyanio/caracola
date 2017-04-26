package com.polymitasoft.caracola.view.drm;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.polymitasoft.caracola.R;
import com.polymitasoft.caracola.about.AboutActivity;
import com.polymitasoft.caracola.drm.ActivationData;
import com.polymitasoft.caracola.settings.Preferences;

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
        try {
            ActivationData activationData = ActivationData.current();
            if(activationData.isActivated()) {
                return activationData.getDays();
            }
            return INVALID;
        } catch (Exception e) {
            Log.e(CheckActivation.class.getName(), "error here, dude");
            e.printStackTrace();
            return INVALID;
        }
    }

    @Override
    protected void onPostExecute(Long result) {
        if (result.equals(INVALID)) {
            activity.startActivity(new Intent(activity, AboutActivity.class));
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
}
