package com.place.finder.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.place.finder.R;
import com.place.finder.model.Location;
import com.place.finder.model.Place;
import com.place.finder.model.Result;
import com.place.finder.utils.RecyclerItemClickListener;
import com.place.finder.view.activity.MyMapActivity;
import com.place.finder.view.adapter.PlaceListAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by rohit.anvekar on 5/18/2017.
 */

public class PlaceFinderFragment extends Fragment {

    @BindView(R.id.placeRecyclerView)
    public RecyclerView mRecyclerView;
    @BindView(R.id.searchMessage)
    TextView searchMessage;
    @BindView(R.id.progressBar)
    public ProgressBar mProgressBar;



    private PlaceListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Unbinder unbinder;
    private List<Result> resultList;
    private Place mPlace;
    public PlaceFinderFragment() {
        super();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view = layoutInflater.inflate(R.layout.fragment_place_view, viewGroup, false);
        unbinder = ButterKnife.bind(this, view);
        // TODO Use fields...
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    public void startProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }


    public void stopProgress() {
        mProgressBar.setVisibility(View.GONE);
        searchMessage.setVisibility(View.VISIBLE);
    }


    public void addPlaceItem(final Place place) {
        mPlace =place;

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // show map option to select either google map or custom my map.
                        displayMapOptions(mPlace,position);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );


        resultList =  place.getResults();

        if(resultList!=null && resultList.size()>0) {
            // specify an adapter (see also next example)
            mAdapter = new PlaceListAdapter(getActivity(), resultList);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            mRecyclerView.setVisibility(View.VISIBLE);
            searchMessage.setText(R.string.search_results);
            searchMessage.setVisibility(View.VISIBLE);
        }else
        {
            mRecyclerView.setVisibility(View.GONE);
            searchMessage.setText(R.string.no_place_found);
            searchMessage.setVisibility(View.VISIBLE);
        }


    }


    public void hidePlaceList() {
       mRecyclerView.setVisibility(View.GONE);
    }

    /**
     * Give option to choose either google map or my map to display selected place on map.
     * @param mPlace
     * @param position
     */
    private void displayMapOptions(final Place mPlace, final int position) {
        AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.MyAlertDialogStyle);
        alertDialogBuilder.setTitle("Select Map");
        alertDialogBuilder.setMessage("Please choose map options to see place");

        alertDialogBuilder.setNegativeButton("Google Map", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
                Result result = mPlace.getResults().get(position);
                Location location = result.getGeometry().getLocation();
                Uri gmmIntentUri = Uri.parse("geo:" + location.getLat() + "," + location.getLng() + "?q=" + result.getName());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        alertDialogBuilder.setNeutralButton("My Map", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                Intent intent = new Intent(getActivity(), MyMapActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("placeResult", mPlace.getResults().get(position));
                intent.putExtra("placeData", bundle);
                getActivity().startActivity(intent);
            }
        });

        /*alertDialogBuilder.setPositiveButton("No Thanks", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });*/

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
}
