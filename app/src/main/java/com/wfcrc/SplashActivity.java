package com.wfcrc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.wfcrc.repository.FTPDocumentRepository;
import com.wfcrc.repository.RepositoryException;
import com.wfcrc.utils.VolleyCallback;

public class SplashActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //show progress bar
        setContentView(R.layout.activity_splash);
        //retrieve document gallery
        try {
            //(new WordPressDocumentRepository(this, new SplashVolleyCallback(this))).getAll();
            (new FTPDocumentRepository(this, new SplashCallback())).getAll();
            //AppConfig.getDocumentRepository(this).getAll();
        } catch (RepositoryException e) {
            //TODO: use offline data from previous usages of the app
            continueLaunching();
        }
//        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//        startActivity(intent);
//        finish();
    }

    private void continueLaunching(){
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public class SplashCallback {

        public void proceedWithLaunching(){
            continueLaunching();
        }

        public void cancelLaunching(){
            //TODO
        }
    }

    private class SplashVolleyCallback extends VolleyCallback{

        public SplashVolleyCallback(Context mContext) {
            super(mContext);
        }

        public  void onResponse(String response){
            super.onResponse(response);
            continueLaunching();
        }

        public void onError(String error){
            super.onError(error);
            //TODO: use offline data from previous usages of the app
            continueLaunching();
        }
    }

}
