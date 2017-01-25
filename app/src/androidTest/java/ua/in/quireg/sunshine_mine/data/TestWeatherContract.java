package ua.in.quireg.sunshine_mine.data;

import android.net.Uri;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.runner.RunWith;
import org.junit.Test;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class TestWeatherContract{

    @Test
    public void testBuildWeatherLocation() {
        Uri locationUri = WeatherContract.LocationEntry.buildUri(TestUtilities.TEST_LOCATION);
        assertNotNull("Error: Null Uri returned.", locationUri);

        assertEquals("Error: Weather location not properly appended to the end of the Uri",
                String.valueOf(TestUtilities.TEST_LOCATION), locationUri.getLastPathSegment());

        assertEquals("Error: Weather location Uri doesn't match our expected result",
                locationUri.toString(),
                "content://ua.in.quireg.sunshine_mine.app/location/" + TestUtilities.TEST_LOCATION);
    }
}
