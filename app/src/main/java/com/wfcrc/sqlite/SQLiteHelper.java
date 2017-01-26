package com.wfcrc.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by maria on 11/10/16.
 */
public class SQLiteHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "WFCRC.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String BOOLEAN_TYPE = " BOOLEAN";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + WFCRCContract.Documents.TABLE_NAME + " (" +
                    WFCRCContract.Documents.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    WFCRCContract.Documents.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    WFCRCContract.Documents.COLUMN_NAME_FORMAT + TEXT_TYPE + COMMA_SEP +
                    WFCRCContract.Documents.COLUMN_NAME_CATEGORY + TEXT_TYPE + COMMA_SEP +
                    WFCRCContract.Documents.COLUMN_NAME_URL + TEXT_TYPE + COMMA_SEP +
                    WFCRCContract.Documents.COLUMN_NAME_DOWNLOADED + BOOLEAN_TYPE + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + WFCRCContract.Documents.TABLE_NAME;

//    private static final String SQL_UPDATE_ENTRIES =
//            "UPDATE " + WFCRCContract.Documents.TABLE_NAME + " SET ";
//
//
//    UPDATE COMPANY SET ADDRESS = 'Texas' WHERE ID = 6;


    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
