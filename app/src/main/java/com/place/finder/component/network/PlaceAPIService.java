package com.place.finder.component.network;

import com.place.finder.BuildConfig;
import com.place.finder.model.Place;
import com.place.finder.model.PlacePhotoResult;
import com.place.finder.model.PlaceResults;
import com.place.finder.model.Result;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by rohit.anvekar on 5/19/2017.
 */


public interface PlaceAPIService {

    @GET("place/textsearch/json?&key="+ BuildConfig.API_KEY)
    Observable<Place> getPlaceList(@Query("query") String query);

    @GET("place/details/json?&key="+ BuildConfig.API_KEY)
    Observable<PlacePhotoResult> getPlacePhotos(@Query("placeid") String placeid);

    @GET("geocode/json?&key="+ BuildConfig.API_KEY)
    Observable<PlaceResults> getCurrentPlace(@Query("address") String address);


}