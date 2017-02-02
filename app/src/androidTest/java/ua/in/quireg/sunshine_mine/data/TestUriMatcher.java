/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ua.in.quireg.sunshine_mine.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import ua.in.quireg.sunshine_mine.data.WeatherContract.LocationEntry;
import ua.in.quireg.sunshine_mine.data.WeatherContract.WeatherByDayEntry;
import ua.in.quireg.sunshine_mine.data.WeatherContract.WeatherByHourEntry;
import ua.in.quireg.sunshine_mine.data.WeatherContract.CurrentWeatherEntry;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class TestUriMatcher{

    private static String testLoc = String.valueOf(TestUtilities.TEST_LOCATION);

    // content://com.example.android.sunshine.app/weather_by_hour/location_id"
    private static final Uri TEST_WEATHER_BY_HOUR_WITH_LOC = WeatherByHourEntry.CONTENT_URI
            .buildUpon().appendPath(testLoc).build();

    // content://com.example.android.sunshine.app/weather_by_day/location_id"
    private static final Uri TEST_WEATHER_BY_DAY_WITH_LOC = WeatherByDayEntry.CONTENT_URI
            .buildUpon().appendPath(testLoc).build();

    // content://com.example.android.sunshine.app/weather_current/location_id"
    private static final Uri TEST_WEATHER_CURRENT_WITH_LOC = CurrentWeatherEntry.CONTENT_URI
            .buildUpon().appendPath(testLoc).build();

    // content://com.example.android.sunshine.app/location/location_id"
    private static final Uri TEST_LOCATION_WITH_ID = LocationEntry.CONTENT_URI
            .buildUpon().appendPath(testLoc).build();

    @Test
    public void testUriMatcher() {
        UriMatcher testMatcher = WeatherProvider.buildUriMatcher();


        assertEquals("Error: The WEATHER_CURRENT_WITH_LOC was matched incorrectly.",
                testMatcher.match(TEST_WEATHER_CURRENT_WITH_LOC), WeatherProvider.WEATHER_CURRENT_WITH_LOC);


        assertEquals("Error: The WEATHER_BY_HOUR_WITH_LOC was matched incorrectly.",
                testMatcher.match(TEST_WEATHER_BY_HOUR_WITH_LOC), WeatherProvider.WEATHER_BY_HOUR_WITH_LOC);


        assertEquals("Error: The WEATHER_BY_DAY_WITH_LOC was matched incorrectly.",
                testMatcher.match(TEST_WEATHER_BY_DAY_WITH_LOC), WeatherProvider.WEATHER_BY_DAY_WITH_LOC);


        assertEquals("Error: The LOCATION_WITH_ID was matched incorrectly.",
                testMatcher.match(TEST_LOCATION_WITH_ID), WeatherProvider.LOCATION_WITH_ID);
    }
}
