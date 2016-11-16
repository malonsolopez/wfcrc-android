package com.wfcrc.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by maria on 11/15/16.
 */
public class FormUtils {

    public static void sendFormByEmail(Activity activity, String subject, String body, String attachmentPath) throws ActivityNotFoundException {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"malonsolopez@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, subject);
        i.putExtra(Intent.EXTRA_TEXT, body);
        i .putExtra(Intent.EXTRA_STREAM, attachmentPath);
        activity.startActivity(Intent.createChooser(i, "Send mail..."));
    }

    public static void attachFile(Activity activity, int requestCode){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        activity.startActivityForResult(intent, requestCode);
    }
}
