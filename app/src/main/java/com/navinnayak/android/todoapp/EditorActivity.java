package com.navinnayak.android.todoapp;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.navinnayak.android.todoapp.data.NoteContract.NoteEntry;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int EXISTING_NOTE_LOADER = 0;
    private EditText titleTextEdit;
    private EditText descTextEdit;
    NoteCursorAdapter mCursorAdapter;

    private boolean mNoteHasChanged = false;
    private Uri mCurrentNoteUri;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mNoteHasChanged = true;
            Log.d("message", "onTouch");
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Log.d("message", "onCreate");

        initializeViews();

        if (mCurrentNoteUri == null) {
            setTitle(getString(R.string.editor_activity_title_new_note));
            invalidateOptionsMenu();

        } else {
            setTitle(getString(R.string.editor_activity_title_edit_note));
            getSupportLoaderManager().initLoader(EXISTING_NOTE_LOADER, null, this);
        }
        titleTextEdit.setOnTouchListener(mTouchListener);
        descTextEdit.setOnTouchListener(mTouchListener);
    }

    void initializeViews() {
        titleTextEdit = findViewById(R.id.titleEdit);
        descTextEdit = findViewById(R.id.descEdit);
        Intent intent = getIntent();
        mCurrentNoteUri = intent.getData();
    }

    public void saveNote() {
        String titleString = titleTextEdit.getText().toString();
        String descString = descTextEdit.getText().toString();

        ContentValues values = new ContentValues();

        if (!TextUtils.isEmpty(titleString)) {
            values.put(NoteEntry.COLUMN_TITLE, titleString);
        } else {
            Toast.makeText(this, getString(R.string.editor__note_notnull), Toast.LENGTH_SHORT).show();
            return;
        }
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
        if (mCurrentNoteUri != null) {

            int rowsDeleted = getContentResolver().delete(mCurrentNoteUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.editor_delete_note_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_delete_note_successful),
                        Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(EditorActivity.this, MainActivity.class);
            startActivity(intent);
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
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if (cursor.moveToFirst()) {
            int titleColumnIndex = cursor.getColumnIndex(NoteEntry.COLUMN_TITLE);
            int descColumnIndex = cursor.getColumnIndex(NoteEntry.COLUMN_DESC);

            String currentTitle = cursor.getString(titleColumnIndex);
            String currentDesc = cursor.getString(descColumnIndex);

            titleTextEdit.setText(currentTitle);
            descTextEdit.setText(currentDesc);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        titleTextEdit.setText("");
        descTextEdit.setText("");
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_confirmation_dialog);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteNote();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (!mNoteHasChanged) {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentNoteUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete_editor);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save_editor:
                saveNote();
                finish();
                return true;
            case R.id.action_delete_editor:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if (!mNoteHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}