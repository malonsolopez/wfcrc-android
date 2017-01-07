package com.wfcrc.repository;

import android.content.Context;

import com.wfcrc.R;
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
    public List<Document> getAll()  throws RepositoryException{
        List<Document> documents = new ArrayList<Document>();
        try {
            FileInputStream fileInputStream = mContext.openFileInput("document_gallery.json");
            String jsonStr = null;
            FileChannel fc = fileInputStream.getChannel();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            jsonStr = Charset.defaultCharset().decode(bb).toString();
            fileInputStream.close();
            documents = JSONDocumentRepository.getAll(jsonStr);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RepositoryException(e.getMessage(), e.getCause(), mContext.getString(R.string.document_gallery_error));
        }
        return documents;
    }
}
