package com.polymitasoft.caracola.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * @author rainermf
 * @since 8/4/2017
 */

public class PhoneUtils {

    public static void dial(Context context, String phoneNumber) {
        context.startActivity(getDialIntent(phoneNumber));
    }

    public static void sendSms(Context context, String phoneNumber, String content) {
        context.startActivity(getSendSmsIntent(phoneNumber, content));
    }

    public static void sendSms(Context context, String phoneNumber) {
        sendSms(context, phoneNumber, "");
    }

    public static Intent getSendSmsIntent(String phoneNumber, String content) {
        Uri uri = Uri.parse("smsto:" + phoneNumber);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", content);
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    public static Intent getDialIntent(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }
}
