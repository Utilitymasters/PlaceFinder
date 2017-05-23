package com.place.finder.component.service;

import com.place.finder.component.network.NetworkError;
import com.place.finder.component.network.PlaceAPIService;
import com.place.finder.model.Place;
import com.place.finder.model.PlacePhotoResult;
import com.place.finder.model.PlaceResults;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by rohit.anvekar on 5/19/2017.
 */

public class Service {
    private final PlaceAPIService placeAPIService;

    public Service(PlaceAPIService placeAPIService){
        this.placeAPIService = placeAPIService;
    }

    public Subscription getPlaceList(String query,final GetPlaceListCallback callback) {

        return placeAPIService.getPlaceList(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends Place>>() {
                    @Override
                    public Observable<? extends Place> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<Place>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(Place place) {
                        callback.onSuccess(place);

                    }
                });
    }

    public Subscription getCurrentPlaceByAddress(String address,final GetCurrentPlaceResultCallback callback) {

        return placeAPIService.getCurrentPlace(address)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends PlaceResults>>() {
                    @Override
                    public Observable<? extends PlaceResults> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<PlaceResults>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(PlaceResults result) {
                        callback.onSuccess(result);

                    }
                });
    }

    public Subscription getPlacePhotos(String query,final GetPlacePhotoResultCallback callback) {

        return placeAPIService.getPlacePhotos(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends PlacePhotoResult>>() {
                    @Override
                    public Observable<? extends PlacePhotoResult> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<PlacePhotoResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(PlacePhotoResult place) {
                        callback.onSuccess(place);

                    }
                });
    }

    public interface GetPlaceListCallback {

        void onSuccess(Place place);

        void onError(NetworkError networkError);
    }

    public interface GetCurrentPlaceResultCallback {

        void onSuccess(PlaceResults result);

        void onError(NetworkError networkError);
    }

    public interface GetPlacePhotoResultCallback {

        void onSuccess(PlacePhotoResult place);

        void onError(NetworkError networkError);
    }

}
