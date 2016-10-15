package com.wfcrc.config;

import android.app.Application;
import android.content.Context;

import com.wfcrc.analytics.Analytics;
import com.wfcrc.analytics.GAImp;
import com.wfcrc.pojos.Document;
import com.wfcrc.pojos.Program;
import com.wfcrc.repository.LocalRawValuesDocumentRepository;
import com.wfcrc.repository.LocalRawValuesProgramRepository;
import com.wfcrc.repository.Repository;

/**
 * Created by maria on 10/8/16.
 */
public class AppConfig extends Application{

    private static Repository<Document> mDocumentRepository = null;

    public static Repository<Document> getDocumentRepository(Context context) {
        if(mDocumentRepository == null)
            mDocumentRepository = new LocalRawValuesDocumentRepository(context);
        return mDocumentRepository;
    }

    private static Repository<Program> mProgramRepository = null;

    public static Repository<Program> getProgramRepository(Context context){
        if(mProgramRepository == null)
            mProgramRepository = new LocalRawValuesProgramRepository(context);
        return mProgramRepository;
    }

    private static Analytics mAnalytics = null;

    public Analytics getAnalytics(){
        if(mAnalytics == null)
            mAnalytics = new GAImp(this);
        return mAnalytics;
    }

}
