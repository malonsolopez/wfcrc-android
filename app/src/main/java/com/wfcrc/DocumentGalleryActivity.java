package com.wfcrc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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

public class DocumentGalleryActivity extends Fragment {

    private View mDocumentGalleryFragmentView;

    private List<Document> mDocumentGallery = null;

    private HashMap<String, ArrayList<Document>> mSortedDocumentGallery = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDocumentGalleryFragmentView = inflater.inflate(R.layout.activity_document_gallery, container, false);
        try {
            //get document gallery
            mDocumentGallery = AppConfig.getDocumentRepository(this.getContext()).getAll();
            //short documents in sublist
            if (mDocumentGallery != null) {
                mSortedDocumentGallery = Document.sortDocuments(mDocumentGallery);
                if (mSortedDocumentGallery != null && !mSortedDocumentGallery.isEmpty())
                    initDocumentGallery();
                else
                    showNoResultsScreen(inflater, container);
            } else {
                showNoResultsScreen(inflater, container);
            }
        } catch (Exception e) {
            showNoResultsScreen(inflater, container);
        }
        //GA
        ((AppConfig)getActivity().getApplication()).getAnalytics().sendPageView(getString(R.string.ga_document_gallery));
        return mDocumentGalleryFragmentView;
    }

    private void showNoResultsScreen(LayoutInflater inflater, ViewGroup container) {
        mDocumentGalleryFragmentView = inflater.inflate(R.layout.no_results_layout, container, false);
        ((TextView) mDocumentGalleryFragmentView.findViewById(R.id.noResultsTextview)).setText(R.string.document_gallery_error);
    }

    private void initDocumentGallery() {
        LinearLayout documentGalleryLayout = (LinearLayout) mDocumentGalleryFragmentView.findViewById(R.id.documentGalleryLayout);
        //create list per each category
        Iterator iterator = mSortedDocumentGallery.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry documents = (Map.Entry) iterator.next();
            //list of documents for this category
            ArrayList<Document> documentList = (ArrayList<Document>) documents.getValue();
            //the category (title) is going to be the first element in form of an imaginary document, so the adapter can draw it as the title
            documentList.add(0, new Document(-1, documents.getKey().toString(), null, null, null, false));
            if (documentList != null) {
                Context context = getContext();
                RecyclerView newDocumentList = new RecyclerView(context);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
                newDocumentList.setLayoutManager(mLayoutManager);
                newDocumentList.setAdapter(new DocumentGalleryAdapter(context, documentList, true));
                documentGalleryLayout.addView(newDocumentList);
            }
        }
    }

    /*@Override
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
    }*/

}
