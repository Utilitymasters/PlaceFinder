package com.place.finder.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.place.finder.component.DaggerDraggerDependency;
import com.place.finder.component.DraggerDependency;
import com.place.finder.component.module.NetworkModule;

import java.io.File;

import io.realm.Realm;

/**
 * Created by rohit.anvekar on 5/18/2017.
 */

class BaseApp extends AppCompatActivity{
    DraggerDependency draggerDependency;

    boolean isRealmActive =true;

    Realm realm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File cacheFile = new File(getCacheDir(), "responses");
        draggerDependency = DaggerDraggerDependency.builder().networkModule(new NetworkModule(cacheFile)).build();
        if(isRealmActive) {
            // Initialize Realm
            Realm.init(this);

            // Get a Realm instance for this thread
            realm = Realm.getDefaultInstance();
        }
    }

    public DraggerDependency getDraggerDependency() {
        return draggerDependency;
    }
}
