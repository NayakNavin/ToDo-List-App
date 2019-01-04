package com.navinnayak.android.todoapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.navinnayak.android.todoapp.data.NoteContract.NoteEntry;

public class NoteProvider extends ContentProvider {


    private static final int NOTES = 100;
    private static final int NOTE_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(NoteContract.CONTENT_AUTHORITY, NoteContract.PATH_NOTES, NOTES);
        sUriMatcher.addURI(NoteContract.CONTENT_AUTHORITY, NoteContract.PATH_NOTES + "/#", NOTE_ID);

    }

    private NoteDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new NoteDbHelper((getContext()));
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case NOTES:
                cursor = database.query(NoteEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case NOTE_ID:
                selection = NoteEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(NoteEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case NOTES:
                return insertNote(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for" + uri);
        }
    }

    private Uri insertNote(Uri uri, ContentValues values) {
        String noteTitle = values.getAsString(NoteEntry.COLUMN_TITLE);
        if (noteTitle == null) {
            throw new IllegalArgumentException("title required");
        }
        String noteDesc = values.getAsString(NoteEntry.COLUMN_DESC);
        if (noteDesc == null) {
            throw new IllegalArgumentException("description needed");
        }
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.insert(NoteEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.v("message:", "Failed to insert new row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case NOTES:
                return updateNote(uri, contentValues, selection, selectionArgs);
            case NOTE_ID:
                selection = NoteEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateNote(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }


    private int updateNote(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(NoteEntry.COLUMN_TITLE)) {
            String noteTitle = values.getAsString(NoteEntry.COLUMN_TITLE);
            if (noteTitle == null) {
                throw new IllegalArgumentException("title required");
            }
        }
        if (values.containsKey(NoteEntry.COLUMN_DESC)) {
            String noteDesc = values.getAsString(NoteEntry.COLUMN_DESC);
            if (noteDesc == null) {
                throw new IllegalArgumentException("description needed");
            }
        }
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsupdated = database.update(NoteEntry.TABLE_NAME, values, selection, selectionArgs);
        if (rowsupdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsupdated;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsDeleted;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case NOTES:
                rowsDeleted = database.delete(NoteEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case NOTE_ID:
                selection = NoteEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(NoteEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for" + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;


    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case NOTES:
                return NoteEntry.CONTENT_LIST_TYPE;
            case NOTE_ID:
                return NoteEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown  URI" + uri + "with match" + match);
        }
    }

}
