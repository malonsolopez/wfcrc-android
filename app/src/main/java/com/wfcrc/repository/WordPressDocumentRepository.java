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

import java.util.List;

/**
 * Created by maria on 10/26/16.
 */
public class WordPressDocumentRepository implements Repository {

    private Context mContext;

    private final String DOCUMENT_GALLERY_URL = "http://wfcrc.org/wfcrc2/wp-json/wp/v2/media?parent=23";

    public WordPressDocumentRepository(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public List<Document> getAll() {
        RequestQueue queue = Volley.newRequestQueue(mContext);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, DOCUMENT_GALLERY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //TODO
                        // Display the first 500 characters of the response string.
                        Log.d("Volley document request","Response is: "+ response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //TODO
                Log.e("Volley document request", "That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
        return null;
    }
}
