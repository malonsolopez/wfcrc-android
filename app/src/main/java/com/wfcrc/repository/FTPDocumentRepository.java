package com.wfcrc.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.wfcrc.R;
import com.wfcrc.SplashActivity;
import com.wfcrc.pojos.Document;
import com.wfcrc.sqlite.WFCRCDB;
import com.wfcrc.utils.SecurityUtils;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by maria on 7/7/17.
 */
public class FTPDocumentRepository implements Repository{

    private Context mContext;
    private SplashActivity.SplashCallback mCallback;

    public FTPDocumentRepository(Context context, SplashActivity.SplashCallback callback){
        mContext = context;
        mCallback = callback;
    }

    @Override
    public List<Document> getAll()  throws RepositoryException{
        new RetrieveDocumentGalleryFromFTP().execute();
        return null;
    }

    private class RetrieveDocumentGalleryFromFTP extends AsyncTask<Void, Void, SplashActivity.SplashCallback> {

        private List<Document> mDocumentGallery = null;

        protected SplashActivity.SplashCallback doInBackground(Void... params) {
            try {
                FTPClient ftpClient = new FTPClient();
                ftpClient.connect(InetAddress.getByName("ftp.wfcrc.org"));
                String ftpLogin = mContext.getSharedPreferences("WFCRC", Context.MODE_PRIVATE).getString("value1", null);
                String ftpPwd = mContext.getSharedPreferences("WFCRC", Context.MODE_PRIVATE).getString("value2", null);
                if (ftpLogin== null || ftpPwd == null) {
                    prepareFTPSharePreferences();
                    ftpLogin = mContext.getSharedPreferences("WFCRC", Context.MODE_PRIVATE).getString("value1", null);
                    ftpPwd = mContext.getSharedPreferences("WFCRC", Context.MODE_PRIVATE).getString("value2", null);
                }
                SecurityUtils encryptionHelper = new SecurityUtils("".toCharArray());
                ftpClient.login(encryptionHelper.decrypt(ftpLogin), encryptionHelper.decrypt(ftpPwd));
                System.out.println("status :: " + ftpClient.getStatus());
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                FTPFile[] filesInDocumentGallery = ftpClient.listFiles("public_html/docs/");
                for (int i = 0; i < filesInDocumentGallery.length; i++) {
                    try {
                        String title = filesInDocumentGallery[i].getName();
                        Document document = new Document();
                        document.setId(new Random().nextInt());
                        document.setTitle(title);
                        document.setUrl("http://wfcrc.org/docs/" + title);
                        document.setFormat(title.substring(title.length()-3));
                        //TODO: improve hardcoded categories
                        String categoryID = title.substring(0, 3);
                        switch (categoryID){
                            case "PSA": document.setCategory("Public Service Announcements");
                                break;
                            case "CAN": document.setCategory("Coral Alert Notifications");
                                break;
                            case "ERD": document.setCategory("Emergency Reporting");
                                break;
                            case "CTA": document.setCategory("Call to Action");
                                break;
                            case "MPA": document.setCategory("Marine Protected Areas");
                                break;
                            case "MLA": document.setCategory("Marine Life Alert");
                                break;
                            case "NSR": document.setCategory("Natural Science Report");
                                break;
                            case "OSA": document.setCategory("Oil Spill Alerts");
                                break;
                            case "OWC": document.setCategory("Oceans & Winds Currents");
                                break;
                            case "GF-": document.setCategory("Grants & Funding");
                                break;
                            case "VP-": document.setCategory("Vendor Products");
                                break;
                            default: document.setCategory("Corporate Documents");
                                break;
                        }

                        if(mDocumentGallery == null)
                            mDocumentGallery = new ArrayList<Document>();
                        mDocumentGallery.add(document);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mCallback;
        }

        protected void onPostExecute(SplashActivity.SplashCallback callback) {
            if (mDocumentGallery != null) {
                if(!mDocumentGallery.isEmpty()){
                    //store documents in database
                    WFCRCDB db = new WFCRCDB(mContext);
                    db.deleteDocuments();
                    db.insertDocuments(mDocumentGallery);
                }
            }
            callback.proceedWithLaunching();
        }

        private void prepareFTPSharePreferences(){
                SharedPreferences.Editor edit = mContext.getSharedPreferences("WFCRC", Context.MODE_PRIVATE).edit();
                String strLine;
                String user = null;
                String password = null;
                try {
                    InputStream input = mContext.getResources().openRawResource(R.raw.data);
                    //InputStream input = mContext.getAssets().open("data.txt");
                    BufferedReader br = new BufferedReader(new InputStreamReader(input));
                    int count = 0;
                    while ((strLine = br.readLine()) != null) {
                        if (count == 0) {
                            user = strLine;
                        } else {
                            password = strLine;
                        }
                        count++;
                    }
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                edit.putString("value1", user);
                edit.putString("value2", password);
                edit.commit();
        }

    }
}
