package com.wfcrc.repository;

import android.content.Context;

import com.wfcrc.R;
import com.wfcrc.pojos.Document;
import com.wfcrc.sqlite.WFCRCDB;
import com.wfcrc.utils.JSONUtils;

import java.util.List;

/**
 * Created by maria on 11/10/16.
 */
public class SQLiteDocumentRepository implements Repository {

    private Context mContext;

    public SQLiteDocumentRepository(Context context){
        mContext = context;
    }

    @Override
    public List<Document> getAll() {
        WFCRCDB db = new WFCRCDB(mContext);
        return db.getDocuments();
    }
}
