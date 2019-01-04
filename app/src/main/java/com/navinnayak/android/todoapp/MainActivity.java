package com.navinnayak.android.todoapp;

import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.database.Cursor;
import com.navinnayak.android.todoapp.data.NoteContract.NoteEntry;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.navinnayak.android.todoapp.data.NoteContract;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<cursor> {

    private static final int NOTE_LOADER = 0;

    NoteCursorAdapter = mCursorAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intnet = new Intent(MainActivity.this, DetailActivity.class);
                startActivity(intnet);
            }
        });
        ListView noteLV = findViewById(R.id.list);


        mCursorAdapter = new NoteCursorAdapter(this, null);
        noteLV.setAdapter(mCursorAdapter);

        noteLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                Uri currentNoteUri = ContentUris.withAppendedId(NoteEntry.CONTENT_URI, id);
                intent.setData(currentNoteUri);
                startActivity(intent);
            }
        });
        getLoaderManager().initLoader(NOTE_LOADER, null, this);

    }
}