package com.wfcrc.repository;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wfcrc.pojos.Document;
import com.wfcrc.utils.VolleyCallback;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by maria on 10/26/16.
 */
public class WordPressDocumentRepository implements Repository{

    private final String DOCUMENT_GALLERY_URL = "http://wfcrc.org/wfcrc2/wp-json/wp/v2/media?parent=23";

    private Context mContext;

    private VolleyCallback mCallback;

    public WordPressDocumentRepository(Context mContext) {
        this.mContext = mContext;
        this.mCallback = new VolleyCallback(mContext);
    }

    public WordPressDocumentRepository(Context mContext, VolleyCallback callback) {
        this.mContext = mContext;
        this.mCallback = callback;
    }

    @Override
    public List<Document> getAll() throws RepositoryException{
        RequestQueue queue = Volley.newRequestQueue(mContext);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, DOCUMENT_GALLERY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mCallback.onResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mCallback.onError(error.toString());
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        return null;
    }

}
