package com.navinnayak.android.todoapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.navinnayak.android.todoapp.data.NoteContract.NoteEntry;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int NOTE_LOADER = 0;
    @BindView(R.id.titleEdit)
    EditText titleTextEdit;
    @BindView(R.id.descEdit)
    EditText descTextEdit;
    NoteCursorAdapter mCursorAdapter;
    private boolean mNoteHasChanged = false;
    private Uri mCurrentNoteUri;

//    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
//        @Override
//        public boolean onTouch(View view, MotionEvent motionEvent) {
//            mNoteHasChanged = true;
//            Log.d("message", "onTouch");
//            return false;
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        ButterKnife.bind(this);


        Intent intent = getIntent();
        mCurrentNoteUri = intent.getData();
//        titleTextEdit.setOnTouchListener(mTouchListener);
//        descTextEdit.setOnTouchListener(mTouchListener);
        getSupportLoaderManager().initLoader(NOTE_LOADER, null, this);
    }

    public void saveNote() {

        String titleString = titleTextEdit.getText().toString();
        String descString = descTextEdit.getText().toString();

        ContentValues values = new ContentValues();
        values.put(NoteEntry.COLUMN_TITLE, titleString);
        values.put(NoteEntry.COLUMN_DESC, descString);

        if (mCurrentNoteUri == null) {
            Uri newUri = getContentResolver().insert(NoteEntry.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(this, getString(R.string.editor_insert_note_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_insert_note_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsAffected = getContentResolver().update(mCurrentNoteUri, values, null, null);
            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.editor_update_note_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_update_note_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }


    }


    public void deleteNote() {
        if (mCurrentNoteUri == null) {

            int rowsDeleted = getContentResolver().delete(mCurrentNoteUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.editor_delete_note_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_delete_note_successful),
                        Toast.LENGTH_SHORT).show();
            }
            finish();

        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                NoteEntry._ID,
                NoteEntry.COLUMN_TITLE,
                NoteEntry.COLUMN_DESC
        };
        return new CursorLoader(this,
                mCurrentNoteUri,
                projection,
                null,
                null,
                null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);

//        if (cursor == null || cursor.getCount() < 1) {
//            return;
//        }
//        if (cursor.moveToFirst()) {
//
//
//            int titleColumnIndex = cursor.getColumnIndex(NoteEntry.COLUMN_TITLE);
//            int descColumnIndex = cursor.getColumnIndex(NoteEntry.COLUMN_DESC);
//
//            String currentTitle = cursor.getString(titleColumnIndex);
//            String currentDesc = cursor.getString(descColumnIndex);
//
//            titleTextEdit.setText(currentTitle);
//            descTextEdit.setText(currentDesc);
//
//
//        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

//        titleTextEdit.setText("");
//        descTextEdit.setText("");
    }


//
//    @Override
//    public void onBackPressed() {
//        if (!mProductHasChanged) {
//            super.onBackPressed();
//            return;
//        }
//        DialogInterface.OnClickListener discardButtonClickListener =
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        finish();
//                    }
//                };
//        showUnsavedChangesDialog(discardButtonClickListener);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveNote();
                finish();
                return true;

            case R.id.action_delete:
                deleteNote();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


}