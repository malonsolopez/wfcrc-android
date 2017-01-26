package com.wfcrc.sqlite;

import android.provider.BaseColumns;

/**
 * Created by maria on 11/10/16.
 */
public class WFCRCContract {

    private WFCRCContract(){

    }

    public static class Documents implements BaseColumns{
        public static final String TABLE_NAME = "document_gallery";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_FORMAT = "format";
        public static final String COLUMN_NAME_CATEGORY = "category";
        public static final String COLUMN_NAME_URL = "url";
        public static final String COLUMN_NAME_DOWNLOADED = "downloaded";
    }


}
