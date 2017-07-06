package com.place.finder.view.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.database.MatrixCursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;

import com.place.finder.R;
import com.place.finder.component.service.Service;
import com.place.finder.model.RecentHistoryItem;
import com.place.finder.model.Place;
import com.place.finder.component.database.DatabaseHelper;
import com.place.finder.presenter.PlaceFindPresenter;
import com.place.finder.presenter.PlaceFinderView;
import com.place.finder.view.adapter.RecentHistoryListAdapter;
import com.place.finder.view.fragment.PlaceFinderFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.RealmResults;

/**
 * Created by rohit.anvekar on 5/18/2017.
 */

public class PlaceFinderActivity extends BaseApp implements PlaceFinderView {

    private static final String TAG = "PlaceFinderActivity";
    @Inject
    public Service service;
    @BindView(R.id.placeFragmentContainer)
    FrameLayout mPlaceFragmentContainer;
    @BindView(R.id.myLocationCardView)
    CardView myLocationCardView;


    private PlaceFinderFragment mPlaceFinderFragment;
    private PlaceFindPresenter presenter;
    private List<RecentHistoryItem> recentSearchItemList = new ArrayList<>();
    private Menu menu;

    @BindView(R.id.searchView)
    public SearchView myAutoComplete;


    private static final String[] COLUMNS = {
            BaseColumns._ID,
            SearchManager.SUGGEST_COLUMN_TEXT_1,
    };

    /*
   * Change to type CustomAutoCompleteView instead of AutoCompleteTextView
   * since we are extending to customize the view and disable filter
   * The same with the XML view, type will be CustomAutoCompleteView
   */

    RecentHistoryListAdapter recentHistoryListAdapter;
    // adapter for auto-complete
    public ArrayAdapter<String> myAdapter;

    // for database operations
    public DatabaseHelper mDatabaseHelper;

    MatrixCursor cursor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placefinder);
        ButterKnife.bind(this);
        getDraggerDependency().inject(this);
        presenter = new PlaceFindPresenter(service, this);
        setUpView();
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void showProgress() {
        mPlaceFinderFragment.startProgress();
    }

    @Override
    public void hideProgress() {
        mPlaceFinderFragment.stopProgress();

    }

    @Override
    public void onFailure(String errorMessage) {
        Log.i("onFailure:", "" + errorMessage);

    }

    @Override
    public void getPlaceList(Place place) {

        Log.i("getPlaceList", "" + place.toString());
        mPlaceFinderFragment.addPlaceItem(place);
    }


    @OnClick(R.id.myLocationCardView)
    public void myLocation() {

        Intent myLocationIntent = new Intent(this, MyLocationActivity.class);
        startActivity(myLocationIntent);

    }

    /**
     * set up views to display initial view of screen.
     */
    private void setUpView() {
        myAutoComplete.onActionViewExpanded();
        addPlaceFragment();
        new RecentHistoryLoadTask().execute();

    }

    /**
     * add @{@link PlaceFinderFragment} in to the place container to show place lists.
     */
    public void addPlaceFragment() {
        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (mPlaceFragmentContainer != null) {

            // Create a new Fragment to be placed in the activity layout
            mPlaceFinderFragment = new PlaceFinderFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            mPlaceFinderFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(mPlaceFragmentContainer.getId(), mPlaceFinderFragment).commit();
        }
    }

    /**
     * load recent searched history item to display as suggestions.
     */
    public void loadRecentHistory() {

        if(isRealmActive){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    RealmResults<RecentHistoryItem> recentSearchItemList = realm.where(RecentHistoryItem.class).findAll();
                    cursor = new MatrixCursor(COLUMNS);
                    for (int i = 0; i < recentSearchItemList.size(); i++) {
                        cursor.addRow(new String[]{"" + i, recentSearchItemList.get(i).itemName});
                    }
                }
            });
        }else {
            mDatabaseHelper = new DatabaseHelper(PlaceFinderActivity.this);
            recentSearchItemList = mDatabaseHelper.getAll();
            cursor = new MatrixCursor(COLUMNS);
            for (int i = 0; i < recentSearchItemList.size(); i++) {
                cursor.addRow(new String[]{"" + i, recentSearchItemList.get(i).itemName});
            }
        }
    }

    public void addSuggestion(String query) {
        if(isRealmActive){

            cursor.addRow(new String[]{"0", query});
            new RecentHistoryItem(query);
        }else {

            if (mDatabaseHelper.checkIfExists(query) == false) {
                cursor.addRow(new String[]{"0", query});
                mDatabaseHelper.create(new RecentHistoryItem(query));
            }
        }

    }

    /**
     * update the recent history recycler view and show suggestion on search box.
     */
    public void updateView() {
        recentHistoryListAdapter = new RecentHistoryListAdapter(this, cursor);
        myAutoComplete.setSuggestionsAdapter(recentHistoryListAdapter);

        //add query listener to perform search operation on text.
        myAutoComplete.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                mPlaceFinderFragment.hidePlaceList();
                addSuggestion(query);
                presenter.getPlaceDetailList(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recentHistoryListAdapter.notifyDataSetChanged();
                return false;
            }
        });


    }


    private class RecentHistoryLoadTask extends AsyncTask<String, Integer, RecentHistoryItem> {

        @Override
        protected RecentHistoryItem doInBackground(String... url) {

            loadRecentHistory();
            return null;
        }

        @Override
        protected void onPostExecute(RecentHistoryItem recentHistoryItem) {
            updateView();
        }
    }


}