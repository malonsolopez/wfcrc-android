package com.wfcrc;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wfcrc.adapters.DocumentGalleryAdapter;
import com.wfcrc.pojos.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DocumentGallerySearchActivity extends AppCompatActivity {

    private List<Document> mDocumentGallery;

    private HashMap<String, ArrayList<Document>> mSortedDocumentGallery;

    private boolean isFiltering = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_gallery_search);
        mDocumentGallery = getIntent().getParcelableArrayListExtra("DocumentGallery");
        mSortedDocumentGallery = Document.sortDocuments(mDocumentGallery);
        createFilterList();
        ((Button)findViewById(R.id.cancelFilter)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelFilter();
            }
        });
    }

    public void createFilterList(){
        Set<String> categories = mSortedDocumentGallery.keySet();
        final ListView filterList = new ListView(this);
        filterList.setVisibility(View.VISIBLE);
        ArrayAdapter<String> filterListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>(categories));
        filterList.setAdapter(filterListAdapter);
        filterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                isFiltering = true;
                //get selected category
                String selectedCategory = (String)adapterView.getItemAtPosition(i);
                //get documents for that category
                ArrayList<Document> filteredDocuments = (ArrayList<Document>) mSortedDocumentGallery.get(selectedCategory);
                //set new title and show cancel button
                ((TextView)findViewById(R.id.categoryFilterTitle)).setText(selectedCategory);
                ((Button)findViewById(R.id.cancelFilter)).setVisibility(View.VISIBLE);
                //show documents
                filterList.setVisibility(View.GONE);
                LinearLayout documentGalleryLayout = (LinearLayout) findViewById(R.id.documentGalleryLayout);
                //the category (title) is going to be the first element in form of an imaginary document, so the adapter can draw it as the title
                //filteredDocuments.add(0, new Document(selectedCategory, null, null, null));
                if (filteredDocuments != null) {
                    RecyclerView newDocumentList = new RecyclerView(DocumentGallerySearchActivity.this);
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(DocumentGallerySearchActivity.this);
                    newDocumentList.setLayoutManager(mLayoutManager);
                    newDocumentList.setAdapter(new DocumentGalleryAdapter(DocumentGallerySearchActivity.this, filteredDocuments, false));
                    documentGalleryLayout.addView(newDocumentList);
                }else{
                    //WE SHOULDN'T HAVE ANY CATEGORY WITHOUT DOCUMENTS
                }
            }
        });
        ((LinearLayout)findViewById(R.id.documentGalleryLayout)).addView(filterList);
    }

    public void cancelFilter(){
        isFiltering = false;
        ((Button)findViewById(R.id.cancelFilter)).setVisibility(View.GONE);
        ((LinearLayout)findViewById(R.id.documentGalleryLayout)).removeAllViews();
        createFilterList();
    }

    @Override
    public void onBackPressed() {
        if(isFiltering)
            cancelFilter();
        else
            super.onBackPressed();
    }

}
