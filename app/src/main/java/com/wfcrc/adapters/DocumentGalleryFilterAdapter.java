package com.wfcrc.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.wfcrc.R;
import com.wfcrc.analytics.Analytics;
import com.wfcrc.config.AppConfig;
import com.wfcrc.pojos.Document;

import java.util.List;

/**
 * Created by maria on 10/10/16.
 */
public class DocumentGalleryFilterAdapter extends ArrayAdapter<String> {

    private static String LOG_TAG = "DocumentGalleryFilterAdapter";

    private List<String> mDataset;

    private Context mContext;

    public DocumentGalleryFilterAdapter(Context context, List<String> myDataset) {
        super(context, -1, myDataset);
        mContext = context;
        mDataset = myDataset;
    }

    @Override
    public int getCount() {
        return mDataset.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.document_gallery_filter_list_item, parent, false);
        //ImageView documentIcon = (ImageView) rowView.findViewById(R.id.documentIcon);
        TextView documentTitle = (TextView) rowView.findViewById(R.id.documentTitle);
        documentTitle.setText(mDataset.get(position));
        return rowView;
    }

}
