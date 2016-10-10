package com.wfcrc.utils;

import android.content.Context;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by maria on 10/10/16.
 */
public class JSONUtils {

    @Nullable
    public static String loadJSONFromAsset(Context context, int rawResource) {
        String json = null;
        try {
            InputStream is = context.getResources().openRawResource(rawResource);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
