package com.wfcrc.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;

import com.wfcrc.R;

/**
 * Created by maria on 11/15/16.
 */
public class FormUtils {

    public static void sendFormByEmail(Activity activity, String to, String subject, String body, String attachmentPath, int requestCode)
            throws ActivityNotFoundException {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{to});
        i.putExtra(Intent.EXTRA_SUBJECT, subject);
        i.putExtra(Intent.EXTRA_TEXT, body);
        if(attachmentPath != null)
            i .putExtra(Intent.EXTRA_STREAM, Uri.parse(attachmentPath));
        //activity.startActivity(Intent.createChooser(i, activity.getResources().getString(R.string.volunteer_email_chooser)));
        activity.startActivityForResult(Intent.createChooser(i, activity.getResources().getString(R.string.volunteer_email_chooser)),
                requestCode);
    }

    public static void attachFile(Activity activity, int requestCode){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        activity.startActivityForResult(intent, requestCode);
    }
}
