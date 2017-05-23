package com.place.finder.presenter;

/**
 * Created by rohit.anvekar on 5/19/2017.
 */


import android.util.Log;

import com.place.finder.component.network.NetworkError;
import com.place.finder.component.service.Service;
import com.place.finder.model.Place;
import com.place.finder.model.Result;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class PlaceFindPresenter {
    private final Service service;
    private final PlaceFinderView view;
    private CompositeSubscription subscriptions;

    public PlaceFindPresenter(Service service, PlaceFinderView view) {
        this.service = service;
        this.view = view;
        this.subscriptions = new CompositeSubscription();
    }

    public void getPlaceDetailList(String query) {
        view.showProgress();

        Subscription subscription = service.getPlaceList(query,new Service.GetPlaceListCallback() {
            @Override
            public void onSuccess(Place place) {
                Log.d("onSuccess",""+place);
                view.hideProgress();
                view.getPlaceList(place);
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