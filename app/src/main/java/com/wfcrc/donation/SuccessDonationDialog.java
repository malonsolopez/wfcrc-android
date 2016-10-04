package com.wfcrc.donation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;


import com.myapplication.R;
import com.wfcrc.social.Share;

/**
 * Created by maria on 9/14/16.
 */
public class SuccessDonationDialog extends DialogFragment {

    private String mCustomTitle;
    private String mCustomMessage;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mCustomTitle = getString(R.string.donate_success_title);
        mCustomMessage = getString(R.string.donate_success_description);
        Bundle args = this.getArguments();
        if(args != null){
            if(args.getString("title") != null)
                mCustomTitle = args.getString("title");
            if(args.getString("message") != null)
                mCustomMessage = args.getString("message");
        }
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(mCustomTitle)
                .setMessage(mCustomMessage)
                .setPositiveButton(R.string.share, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Share shareDonation = new Share(getActivity(), getString(R.string.donate_share_subject), getString(R.string.donate_share_body));
                        shareDonation.share();
                    }
                })
                .setNegativeButton(android.support.v7.appcompat.R.string.abc_action_mode_done, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog and goes back to main activity
                        getActivity().finish();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
