package com.wfcrc.social;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by maria on 9/13/16.
 */
public class Share {

    private Activity mContext;
    private String subject;
    private String body;

    public Share(Activity mContext, String subject, String body) {
        this.mContext = mContext;
        this.subject = subject;
        this.body = body;
    }

    public void share(){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
        mContext.startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
}
