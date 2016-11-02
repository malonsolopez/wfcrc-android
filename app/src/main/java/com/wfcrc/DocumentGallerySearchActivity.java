package com.wfcrc;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class DocumentGallerySearchActivity extends AppCompatActivity
        implements SearchView.OnQueryTextListener {

    private List<Document> mDocumentGallery;

    private HashMap<String, ArrayList<Document>> mSortedDocumentGallery;

    private List<String> mIndexTitles;

    private boolean isFiltering = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_gallery_search);
        mDocumentGallery = getIntent().getParcelableArrayListExtra("DocumentGallery");
        mSortedDocumentGallery = Document.sortDocuments(mDocumentGallery);
        getIndexTitles();
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
                //the category (title) is going to be the first element in form of an imaginary document, so the adapter can draw it as the title
                //filteredDocuments.add(0, new Document(selectedCategory, null, null, null));
                if (filteredDocuments != null) {
                    buildDocumentList(filteredDocuments);
                }else{
                    //WE SHOULDN'T HAVE ANY CATEGORY WITHOUT DOCUMENTS
                }
            }
        });
        ((LinearLayout)findViewById(R.id.documentGalleryLayout)).addView(filterList);
    }

    public void buildDocumentList(List<Document> documents){
        RecyclerView newDocumentList = new RecyclerView(DocumentGallerySearchActivity.this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(DocumentGallerySearchActivity.this);
        newDocumentList.setLayoutManager(mLayoutManager);
        newDocumentList.setAdapter(new DocumentGalleryAdapter(DocumentGallerySearchActivity.this, documents, false));
        LinearLayout documentGalleryLayout = (LinearLayout) findViewById(R.id.documentGalleryLayout);
        documentGalleryLayout.addView(newDocumentList);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.document_gallery_search_menu, menu);

        // Associate searchable configuration with the SearchView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
            searchView.setIconified(false);//search field always expanded
            searchView.setOnQueryTextListener(this);
            // Get the search close button image view
            ImageView closeButton = (ImageView)searchView.findViewById(R.id.search_close_btn);
            closeButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //Find EditText view
                    EditText et = (EditText) findViewById(R.id.search_src_text);
                    //Clear the text from EditText view
                    et.setText("Search");
                    ((LinearLayout)findViewById(R.id.documentGalleryLayout)).removeAllViews();
                    createFilterList();
                }
            });
        }
        return true;
    }

    // User pressed the search button
    @Override
    public boolean onQueryTextSubmit(String query) {
        //FIXME
        //get results
        List<String> results = new ArrayList<String>();
        for (String title: mIndexTitles) {
            if(title.contains(query))
                results.add(title);
        }
        if(results.isEmpty()){
            //TODO: SHOW not results
        }else{
            ((LinearLayout)findViewById(R.id.documentGalleryLayout)).removeAllViews();
            buildDocumentList(getDocumentsFromTitle(results));
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // User changed the text
        return false;
    }

    public void getIndexTitles(){
        //FIXME
        mIndexTitles = new ArrayList<String>();
        for (Document document: mDocumentGallery) {
            mIndexTitles.add(document.getTitle());
        }
    }

    public List<Document> getDocumentsFromTitle(List<String> documentTitles){
        //TODO
        return null;
    }

}
