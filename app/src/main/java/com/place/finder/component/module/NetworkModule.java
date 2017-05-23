package com.place.finder.component.module;

import android.util.Log;

import com.place.finder.BuildConfig;
import com.place.finder.component.network.PlaceAPIService;
import com.place.finder.component.service.Service;

import java.io.File;
import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by rohit.anvekar on 5/19/2017.
 */
@Module
public class NetworkModule {

    File cacheFile;

    public NetworkModule(File cacheFile) {
        this.cacheFile = cacheFile;
    }


    @Provides
    @Singleton
    Retrofit provideCall() {
        Cache cache = null;

        try{
            cache = new Cache(cacheFile, 10 * 1024 * 1024);

        }catch (Exception e){

            e.printStackTrace();
        }

        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request orignal = chain.request();

                Request request = orignal.newBuilder().header("Content-Type", "application/json")
                        .removeHeader("Pragma")
                        .header("Cache-Control", String.format("max-age=%d", BuildConfig.CACHETIME))
                        .build();


                Log.i("request.url():",""+request.url());

                Response response = chain.proceed(request);
                response.cacheResponse();

                Log.i("response:",""+response.toString());
                // Customize or return the response
                return response;
            }
        })
                .cache(cache)

                .build();


        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASEURL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public PlaceAPIService providesNetworkService(
            Retrofit retrofit) {
        return retrofit.create(PlaceAPIService.class);
    }
    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public Service providesService(
            PlaceAPIService placeAPIService) {
        return new Service(placeAPIService);
    }

}
