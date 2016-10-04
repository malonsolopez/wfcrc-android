package com.wfcrc.donation;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by maria on 9/14/16.
 */
public abstract class Donation{

    public double mAmount;
    public Activity mContext;


    public Donation(Activity mContext) {
        this.mContext = mContext;
    }

    public double getmAmount() {
        return mAmount;
    }

    public void setmAmount(double mAmount) {
        this.mAmount = mAmount;
    }

    public Activity getmContext() {
        return mContext;
    }

    public void setmContext(Activity mContext) {
        this.mContext = mContext;
    }

    public void performDonation(){
        boolean donationReady = beforeDonate();
        if(donationReady) {
            boolean result = donate();
            afterDonate(result);
        }else{
            afterDonate(false);
        }
    }

    protected abstract boolean beforeDonate();
    protected abstract boolean donate();

    protected void afterDonate(boolean result){
        if(result){
            SuccessDonationDialog successDialog = new SuccessDonationDialog();
            Bundle argsSuccess = new Bundle();
            argsSuccess.putString(new String("title"), new String("hola"));
            //argsSuccess.putString(new String("message"), new String("caracola"));
            successDialog.setArguments(argsSuccess);
            successDialog.show(mContext.getFragmentManager(), "SuccessDonationDialog");
        }else{
            FailedDonationDialog failedDonationDialog = new FailedDonationDialog();
            failedDonationDialog.show(mContext.getFragmentManager(), "FailedDonationDialog");
        }
    }
}
