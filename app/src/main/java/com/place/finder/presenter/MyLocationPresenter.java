package com.place.finder.presenter;

import android.util.Log;

import com.place.finder.component.network.NetworkError;
import com.place.finder.component.service.Service;
import com.place.finder.model.PlacePhotoResult;
import com.place.finder.model.PlaceResults;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by rohit.anvekar on 5/22/2017.
 */

public class MyLocationPresenter {
    private final Service service;
    private final MyLocationView view;
    private CompositeSubscription subscriptions;

    public MyLocationPresenter(Service service, MyLocationView view) {
        this.service = service;
        this.view = view;
        this.subscriptions = new CompositeSubscription();
    }


    public void getCurrentPlace(String currentAddress) {
        view.showProgress();

        Subscription subscription = service.getCurrentPlaceByAddress(currentAddress,new Service.GetCurrentPlaceResultCallback() {
            @Override
            public void onSuccess(PlaceResults result) {
                Log.d("onSuccess",""+result);
                view.hideProgress();
                view.getCurrentPlaceResult(result);
            }

            @Override
            public void onError(NetworkError networkError) {
                Log.d("onError",""+networkError.getMessage());
                view.hideProgress();
                view.onFailure(networkError.getAppErrorMessage());
            }

        });

        subscriptions.add(subscription);
    }


    public void getPlacePhotos(String placeid) {
        view.showProgress();

        Subscription subscription = service.getPlacePhotos(placeid,new Service.GetPlacePhotoResultCallback() {
            @Override
            public void onSuccess(PlacePhotoResult result) {
                Log.d("onSuccess",""+result);
                view.hideProgress();
                view.getPlacePhotos(result);
            }

            @Override
            public void onError(NetworkError networkError) {
                Log.d("onError",""+networkError.getMessage());
                view.hideProgress();
                view.onFailure(networkError.getAppErrorMessage());
            }

        });

        subscriptions.add(subscription);
    }

    public void onStop() {
        subscriptions.unsubscribe();
    }
}

