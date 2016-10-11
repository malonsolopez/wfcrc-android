package com.wfcrc.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wfcrc.ProgramDetailActivity;
import com.wfcrc.R;
import com.wfcrc.pojos.Program;
import com.wfcrc.social.Share;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maria on 10/10/16.
 */
public class ProgramCardAdapter extends RecyclerView.Adapter<ProgramCardAdapter.DataObjectHolder>{

    private static String LOG_TAG = "ProgramCarAdapter";

    private List<Program> mDataset;

    private Context mContext;

    private static MyClickListenerImplementation myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        ImageView cardThumbnail;
        TextView cardTitle;
        TextView cardDescription;
        Button cardAction1;
        Button cardAction2;

        public DataObjectHolder(View itemView) {
            super(itemView);
            cardThumbnail = (ImageView) itemView.findViewById(R.id.cardThumbnail);
            cardTitle = (TextView) itemView.findViewById(R.id.cardTitle);
            cardDescription = (TextView) itemView.findViewById(R.id.cardDescription);
            cardAction1 = (Button)itemView.findViewById(R.id.cardAction1);
            cardAction2 = (Button)itemView.findViewById(R.id.cardAction2);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }


    public ProgramCardAdapter(Context context, List<Program> myDataset) {
        mContext = context;
        mDataset = myDataset;
        this.setOnItemClickListener(new MyClickListenerImplementation());
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.program_cardview, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        final Program program = mDataset.get(position);
        int imageId = mContext.getResources().getIdentifier(program.getImageId(), "drawable", mContext.getPackageName());
        if(imageId == 0)
            holder.cardThumbnail.setVisibility(ImageView.GONE);
        else
            holder.cardThumbnail.setImageResource(imageId);
        holder.cardTitle.setText(program.getTitle());
        holder.cardDescription.setText(program.getSubtitle());
        holder.cardAction1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProgramDetail(program);
            }
        });
        holder.cardAction2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: content for sharing
                String subject ="";
                String body ="";
                (new Share(ProgramCardAdapter.this.mContext, subject, body)).share();
            }
        });
    }

    public void addItem(Program dataObj, int index) {
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
            openProgramDetail(mDataset.get(position));
        }
    }

    private void openProgramDetail(Program program){
        Intent intent = new Intent(mContext, ProgramDetailActivity.class);
        intent.putExtra("Program", program);
        mContext.startActivity(intent);
    }
}
