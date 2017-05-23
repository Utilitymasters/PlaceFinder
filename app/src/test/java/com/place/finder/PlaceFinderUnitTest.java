package com.place.finder;

import com.place.finder.component.database.DatabaseHelper;
import com.place.finder.model.RecentHistoryItem;
import com.place.finder.utils.Utils;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

/**
 * PlaceFinderUnitTest local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class PlaceFinderUnitTest {


    @Test
    public void buildUrl() {

        String url = "https://maps.googleapis.com/maps/api/place/photo?&key=AIzaSyCefwvVJdjzYENGMYxVmhM1QJsDYqy40Fk&sensor=true&maxwidth=200&maxheight=200&photoreference=CmRYAAAAhqcZZKZbAboFvbwAq7oHiLTgOGwNiL21SrnnYTeU79";


        Assert.assertEquals(url,Utils.getPhotoUrl("CmRYAAAAhqcZZKZbAboFvbwAq7oHiLTgOGwNiL21SrnnYTeU79"));
    }

}