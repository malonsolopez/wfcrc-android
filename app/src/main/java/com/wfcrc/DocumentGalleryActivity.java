package com.wfcrc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wfcrc.adapters.DocumentGalleryAdapter;
import com.wfcrc.config.AppConfig;
import com.wfcrc.pojos.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DocumentGalleryActivity extends AppCompatActivity {

    private List<Document> mDocumentGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_gallery);
        mDocumentGallery = AppConfig.getDocumentRepository(this).getAll();
        initDocumentGallery();
        //GA
        ((AppConfig)getApplication()).getAnalytics().sendPageView(getString(R.string.ga_document_gallery));
    }

    private void initDocumentGallery(){
        LinearLayout documentGalleryLayout = (LinearLayout) findViewById(R.id.documentGalleryLayout);
        //short documents in sublist
        HashMap<String, ArrayList<Document>> sortedDocumentGallery = Document.sortDocuments(mDocumentGallery);
        //create list per each category
        Iterator iterator = sortedDocumentGallery.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry documents = (Map.Entry)iterator.next();
            //list of documents for this category
            ArrayList<Document> documentList = (ArrayList<Document>)documents.getValue();
            //the category (title) is going to be the first element in form of an imaginary document, so the adapter can draw it as the title
            documentList.add(0, new Document(documents.getKey().toString(), null, null, null));
            if (documentList != null) {
                RecyclerView newDocumentList = new RecyclerView(this);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
                newDocumentList.setLayoutManager(mLayoutManager);
                newDocumentList.setAdapter(new DocumentGalleryAdapter(this, documentList));
                documentGalleryLayout.addView(newDocumentList);
            }
        }
    }

}
