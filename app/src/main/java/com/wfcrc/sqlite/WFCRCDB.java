package com.wfcrc.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.wfcrc.pojos.Document;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by maria on 11/10/16.
 */
public class WFCRCDB {

    private Context mContext;
    private SQLiteHelper mDbHelper;

    public WFCRCDB(Context context){
        mContext = context;
        mDbHelper = new SQLiteHelper(mContext);
    }

    public long insertDocument(Document document){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(WFCRCContract.Documents.COLUMN_NAME_ID, document.getId());
        values.put(WFCRCContract.Documents.COLUMN_NAME_TITLE, document.getTitle());
        values.put(WFCRCContract.Documents.COLUMN_NAME_FORMAT, document.getFormat());
        values.put(WFCRCContract.Documents.COLUMN_NAME_CATEGORY, document.getCategory());
        values.put(WFCRCContract.Documents.COLUMN_NAME_URL, document.getUrl());
        values.put(WFCRCContract.Documents.COLUMN_NAME_DOWNLOADED, document.isDownloaded());
        // Insert the new row, returning the primary key value of the new row
        //replaces on clonflict to make sure we don't overwrite the field downloaded
        return db.insertWithOnConflict(WFCRCContract.Documents.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void insertDocuments(List<Document> documentList){
        File documentGalleryDirectory = mContext.getExternalFilesDir(null);
        List<String> downloadedDocuments = null;
        try{
            downloadedDocuments = Arrays.asList(documentGalleryDirectory.list());
        }catch (Exception e){
            e.printStackTrace();
        }
        for (Document document: documentList) {
            //verify if the document is downloaded before inserting
            if(downloadedDocuments != null)
                document.setDownloaded(downloadedDocuments.contains(document.getTitle()));
            insertDocument(document);
        }
    }

    public List<Document> getDocuments(String where, String[] whereArgs, String group, String filter, String order){
        List<Document> returnDocuments = null;
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {"*"
                /*WFCRCContract.Documents._ID,
                WFCRCContract.Documents.COLUMN_NAME_TITLE,
                WFCRCContract.Documents.COLUMN_NAME_FORMAT,
                WFCRCContract.Documents.COLUMN_NAME_CATEGORY,
                WFCRCContract.Documents.COLUMN_NAME_URL*/
        };
        Cursor cursor = db.query(
                WFCRCContract.Documents.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                where,                                // The columns for the WHERE clause
                whereArgs,                            // The values for the WHERE clause
                group,                                     // don't group the rows
                filter,                                     // don't filter by row groups
                order                                 // The sort order
        );
        //cursor.moveToFirst();
        while(cursor.moveToNext()){
            Document doc = new Document();
            doc.setId(cursor.getInt(cursor.getColumnIndexOrThrow(WFCRCContract.Documents.COLUMN_NAME_ID)));
            doc.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(WFCRCContract.Documents.COLUMN_NAME_TITLE)));
            doc.setFormat(cursor.getString(cursor.getColumnIndexOrThrow(WFCRCContract.Documents.COLUMN_NAME_FORMAT)));
            doc.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(WFCRCContract.Documents.COLUMN_NAME_CATEGORY)));
            doc.setUrl(cursor.getString(cursor.getColumnIndexOrThrow(WFCRCContract.Documents.COLUMN_NAME_URL)));
            doc.setDownloaded(cursor.getInt(cursor.getColumnIndexOrThrow(WFCRCContract.Documents.COLUMN_NAME_DOWNLOADED)) ==  0 ? false:true);
            if (returnDocuments == null){
                returnDocuments = new ArrayList<Document>();
            }
            returnDocuments.add(doc);
        }
        return returnDocuments;
    }

    public List<Document> getDocuments(){
        return getDocuments(null, null, null, null, null);
    }

    public List<Document> getDocumentsFromCategory(String category){
        return getDocuments(WFCRCContract.Documents.COLUMN_NAME_CATEGORY + " = ?", new String[]{category}, null, null, null);
    }

    public List<Document> getDocumentsFromTitle(String title){
        return getDocuments(WFCRCContract.Documents.COLUMN_NAME_TITLE + " LIKE \'%" + title + "%\'", null, null, null, null);
    }

    public int updateDocument(Document document){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        // New value for one column
        ContentValues values = new ContentValues();
        values.put(WFCRCContract.Documents.COLUMN_NAME_ID, document.getId());
        values.put(WFCRCContract.Documents.COLUMN_NAME_TITLE, document.getTitle());
        values.put(WFCRCContract.Documents.COLUMN_NAME_FORMAT, document.getFormat());
        values.put(WFCRCContract.Documents.COLUMN_NAME_CATEGORY, document.getCategory());
        values.put(WFCRCContract.Documents.COLUMN_NAME_URL, document.getUrl());
        values.put(WFCRCContract.Documents.COLUMN_NAME_DOWNLOADED, document.isDownloaded());
        String where = WFCRCContract.Documents.COLUMN_NAME_ID + " LIKE ?";
        String[] whereArgs = {Integer.toString(document.getId())};
        return db.update(
                WFCRCContract.Documents.TABLE_NAME,
                values,
                where,
                whereArgs);
    }

    public int deleteDocuments(){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        return db.delete(WFCRCContract.Documents.TABLE_NAME, null, null);
    }

}
