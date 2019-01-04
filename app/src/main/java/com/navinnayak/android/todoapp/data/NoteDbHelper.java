package com.navinnayak.android.todoapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.navinnayak.android.todoapp.data.NoteContract.NoteEntry;

public class NoteDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "evernote.db";
    private static final int DATABASE_VERSION = 1;


    private NoteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_NOTES_TABLE = " CREATE TABLE " + NoteEntry.TABLE_NAME + " ("
                + NoteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NoteEntry.COLUMN_TITLE + " TEXT NOT NULL, "
                + NoteEntry.COLUMN_DESC + " BLOB NOT NULL, "
                + NoteEntry.COLUMN_DONE_NOTDONE + " INTEGER, "
                + NoteEntry.COLUMN_DATE + " DATE, "
                + NoteEntry.COLUMN_TIME + " TIME); ";
        db.execSQL(SQL_CREATE_NOTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NoteEntry.TABLE_NAME);
    }
}
