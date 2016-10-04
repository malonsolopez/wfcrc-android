package com.wfcrc.donation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.wfcrc.R;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalActivity;
import com.paypal.android.MEP.PayPalInvoiceData;
import com.paypal.android.MEP.PayPalInvoiceItem;
import com.paypal.android.MEP.PayPalPayment;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by maria on 9/14/16.
 */
public class DonationPayPal extends Donation{

    public static final int REQUEST_PAYPAL_CHECKOUT = 2;

    private static final String PAYPAL_APP_ID_SANDBOX = "APP-80W284485P519543T";

    public DonationPayPal(Activity mContext) {
        super(mContext);
    }

    @Override
    public void performDonation(){
        boolean donationReady = beforeDonate();
        if(donationReady) {
            donate();
        }else{
            //TODO
        }
    }

    public boolean beforeDonate(){
        boolean _paypalLibraryInit = true;//TODO
        PayPal pp = PayPal.getInstance();
        // If the library is already initialized, then we don't need to
        // initialize it again.
        if (pp == null) {
            // This is the main initialization call that takes in your Context,
            // the Application ID, and the server you would like to connect to.
            pp = PayPal.initWithAppID(mContext, PAYPAL_APP_ID_SANDBOX,
                    PayPal.ENV_SANDBOX);

            // -- These are required settings.
            pp.setLanguage("en_US"); // Sets the language for the library.
            // --

            // -- These are a few of the optional settings.
            // Sets the fees payer. If there are fees for the transaction, this
            // person will pay for them. Possible values are FEEPAYER_SENDER,
            // FEEPAYER_PRIMARYRECEIVER, FEEPAYER_EACHRECEIVER, and
            // FEEPAYER_SECONDARYONLY.
            pp.setFeesPayer(PayPal.FEEPAYER_SENDER);
            // Set to true if the transaction will require shipping.
            pp.setShippingEnabled(false);
            // Dynamic Amount Calculation allows you to set tax and shipping
            // amounts based on the user's shipping address. Shipping must be
            // enabled for Dynamic Amount Calculation. This also requires you to
            // create a class that implements PaymentAdjuster and Serializable.
            //pp.setDynamicAmountCalculationEnabled(false);
            // --
            _paypalLibraryInit = true;
        }
        return _paypalLibraryInit;
    }

    public boolean donate(){
        // Create a basic PayPalPayment.
        PayPalPayment payment = new PayPalPayment();
        // Sets the currency type for this payment.
        payment.setCurrencyType("USD");
        // Sets the recipient for the payment. This can also be a phone
        // number.
        payment.setRecipient("pd_1265515509_biz@yahoo.com");
        // Sets the amount of the payment, not including tax and shipping
        // amounts.
        BigDecimal st = new BigDecimal(mAmount);
        st = st.setScale(2, RoundingMode.HALF_UP);
        payment.setSubtotal(st);
        // Sets the payment type. This can be PAYMENT_TYPE_GOODS,
        // PAYMENT_TYPE_SERVICE, PAYMENT_TYPE_PERSONAL, or
        // PAYMENT_TYPE_NONE.
        payment.setPaymentType(PayPal.PAYMENT_TYPE_SERVICE);
        // PayPalInvoiceData can contain tax and shipping amounts. It also
        // contains an ArrayList of PayPalInvoiceItem which can
        // be filled out. These are not required for any transaction.
        PayPalInvoiceData invoice = new PayPalInvoiceData();
        // Sets the tax amount.
        //BigDecimal tax = new BigDecimal(_taxAmount);
        //tax = tax.setScale(2, RoundingMode.HALF_UP);
        //invoice.setTax(tax);
        // Sets the shipping amount.
        /*if (_method == METHOD_DELIVERY) {
            invoice.setShipping(new BigDecimal("2.00"));
            // Forces the user to go to the review page
            PayPal.getInstance().setShippingEnabled(true);
        }*/
        // PayPalInvoiceItem has several parameters available to it. None of these parameters is required.
        PayPalInvoiceItem item1 = new PayPalInvoiceItem();
        // Sets the name of the item.
        item1.setName(mContext.getString(R.string.donation_invoice));
        // Sets the ID. This is any ID that you would like to have associated with the item.
        //item1.setID("1234");
        // Sets the total price which should be (quantity * unit price). The total prices of all PayPalInvoiceItem should add up
        // to less than or equal the subtotal of the payment.
        item1.setTotalPrice(new BigDecimal(mAmount));
        // Sets the unit price.
        item1.setUnitPrice(new BigDecimal(mAmount));
        // Sets the quantity.
        item1.setQuantity(1);
        // Sets the PayPalPayment invoice data.
        payment.setInvoiceData(invoice);
        // Sets the merchant name. This is the name of your Application or
        // Company.
        payment.setMerchantName(mContext.getString(R.string.app_name));
        // Sets the description of the payment.
        payment.setDescription(mContext.getString(R.string.donation_invoice));
        // Use checkout to create our Intent.
        Intent checkoutIntent = PayPal.getInstance().checkout(payment, mContext/*, new ResultDelegate()*/);
        // Use the android's startActivityForResult() and pass in our
        // Intent.
        // This will start the library.
        mContext.startActivityForResult(checkoutIntent, REQUEST_PAYPAL_CHECKOUT);


        return true;
    }

    /* This method handles the PayPal Activity Results. This handles all the responses from the PayPal
         * Payments Library.
         *  This method must be called from the application's onActivityResult() handler
         */
    public void PayPalActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (resultCode) {
            case Activity.RESULT_OK:
                // The payment succeeded
                String payKey = intent
                        .getStringExtra(PayPalActivity.EXTRA_PAY_KEY);
                //this.paymentSucceeded(payKey);
                SuccessDonationDialog successDialog = new SuccessDonationDialog();
                Bundle argsSuccess = new Bundle();
                String message = mContext.getString(R.string.donate_success_description) + "\n\n" + payKey;
                argsSuccess.putString(new String("message"), message);
                successDialog.setArguments(argsSuccess);
                successDialog.show(mContext.getFragmentManager(), "SuccessDonationDialog");
                break;
            case Activity.RESULT_CANCELED:
                // The payment was canceled
                //this.paymentCanceled();
                FailedDonationDialog cancelledDonationDialog = new FailedDonationDialog();
                cancelledDonationDialog.show(mContext.getFragmentManager(), "FailedDonationDialog");
                break;
            case PayPalActivity.RESULT_FAILURE:
                // The payment failed -- we get the error from the
                // EXTRA_ERROR_ID and EXTRA_ERROR_MESSAGE
                String errorID = intent
                        .getStringExtra(PayPalActivity.EXTRA_ERROR_ID);
                String errorMessage = intent
                        .getStringExtra(PayPalActivity.EXTRA_ERROR_MESSAGE);
                //this.paymentFailed(errorID, errorMessage);
                FailedDonationDialog failedDonationDialog = new FailedDonationDialog();
                Bundle argsFailure = new Bundle();
                String failuereMessage = mContext.getString(R.string.donate_error_description) + "\n\n" + errorID  + ": " + errorMessage;
                argsFailure.putString(new String("message"), failuereMessage);
                failedDonationDialog.show(mContext.getFragmentManager(), "FailedDonationDialog");
        }
    }

}
