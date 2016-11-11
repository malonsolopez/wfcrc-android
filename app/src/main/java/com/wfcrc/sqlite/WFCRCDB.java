package com.wfcrc.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wfcrc.pojos.Document;

import java.util.ArrayList;
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
        values.put(WFCRCContract.Documents.COLUMN_NAME_TITLE, document.getTitle());
        values.put(WFCRCContract.Documents.COLUMN_NAME_FORMAT, document.getFormat());
        values.put(WFCRCContract.Documents.COLUMN_NAME_CATEGORY, document.getCategory());
        values.put(WFCRCContract.Documents.COLUMN_NAME_URL, document.getUrl());
        // Insert the new row, returning the primary key value of the new row
        return db.insert(WFCRCContract.Documents.TABLE_NAME, null, values);
    }

    public void insertDocuments(List<Document> documentList){
        for (Document document: documentList) {
            insertDocument(document);
        }
    }

    public List<Document> getDocuments(String where, String[] whereArgs, String group, String filter, String order){
        List<Document> returnDocuments = null;
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                WFCRCContract.Documents._ID,
                WFCRCContract.Documents.COLUMN_NAME_TITLE,
                WFCRCContract.Documents.COLUMN_NAME_FORMAT,
                WFCRCContract.Documents.COLUMN_NAME_CATEGORY,
                WFCRCContract.Documents.COLUMN_NAME_URL
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
            doc.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(WFCRCContract.Documents.COLUMN_NAME_TITLE)));
            doc.setFormat(cursor.getString(cursor.getColumnIndexOrThrow(WFCRCContract.Documents.COLUMN_NAME_FORMAT)));
            doc.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(WFCRCContract.Documents.COLUMN_NAME_CATEGORY)));
            doc.setUrl(cursor.getString(cursor.getColumnIndexOrThrow(WFCRCContract.Documents.COLUMN_NAME_URL)));
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
        return getDocuments(WFCRCContract.Documents.COLUMN_NAME_TITLE + " = ?", new String[]{title}, null, null, null);
    }

    public int updateDocument(Document document){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        // New value for one column
        ContentValues values = new ContentValues();
        values.put(WFCRCContract.Documents.COLUMN_NAME_TITLE, document.getTitle());
        values.put(WFCRCContract.Documents.COLUMN_NAME_FORMAT, document.getFormat());
        values.put(WFCRCContract.Documents.COLUMN_NAME_CATEGORY, document.getCategory());
        values.put(WFCRCContract.Documents.COLUMN_NAME_URL, document.getUrl());
        String where = WFCRCContract.Documents.COLUMN_NAME_TITLE + " LIKE ?";
        String[] whereArgs = {document.getTitle()};
        return db.update(
                WFCRCContract.Documents.TABLE_NAME,
                values,
                where,
                whereArgs);
    }

    public void deleteDocuments(){
        //db.delete(FeedEntry.TABLE_NAME, selection, selectionArgs);
        //TODO
    }

}
