package com.navinnayak.android.todoapp;

import android.content.ContentUris;
import android.content.ContentValues;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.navinnayak.android.todoapp.data.NoteContract.NoteEntry;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int NOTE_LOADER = 0;
    NoteCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fabAddInitialize();
        ListView noteLV = findViewById(R.id.list);

        mCursorAdapter = new NoteCursorAdapter(this, null);
        noteLV.setAdapter(mCursorAdapter);

        noteLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, final long ID) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                Uri currentNoteUri = ContentUris.withAppendedId(NoteEntry.CONTENT_URI, ID);
                intent.setData(currentNoteUri);
                startActivity(intent);
            }
        });
        getSupportLoaderManager().initLoader(NOTE_LOADER, null, this);
    }

    void fabAddInitialize() {
        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                NoteEntry._ID,
                NoteEntry.COLUMN_TITLE,
                NoteEntry.COLUMN_DESC,
                NoteEntry.COLUMN_DATE_TIME

        };
        return new CursorLoader(this,
                NoteEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    private void insertProduct() {
        SimpleDateFormat dtFormat = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss");

        ContentValues values = new ContentValues();
        values.put(NoteEntry.COLUMN_TITLE, "Sennheiser PXC 550");
        values.put(NoteEntry.COLUMN_DESC, "29999 hsdfihjodjoc dfijo ");
        values.put(NoteEntry.COLUMN_DATE_TIME, dtFormat.format(new Date()));

        Uri newUri = getContentResolver().insert(NoteEntry.CONTENT_URI, values);
        if (newUri == null) {
            Toast.makeText(this, getString(R.string.editor_insert_note_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.editor_insert_note_successful),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_dummy:
                insertProduct();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}