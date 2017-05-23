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

    @GET("place/photo?&key="+ BuildConfig.API_KEY)
    Observable<Place> getPhoto(@Query("maxwidth") String maxwidth,@Query("photoreference") String photoreference);


   /* @GET("geocode/json?&key="+ BuildConfig.API_KEY)
    Observable<Result> getCurrentPlace(@Query("address") String address);*/


    @GET("geocode/json?&key="+ BuildConfig.API_KEY)
    Observable<PlaceResults> getCurrentPlace(@Query("address") String address);

 //   https://maps.googleapis.com/maps/api/geocode/json

//    https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=CnRtAAAATLZNl354RwP_9UKbQ_5Psy40texXePv4oAlgP4qNEkdIrkyse7rPXYGd9D_Uj1rVsQdWT4oRz4QrYAJNpFX7rzqqMlZw2h2E2y5IKMUZ7ouD_SlcHxYq1yL4KbKUv3qtWgTK0A6QbGh87GB3sscrHRIQiG2RrmU_jF4tENr9wGS_YxoUSSDrYjWmrNfeEHSGSc3FyhNLlBU&key=YOUR_API_KEY

//    https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=500&types=food&name=cruise&key=YOUR_API_KEY


}