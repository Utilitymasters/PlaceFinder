package com.place.finder.presenter;

import com.place.finder.model.Place;
import com.place.finder.model.PlacePhotoResult;
import com.place.finder.model.PlaceResults;
import com.place.finder.model.Result;

/**
 * Created by rohit.anvekar on 5/22/2017.
 */

public interface MyLocationView {

    void showProgress();

    void hideProgress();

    void onFailure(String errorMessage);


    void getCurrentPlaceResult(PlaceResults result);

    void getPlacePhotos(PlacePhotoResult place);
}
