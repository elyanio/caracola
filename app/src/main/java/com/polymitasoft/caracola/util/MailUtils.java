package com.polymitasoft.caracola.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * @author Rainer Martínez Fraga <rmf@polymitasoft.com>
 * @since 4/25/2017.
 */

public class MailUtils {

    public static void sendEmail(Context context, String emailAddress) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{ emailAddress });
        emailIntent.setType("message/rfc822");
        context.startActivity(Intent.createChooser(emailIntent, "Email"));
    }
}
