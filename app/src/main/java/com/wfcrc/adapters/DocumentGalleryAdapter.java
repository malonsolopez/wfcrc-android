package com.wfcrc.adapters;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wfcrc.R;
import com.wfcrc.analytics.Analytics;
import com.wfcrc.config.AppConfig;
import com.wfcrc.pojos.Document;
import com.wfcrc.sqlite.WFCRCDB;
import com.wfcrc.utils.ConnectivityUtils;

import java.io.File;
import java.util.List;

/**
 * Created by maria on 10/10/16.
 */
public class DocumentGalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static String LOG_TAG = "DocumentGalleryAdapter";
    private static final int TITLE_VIEW_TYPE = 0;
    private static final int ITEM_VIEW_TYPE =1;

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
        ImageButton documentSync;
        View rowBottomSeparator;


        public ItemHolder(View itemView) {
            super(itemView);
            documentIcon = (ImageView) itemView.findViewById(R.id.documentIcon);
            documentTitle = (TextView) itemView.findViewById(R.id.documentTitle);
            documentSync = (ImageButton) itemView.findViewById(R.id.documentSyncButton);
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
            return TITLE_VIEW_TYPE;//first item is the title
        else if(mDataset.get(position).getCategory() == null)
            return TITLE_VIEW_TYPE;//titles below in the list
        else
            return ITEM_VIEW_TYPE;//the rest of the items are documents
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TITLE_VIEW_TYPE) {//title
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
            /*if (position != 0) {
                //TODO: set right icons
                //((ItemHolder)holder).documentIcon.setImageResource(imageId);
                if(document.isDownloaded()) {
                    //((ItemHolder) holder).documentSync.setImageResource(R.drawable.ic_sync_black_24dp);
                    ((ItemHolder) holder).documentSync.setEnabled(false);
                }else{
                    //((ItemHolder) holder).documentSync.setImageResource(R.drawable.ic_sync_disabled_black_24dp);
                    ((ItemHolder) holder).documentSync.setEnabled(true);
                }
                ((ItemHolder) holder).documentTitle.setText(document.getTitle());
                ((ItemHolder)holder).documentSync.setOnClickListener(new DownloadDocumentOnClickListener(document));
            } else {
                ((TitleHolder) holder).documentCategory.setText(document.getTitle());
            }*/
            if(holder instanceof TitleHolder){
                ((TitleHolder) holder).documentCategory.setText(document.getTitle());
            }else if(holder instanceof ItemHolder){
                //TODO: set right icons
                //((ItemHolder)holder).documentIcon.setImageResource(imageId);
                if(document.isDownloaded()) {
                    //((ItemHolder) holder).documentSync.setImageResource(R.drawable.ic_sync_black_24dp);
                    ((ItemHolder) holder).documentSync.setEnabled(false);
                }else{
                    //((ItemHolder) holder).documentSync.setImageResource(R.drawable.ic_sync_disabled_black_24dp);
                    ((ItemHolder) holder).documentSync.setEnabled(true);
                }
                ((ItemHolder) holder).documentTitle.setText(document.getTitle());
                //((ItemHolder)holder).documentSync.setOnClickListener(new DownloadDocumentOnClickListener(document));
            }
        }else{
            //TODO: set right icons
            //((ItemHolder)holder).documentIcon.setImageResource(imageId);
            if(document.isDownloaded()) {
                //((ItemHolder) holder).documentSync.setImageResource(R.drawable.ic_sync_black_24dp);
                ((ItemHolder) holder).documentSync.setEnabled(false);
            }else{
                //((ItemHolder) holder).documentSync.setImageResource(R.drawable.ic_sync_disabled_black_24dp);
                ((ItemHolder) holder).documentSync.setEnabled(true);
            }
            //backgroundTint="@color/primary_dark_material_light"
            ((ItemHolder) holder).documentTitle.setText(document.getTitle());
            //((ItemHolder)holder).documentSync.setOnClickListener(new DownloadDocumentOnClickListener(document));
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

    //onClick on the document
    private class MyClickListenerImplementation implements MyClickListener{
        @Override
        public void onItemClick(int position, View v) {
            Log.i(LOG_TAG, " Clicked on Item " + position);
            Document doc = mDataset.get(position);
            //open URL if we are online
            if(ConnectivityUtils.isNetworkAvailable(mContext)){
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(doc.getUrl()));
                mContext.startActivity(browserIntent);
            }else{//open downloaded document if possible
                if(doc.isDownloaded()){

                    File file = new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOWNLOADS),
                            doc.getTitle() + ".pdf");
                    Uri path = Uri.fromFile(file);
                    Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                    pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    pdfOpenintent.setDataAndType(path, "*/*");
                    try {
                        mContext.startActivity(pdfOpenintent);
                    }
                    catch (ActivityNotFoundException e) {
                        //// TODO: 1/31/17
                    }
                }
            }
        }
    }

    private class DownloadDocumentOnClickListener implements  View.OnClickListener {

        private Document documentToDownload;

        public DownloadDocumentOnClickListener(Document doc){
            this.documentToDownload = doc;
        }

        @Override
        public void onClick(final View view) {
            if (ConnectivityUtils.isNetworkAvailable(mContext)) {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(documentToDownload.getUrl()));
                request.setDescription(documentToDownload.getTitle());
                request.setTitle(documentToDownload.getTitle());
                // in order for this if to run, you must use the android 3.2 to compile your app
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                }//TODO: controlar resto de casos
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, documentToDownload.getTitle());
                // get download service and enqueue file
                DownloadManager manager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);
                BroadcastReceiver onComplete=new BroadcastReceiver() {
                    public void onReceive(Context ctxt, Intent intent) {
                        documentToDownload.setDownloaded(true);
                        //change isDownload flag on document gallery database
                        WFCRCDB db = new WFCRCDB(mContext);
                        db.updateDocument(documentToDownload);
                        //disable download button
                        view.setEnabled(false);
                    }
                };
                mContext.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
            }else{
                Toast.makeText(mContext, mContext.getString(R.string.no_network), Toast.LENGTH_LONG).show();
            }
        }
    }
}
