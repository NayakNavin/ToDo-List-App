package com.navinnayak.android.todoapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.navinnayak.android.todoapp.data.NoteContract.NoteEntry;

import static com.navinnayak.android.todoapp.EditorActivity.EXISTING_NOTE_LOADER;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private TextView titleTextDetail;
    private TextView descTextDetail;
    NoteCursorAdapter mCursorAdapter;
    private Uri mCurrentNoteUri;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initializeViews();
        fabEditInitialize();

        LinearLayout lv = findViewById(R.id.detail_layout);

        lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, EditorActivity.class);
                intent.setData(mCurrentNoteUri);
                startActivity(intent);
            }
        });

        if (mCurrentNoteUri != null) {
            getSupportLoaderManager().initLoader(EXISTING_NOTE_LOADER, null, this);
        }
    }

    void initializeViews() {
        titleTextDetail = findViewById(R.id.titleDetail);
        descTextDetail = findViewById(R.id.descDetail);

        Intent intent = getIntent();
        mCurrentNoteUri = intent.getData();

    }

    void fabEditInitialize() {

        FloatingActionButton fabEdit = findViewById(R.id.fab_edit);
        fabEdit.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, EditorActivity.class);
                intent.setData(mCurrentNoteUri);
                startActivity(intent);
            }
        });
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

            titleTextDetail.setText(currentTitle);
            descTextDetail.setText(currentDesc);

            title = currentTitle;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        titleTextDetail.setText("");
        descTextDetail.setText("");
    }

    public void deleteNote() {
        int rowsDeleted = getContentResolver().delete(mCurrentNoteUri, null, null);
        Toast.makeText(this, getString(R.string.editor_delete_note_successful), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(DetailActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete);
        builder.setMessage(R.string.delete_confirmation_dialog);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteNote();
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_detail:
                showDeleteConfirmationDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}