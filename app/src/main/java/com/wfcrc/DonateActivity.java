package com.wfcrc;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.wfcrc.donation.DonationPayPal;
import com.paypal.android.MEP.CheckoutButton;
import com.paypal.android.MEP.PayPal;

public class DonateActivity extends AppCompatActivity {

    private DonationPayPal mPayPalDonation;

    private static final int INTERNET_PERMISSION = 0;
    private static final int READ_PHONE_STATE_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);
        //before continue we need to check permission due to paypal functionality
        int hasInternetPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        if (hasInternetPermission != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, INTERNET_PERMISSION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }else{
            int hasReadPhoneStatePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
            if (hasReadPhoneStatePermission != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE_PERMISSION);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }else{

                initView();
            }
        }
    }

    //PayPal needs to request some permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case INTERNET_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay!
                    initView();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    //TODO
                    finish();
                }
                return;
            }
            case READ_PHONE_STATE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay!
                    initView();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    //TODO
                    finish();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    // PayPal Activity Results. This handles all the responses from the PayPal
    // Payments Library
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == DonationPayPal.REQUEST_PAYPAL_CHECKOUT) {
            mPayPalDonation. PayPalActivityResult(requestCode, resultCode, intent);
        } else {
            super.onActivityResult(requestCode, resultCode, intent);
        }
    }

    public void initView() {
        //add currency to radiobuttons amounts
        ((RadioButton)findViewById(R.id.radioButton1)).setText(getString(R.string.currency) + getString(R.string.donate_defaultAmount1));
        ((RadioButton)findViewById(R.id.radioButton2)).setText(getString(R.string.currency) + getString(R.string.donate_defaultAmount2));
        ((RadioButton)findViewById(R.id.radioButton3)).setText(getString(R.string.currency) + getString(R.string.donate_defaultAmount3));
        //enable/disable edittext when other mAmount radiobutton is checked/unchecked and clear amounts
        ((RadioButton)findViewById(R.id.radioButton4)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ((EditText) findViewById(R.id.editTextOtherAmount)).setEnabled(b);
            }
        });
        ((RadioButton)findViewById(R.id.radioButton1)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                clearOtherAmount();
            }
        });
        ((RadioButton)findViewById(R.id.radioButton2)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                clearOtherAmount();
            }
        });
        ((RadioButton)findViewById(R.id.radioButton3)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                clearOtherAmount();
            }
        });
        //payments buttons
        mPayPalDonation = new DonationPayPal(this);
        mPayPalDonation.beforeDonate();
        //PayPal button
        PayPal pp = PayPal.getInstance();
        CheckoutButton launchPayPalButton = pp.getCheckoutButton(this, PayPal.BUTTON_278x43, CheckoutButton.TEXT_PAY);
        launchPayPalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double amount = 0.1;
                try {
                    amount = retrieveAmount();
                }catch (Exception e){
                    e.printStackTrace();
                    return;
                }
                mPayPalDonation.setmAmount(amount);
                mPayPalDonation.performDonation();
            }
        });
        LinearLayout paymentsLayout = (LinearLayout) findViewById(R.id.donationPaymentsLayout);
        paymentsLayout.addView(launchPayPalButton);
    }

    public void clearOtherAmount(){
        EditText otherAmount = (EditText) findViewById(R.id.editTextOtherAmount);
        otherAmount.setText(new String());
        otherAmount.setError(null);
    }

    public double retrieveAmount()throws Exception{
        double amount = 0.1;
        if (((RadioButton)findViewById(R.id.radioButton1)).isChecked())
            amount = 5.0;
        else if (((RadioButton)findViewById(R.id.radioButton2)).isChecked())
            amount = 10.0;
        else if (((RadioButton)findViewById(R.id.radioButton3)).isChecked())
            amount = 20.0;
        else {
            EditText otherAmount = (EditText) findViewById(R.id.editTextOtherAmount);
            try{
                amount = Double.parseDouble(otherAmount.getText().toString());
            }catch (Exception e){
                e.printStackTrace();
                otherAmount.setError("Please, introduce an mAmount");
                throw e;
            }
        }
        return amount;
    }

}
