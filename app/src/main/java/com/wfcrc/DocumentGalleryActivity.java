package com.wfcrc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wfcrc.pojos.Document;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DocumentGalleryActivity extends AppCompatActivity {

    private ArrayList<Document> mDocumentGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_gallery);
        initDocumentGallery();
    }

    private void initDocumentGallery(){
        LinearLayout documentGalleryLayout = (LinearLayout) findViewById(R.id.documentGalleryLayout);
        //short documents in sublist
        try {
            mDocumentGallery = Document.fromJson(loadJSONFromAsset());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HashMap<String, ArrayList<Document>> sortedDocumentGallery = Document.sortDocuments(mDocumentGallery);
        //create list per each category
        Iterator iterator = sortedDocumentGallery.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry documents = (Map.Entry)iterator.next();
            //title (category)
            LayoutInflater inflater = LayoutInflater.from(this);
            View title = inflater.inflate(R.layout.document_gallery_category_title, documentGalleryLayout, false);
            documentGalleryLayout.addView(title);
            ((TextView)title.findViewById(R.id.documentCategory)).setText(documents.getKey().toString());
            //list of documents for this category
            ArrayList<Document> documentList = (ArrayList<Document>)documents.getValue();
            if (documentList != null) {
                for (Document document:documentList) {
                    View view = inflater.inflate(R.layout.document_gallery_item, documentGalleryLayout, false);
                    documentGalleryLayout.addView(view);
                    //TODO: (ImageView) view.findViewById(R.id.documentIcon);
                    ((TextView) view.findViewById(R.id.documentTitle)).setText(document.getTitle());
                    //TODO: (ImageView) view.findViewById(R.id.documentSync);
                    View divider = this.getLayoutInflater().inflate(R.layout.document_gallery_item, null);
                    divider.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
                    divider.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                    documentGalleryLayout.addView(divider);
                }
            }
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getResources().openRawResource(R.raw.document_gallery);
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
