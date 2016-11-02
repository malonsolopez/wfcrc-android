package com.wfcrc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

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

    private HashMap<String, ArrayList<Document>> mSortedDocumentGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_gallery);
        //get document gallery
        mDocumentGallery = AppConfig.getDocumentRepository(this).getAll();//TODO: error control
        //short documents in sublist
        mSortedDocumentGallery = Document.sortDocuments(mDocumentGallery);//TODO: error control
        initDocumentGallery();
        //GA
        ((AppConfig)getApplication()).getAnalytics().sendPageView(getString(R.string.ga_document_gallery));
    }

    private void initDocumentGallery(){
        LinearLayout documentGalleryLayout = (LinearLayout) findViewById(R.id.documentGalleryLayout);
        //create list per each category
        Iterator iterator = mSortedDocumentGallery.entrySet().iterator();
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
                newDocumentList.setAdapter(new DocumentGalleryAdapter(this, documentList, true));
                documentGalleryLayout.addView(newDocumentList);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.document_gallery_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.search) {
            Intent intent = new Intent(this, DocumentGallerySearchActivity.class);
            intent.putParcelableArrayListExtra("DocumentGallery", (ArrayList<Document>) mDocumentGallery);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}
