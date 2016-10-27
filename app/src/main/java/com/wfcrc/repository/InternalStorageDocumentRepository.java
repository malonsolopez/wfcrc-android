package com.wfcrc.repository;

import android.content.Context;

import com.wfcrc.pojos.Document;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maria on 10/26/16.
 */
public class InternalStorageDocumentRepository implements Repository{

    private Context mContext;

    public InternalStorageDocumentRepository(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public List<Document> getAll() {
        ArrayList<Document> documents = new ArrayList<Document>();
        try {
            FileInputStream fileInputStream = mContext.openFileInput("document_gallery.json");
            String jsonStr = null;
            FileChannel fc = fileInputStream.getChannel();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            jsonStr = Charset.defaultCharset().decode(bb).toString();
            fileInputStream.close();
            JSONArray jsonArray = new JSONArray(jsonStr);
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
        } catch (Exception e) {
            //TODO
            e.printStackTrace();
        }
        return documents;
    }
}
