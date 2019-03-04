package com.navinnayak.android.todoapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.navinnayak.android.todoapp.data.NoteContract.NoteEntry;

public class NoteCursorAdapter extends CursorAdapter {

    public NoteCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {

        TextView titleTextView = view.findViewById(R.id.titleText);
        TextView descTextView = view.findViewById(R.id.subText);
        TextView dateTextView = view.findViewById(R.id.dateText);

        int dateColunIndex = cursor.getColumnIndex(NoteEntry.COLUMN_DATE_TIME);
        int titleColumnIndex = cursor.getColumnIndex(NoteEntry.COLUMN_TITLE);
        int descColumnIndex = cursor.getColumnIndex(NoteEntry.COLUMN_DESC);

        titleTextView.setText(cursor.getString(titleColumnIndex));
        descTextView.setText(cursor.getString(descColumnIndex));
        dateTextView.setText(cursor.getString(dateColunIndex));

    }
}