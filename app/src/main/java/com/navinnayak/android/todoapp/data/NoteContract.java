package com.navinnayak.android.todoapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class NoteContract {

    public static final String CONTENT_AUTHORITY = "com.navinnayak.android.todoapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_NOTES = "note";

    private NoteContract() {
    }

    public static final class NoteEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_NOTES);
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + PATH_NOTES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + PATH_NOTES;

        public final static String TABLE_NAME = "notes";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_TITLE = "title";
        public final static String COLUMN_DESC = "description";
        public final static String COLUMN_DATE_TIME = "datetime";
        public final static String COLUMN_DONE_NOTDONE = "completed";
        public static final int CHECKED = 1;
        public static final int UNCHECKED = 0;

        public static boolean isValidCheckBox(int completed) {
            return completed == CHECKED || completed == UNCHECKED;
        }


    }


}
