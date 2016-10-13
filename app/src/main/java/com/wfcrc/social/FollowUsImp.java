package com.wfcrc.social;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.wfcrc.R;

/**
 * Created by maria on 9/13/16.
 */
public class FollowUsImp implements FollowUs{

    private Activity mContext;

    public FollowUsImp(Activity mContext){
        this.mContext = mContext;
    }

    public void follow(String socialNetwork){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(socialNetwork));
        mContext.startActivity(intent);
    }
}
