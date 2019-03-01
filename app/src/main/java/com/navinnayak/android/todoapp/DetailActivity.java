package com.navinnayak.android.todoapp;

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
import android.widget.TextView;

import com.navinnayak.android.todoapp.data.NoteContract.NoteEntry;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.titleDetail)
    TextView titleTextDetail;
    @BindView(R.id.descDetail)
    TextView descTextDetail;
    NoteCursorAdapter mCursorAdapter;
    private Uri mCurrentNoteUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);


        FloatingActionButton fabEdit = findViewById(R.id.fab_edit);
        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intnet = new Intent(DetailActivity.this, EditorActivity.class);
                startActivity(intnet);
            }
        });


        Intent intent = getIntent();
        mCurrentNoteUri = intent.getData();


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
//        if (cursor == null || cursor.getCount() < 1) {
//            return;
//        }
//        if (cursor.moveToFirst()) {
//            int titleColumnIndex = cursor.getColumnIndex(NoteEntry.COLUMN_TITLE);
//            int descColumnIndex = cursor.getColumnIndex(NoteEntry.COLUMN_DESC);
//
//
//            String currentTitle = cursor.getString(titleColumnIndex);
//            String currentDesc = cursor.getString(descColumnIndex);
//
//            titleTextDetail.setText(currentTitle);
//            descTextDetail.setText(currentDesc);
//
//
//        }
        mCursorAdapter.swapCursor(cursor);


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }


}
