package com.navinnayak.android.todoapp;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.navinnayak.android.todoapp.data.NoteContract.NoteEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int NOTE_LOADER = 0;
    NoteCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intnet = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intnet);
            }
        });
        ListView noteLV = findViewById(R.id.list);


        mCursorAdapter = new NoteCursorAdapter(this, null);
        noteLV.setAdapter(mCursorAdapter);

        noteLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, final long id) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                Uri currentNoteUri = ContentUris.withAppendedId(NoteEntry.CONTENT_URI, id);
                intent.setData(currentNoteUri);
                startActivity(intent);
            }
        });
        getSupportLoaderManager().initLoader(NOTE_LOADER, null, this);

    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                NoteEntry._ID,
                NoteEntry.COLUMN_TITLE,
                NoteEntry.COLUMN_DESC

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


}