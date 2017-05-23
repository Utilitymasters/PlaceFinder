package com.place.finder.view.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.place.finder.R;
import com.place.finder.component.service.Service;
import com.place.finder.model.Geometry;
import com.place.finder.model.Photo;
import com.place.finder.model.PlacePhotoResult;
import com.place.finder.model.PlaceResults;
import com.place.finder.model.Result;
import com.place.finder.presenter.MyLocationPresenter;
import com.place.finder.presenter.MyLocationView;
import com.place.finder.utils.Utils;

import java.util.List;

import javax.inject.Inject;

public class MyMapActivity extends BaseApp implements OnMapReadyCallback ,MyLocationView{

    public static final String TAG = "MyMapActivity";

    @Inject
    public Service service;

    private GoogleMap mMap;

    private Result placeResultDetail;
    private Geometry geometry;
    public MyLocationPresenter myMapPresenter;
     LatLng placeLatlng;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        getDraggerDependency().inject(this);
        myMapPresenter = new MyLocationPresenter(service, this);


        if (getIntent() != null) {
            Bundle bundle = getIntent().getBundleExtra("placeData");

            if (bundle != null) {
                placeResultDetail = (Result) bundle.getParcelable("placeResult");
                geometry = placeResultDetail.getGeometry();
                Log.d(TAG, "onCreate: "+placeResultDetail.toString());
            }
        }
        myMapPresenter.getPlacePhotos(placeResultDetail.getPlaceId());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Log.d("Location LaLng:", "" + geometry.getLocation().getLat() + " : " + geometry.getLocation().getLng());
        // Add a marker in place and move the camera
        placeLatlng = new LatLng(geometry.getLocation().getLat(), geometry.getLocation().getLng());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(placeLatlng);
        markerOptions.title(placeResultDetail.getName());

      /*  Photo url =placeResultDetail.getPhotos().get(0);
        GroundOverlayOptions newarkMap = new GroundOverlayOptions().image(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_black_24dp))
                .position(place, 8600f, 6500f);*/

        mMap.addMarker(markerOptions);
        //mMap.addGroundOverlay(newarkMap);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(placeLatlng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                Log.d(TAG, "onMarkerClick: ");

                return false;
            }
        });


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);


    }


    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onFailure(String errorMessage) {

    }

    @Override
    public void getCurrentPlaceResult(PlaceResults result) {

    }

    @Override
    public void getPlacePhotos(PlacePhotoResult place) {
        List<Photo> photoList =place.getResult().getPhotos();

        if(photoList!=null & photoList.size()>0) {

            downloadPhoto(photoList);
        }else
        {
            Log.e("Get photo:", "No photo available");
        }
    }

    public void downloadPhoto(final List<Photo> photoList) {

        if (photoList != null & photoList.size() > 0) {

                PhotoDownloadTask photoDownloadTask = new PhotoDownloadTask();

                photoDownloadTask.execute(Utils.getPhotoUrl(photoList.get(0).getPhotoReference()));

        } else {

            Log.e("Get photo:", "No photo found");

        }

    }

    private class PhotoDownloadTask extends AsyncTask<String, Integer, Bitmap> {
        Bitmap bitmap = null;

        @Override
        protected Bitmap doInBackground(String... url) {
            try {
                // Starting image download
                bitmap = Utils.downloadImage(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            BitmapDescriptor image = BitmapDescriptorFactory.fromBitmap(bitmap);
            GroundOverlayOptions groundOverlay = new GroundOverlayOptions()
                    .image(image)
                    .position(placeLatlng, 1500f)
                    .transparency(0.5f);
            mMap.addGroundOverlay(groundOverlay);
        }
    }
}
