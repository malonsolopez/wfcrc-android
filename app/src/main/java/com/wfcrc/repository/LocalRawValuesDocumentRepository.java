package com.wfcrc.repository;

import android.content.Context;
import android.support.annotation.Nullable;

import com.wfcrc.R;
import com.wfcrc.pojos.Document;
import com.wfcrc.pojos.Program;
import com.wfcrc.utils.JSONUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maria on 10/6/16.
 */
public class LocalRawValuesDocumentRepository implements Repository {

    private Context mContext;

    public LocalRawValuesDocumentRepository(Context context){
        mContext = context;
    }

    @Override
    public List<Document> getAll() {
        String strJson = JSONUtils.loadJSONFromAsset(mContext, R.raw.document_gallery_wp);
        ArrayList<Document> documents = new ArrayList<Document>();
        try {
            JSONArray jsonArray = new JSONArray(strJson);
            JSONObject documentJson;
            documents = new ArrayList<Document>(jsonArray.length());
            // Process each result in json array, decode and convert to business object
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    documentJson = jsonArray.getJSONObject(i);
                } catch (Exception e) {
                    //TODO
                    e.printStackTrace();
                    continue;
                }

                Document document = Document.fromJson(documentJson);
                if (document != null) {
                    documents.add(document);
                }
            }
        }catch (Exception e){
            //TODO
            e.printStackTrace();
        }
        return documents;
    }

}
