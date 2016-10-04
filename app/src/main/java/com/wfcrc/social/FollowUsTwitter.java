package com.wfcrc.social;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.myapplication.R;

/**
 * Created by maria on 9/13/16.
 */
public class FollowUsTwitter implements FollowUs{

    private Activity mContext;

    public FollowUsTwitter(Activity mContext){
        this.mContext = mContext;
    }

    public void follow(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(mContext.getString(R.string.follow_twitter)));
        mContext.startActivity(intent);
    }
}
