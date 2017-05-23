package com.place.finder.view.activity;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.place.finder.R;
import com.place.finder.component.service.Service;
import com.place.finder.model.Photo;
import com.place.finder.model.PlacePhotoResult;
import com.place.finder.model.PlaceResults;
import com.place.finder.presenter.MyLocationPresenter;
import com.place.finder.presenter.MyLocationView;
import com.place.finder.view.fragment.MyLocationFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rohit.anvekar on 5/20/2017.
 */

public class MyLocationActivity extends BaseApp implements MyLocationView{

    @Inject
    public Service service;
    @BindView(R.id.myLocationFragmentContainer)
    FrameLayout mMyLocationFragmentContainer;


    MyLocationFragment mMyLocationFragment;

    public MyLocationPresenter myLocationPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_location);
        ButterKnife.bind(this);
        getDraggerDependency().inject(this);
        myLocationPresenter = new MyLocationPresenter(service, this);
        addMyLocationFragment();
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

    public void addMyLocationFragment() {
        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (mMyLocationFragmentContainer != null) {

            // Create a new Fragment to be placed in the activity layout
            mMyLocationFragment = new MyLocationFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            mMyLocationFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(mMyLocationFragmentContainer.getId(), mMyLocationFragment).commit();
        }
    }

    @Override
    public void showProgress() {
        mMyLocationFragment.startProgress();
    }

    @Override
    public void hideProgress() {
        mMyLocationFragment.stopProgress();
    }

    @Override
    public void onFailure(String errorMessage) {
        mMyLocationFragment.hidePhotoList();
    }

    @Override
    public void getCurrentPlaceResult(PlaceResults result) {

        myLocationPresenter.getPlacePhotos(result.getResults().get(0).getPlaceId());


    }

    @Override
    public void getPlacePhotos(PlacePhotoResult place) {

        List<Photo> photoList =place.getResult().getPhotos();

        if(photoList!=null & photoList.size()>0) {

            mMyLocationFragment.addPhotoList(photoList);
        }else
        {
            mMyLocationFragment.hidePhotoList();
        }

    }
}
