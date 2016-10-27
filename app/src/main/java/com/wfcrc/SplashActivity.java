package com.wfcrc;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.wfcrc.config.AppConfig;
import com.wfcrc.repository.WordPressDocumentRepository;
import com.wfcrc.utils.VolleyCallback;

import java.io.FileOutputStream;
import java.io.IOException;

import static com.wfcrc.repository.WordPressDocumentRepository.*;

public class SplashActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //retrieve document gallery
        (new WordPressDocumentRepository(this, new SplashVolleyCallback(this))).getAll();
    }

    private class SplashVolleyCallback extends VolleyCallback{

        public SplashVolleyCallback(Context mContext) {
            super(mContext);
        }

        public  void onResponse(String response){
            super.onResponse(response);
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        public void onError(String error){
            //TODO: use offline data from previous usages of the app
        }
    }

}
