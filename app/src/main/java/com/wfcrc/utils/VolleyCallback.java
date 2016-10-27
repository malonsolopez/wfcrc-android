package com.wfcrc.utils;

import android.content.Context;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by maria on 10/26/16.
 */
public class VolleyCallback {

    private Context mContext;

    public VolleyCallback(Context mContext) {
        this.mContext = mContext;
    }

    public  void onResponse(String response){
        Log.d("Volley document request","Response is: "+ response);
        //store json in internal storage
        try {
            FileOutputStream fos = mContext.openFileOutput("document_gallery.json", Context.MODE_PRIVATE);
            fos.write(response.getBytes());
            fos.close();
        } catch (IOException e) {
            //TODO
            e.printStackTrace();
        }
    }

    public void onError(String error){
        //TODO
        Log.e("Volley document request", "That didn't work!");
    }
}
