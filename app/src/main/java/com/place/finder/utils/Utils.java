package com.place.finder.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;

import com.place.finder.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by rohit.anvekar on 5/22/2017.
 */

public class Utils {


    /**
     * direct dowload file from dynamic url and return bitmap object of image.
     * @param strUrl
     * @return
     * @throws IOException
     */
    public static Bitmap downloadImage(String strUrl) throws IOException {
        Bitmap bitmap = null;
        InputStream iStream = null;
        try {
            URL url = new URL(strUrl);

            /** Creating an http connection to communcate with url */
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            /** Connecting to url */
            urlConnection.connect();

            /** Reading data from url */
            iStream = urlConnection.getInputStream();

            /** Creating a bitmap from the stream returned from the url */
            bitmap = BitmapFactory.decodeStream(iStream);

        } catch (Exception e) {
            Log.d("downloading url", e.toString());
        } finally {
            iStream.close();
        }
        return bitmap;
    }

    /**
     * // Build URL for downloading the photo from Google api.
     *
     * @param photoRef
     * @return
     */

    public static String getPhotoUrl(String photoRef){

        StringBuilder buildUrl = new StringBuilder();
        buildUrl.append("https://maps.googleapis.com/maps/api/place/photo?")
                .append("&key=" + BuildConfig.API_KEY).append("&sensor=true")
                .append("&maxwidth=" + 200)
                .append("&maxheight=" + 200);

        String photoReference = "photoreference=" + photoRef;


        buildUrl.append("&"+photoReference);

       // Log.e("getPhotoUrl:", "" + buildUrl.toString());

        return buildUrl.toString();
    }

    public static boolean isSdCardAvailable(){

       Boolean isAvailable =false;
       if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED) && Environment.isExternalStorageRemovable()){
           isAvailable = true;
       }
        return isAvailable;
    }
}
