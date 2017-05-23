package com.place.finder.component;

/**
 * Created by odyssey on 5/19/2017.
 */

import com.place.finder.component.module.NetworkModule;
import com.place.finder.view.activity.MyMapActivity;
import com.place.finder.view.activity.MyLocationActivity;
import com.place.finder.view.activity.PlaceFinderActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {NetworkModule.class,})
public interface DraggerDependency {
    void inject(PlaceFinderActivity placeFinderActivity);
    void inject(MyLocationActivity myLocationActivity);
    void inject(MyMapActivity myMapActivity);
}