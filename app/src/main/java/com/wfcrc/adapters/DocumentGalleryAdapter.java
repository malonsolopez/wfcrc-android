package com.wfcrc.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wfcrc.R;
import com.wfcrc.analytics.Analytics;
import com.wfcrc.config.AppConfig;
import com.wfcrc.pojos.Document;

import java.util.List;

/**
 * Created by maria on 10/10/16.
 */
public class DocumentGalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static String LOG_TAG = "DocumentGalleryAdapter";

    private List<Document> mDataset;

    private Context mContext;

    private static MyClickListenerImplementation myClickListener;

    private Analytics mTracker;

    private boolean mNeedsTitle;

    public static class TitleHolder extends RecyclerView.ViewHolder{

        TextView documentCategory;

        public TitleHolder(View itemView) {
            super(itemView);
            documentCategory = (TextView) itemView.findViewById(R.id.documentCategory);
        }
    }


    public static class ItemHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        ImageView documentIcon;
        TextView documentTitle;
        ImageView documentSync;
        View rowBottomSeparator;


        public ItemHolder(View itemView) {
            super(itemView);
            documentIcon = (ImageView) itemView.findViewById(R.id.documentIcon);
            documentTitle = (TextView) itemView.findViewById(R.id.documentTitle);
            documentSync = (ImageView) itemView.findViewById(R.id.documentSync);
            rowBottomSeparator = (View) itemView.findViewById(R.id.rowBottomSeparator);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }


    public DocumentGalleryAdapter(Context context, List<Document> myDataset, boolean needsTitle) {
        mContext = context;
        mDataset = myDataset;
        mNeedsTitle = needsTitle;
        this.setOnItemClickListener(new MyClickListenerImplementation());
        mTracker =  ((AppConfig)mContext.getApplicationContext()).getAnalytics();
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0 && mNeedsTitle)
            return 0;//first item is the title
        else
            return 1;//the rest of the items are documents
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == 0) {//title
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.document_gallery_category_title, parent, false);
            return  new TitleHolder(view);
        }else {//item
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.document_gallery_item, parent, false);
            return new ItemHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Document document = mDataset.get(position);
        if (mNeedsTitle) {
            if (position != 0) {
                //TODO: set right icons
                //((ItemHolder)holder).documentIcon.setImageResource(imageId);
                ((ItemHolder) holder).documentTitle.setText(document.getTitle());
                //((ItemHolder)holder).documentSync.setImageResource(imageId);
            } else {
                ((TitleHolder) holder).documentCategory.setText(document.getTitle());
            }
        }else{
            //TODO: set right icons
            //((ItemHolder)holder).documentIcon.setImageResource(imageId);
            ((ItemHolder) holder).documentTitle.setText(document.getTitle());
            //((ItemHolder)holder).documentSync.setImageResource(imageId);
            if(position == getItemCount()-1)
                ((ItemHolder) holder).rowBottomSeparator.setVisibility(View.GONE);
        }
    }

    public void addItem(Document dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(MyClickListenerImplementation myClickListener) {
        this.myClickListener = myClickListener;
    }

    private class MyClickListenerImplementation implements MyClickListener{
        @Override
        public void onItemClick(int position, View v) {
            Log.i(LOG_TAG, " Clicked on Item " + position);
            //TODO: onClick on the document
        }
    }
}
