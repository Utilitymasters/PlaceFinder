package com.place.finder.view.adapter;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.place.finder.R;
import com.place.finder.view.activity.PlaceFinderActivity;

import java.util.List;

/**
 * Created by rohit.anvekar on 5/20/2017.
 */

public class RecentHistoryListAdapter extends CursorAdapter {


    private TextView text;

    public RecentHistoryListAdapter(Context context, Cursor cursor) {

        super(context, cursor, false);

    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        //   text.setText(items.get(cursor.getPosition()));
        final int textIndex = cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1);
        text.setText(cursor.getString(textIndex));


        ((PlaceFinderActivity) context).myAutoComplete.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int i) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {

                final int textIndex = cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1);
                text.setText(cursor.getString(textIndex));
                ((PlaceFinderActivity) context).myAutoComplete.setQuery(text.getText(), true);
                return true;
            }
        });

    }

    @Override
    public View newView(final Context context, Cursor cursor, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.recent_history_item, parent, false);

        text = (TextView) view.findViewById(R.id.recentItemText);


        return view;

    }

}
