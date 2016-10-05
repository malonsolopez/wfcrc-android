package com.wfcrc.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wfcrc.R;
import com.wfcrc.pojos.Document;

import java.util.ArrayList;

/**
 * Created by maria on 10/4/16.
 */
public class DocumentListAdapter extends BaseAdapter {

    private ArrayList<Document> mData = new ArrayList<Document>();
    private Context mContext;

    public DocumentListAdapter(Context context, ArrayList<Document> documents) {
        mContext = context;
        mData = documents;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Document getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public static class ViewHolder {
        public ImageView icon;
        public TextView title;
        public ImageView sync;

    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        int type = getItemViewType(i);
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.document_gallery_item, null);
            holder.icon = (ImageView) view.findViewById(R.id.documentIcon);
            holder.title = (TextView) view.findViewById(R.id.documentTitle);
            holder.sync = (ImageView) view.findViewById(R.id.documentSync);
            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }
        Document document = mData.get(i);
        //TODO: display different icons depending on the type of document
        holder.title.setText(document.getTitle());
        //TODO: display different icons depending on the type of document
        return view;
    }
}
