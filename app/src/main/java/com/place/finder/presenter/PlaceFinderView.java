package com.place.finder.presenter;

import com.place.finder.model.Place;
import com.place.finder.model.Result;

/**
 * Created by rohit.anvekar on 5/19/2017.
 */


public interface PlaceFinderView {

    void showProgress();

    void hideProgress();

    void onFailure(String errorMessage);

    void getPlaceList(Place place);

}