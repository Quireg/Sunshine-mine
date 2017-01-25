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
import android.test.AndroidTestCase;

import org.junit.Test;
import org.junit.runner.RunWith;

import ua.in.quireg.sunshine_mine.data.WeatherContract.LocationEntry;
import ua.in.quireg.sunshine_mine.data.WeatherContract.WeatherByDayEntry;
import ua.in.quireg.sunshine_mine.data.WeatherContract.WeatherByHourEntry;
import ua.in.quireg.sunshine_mine.data.WeatherContract.CurrentWeatherEntry;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class TestUriMatcher extends AndroidTestCase {

    // content://com.example.android.sunshine.app/weather_by_hour"
    private static final Uri TEST_WEATHER_BY_HOUR = WeatherByHourEntry.CONTENT_URI;
    private static final Uri TEST_WEATHER_BY_HOUR_WITH_LOC = WeatherByHourEntry.CONTENT_URI
            .buildUpon().appendPath("14881488").build();
    private static final Uri TEST_WEATHER_BY_HOUR_WITH_LOC_AND_DATE = WeatherByHourEntry.CONTENT_URI
            .buildUpon().appendPath("14881488").appendPath("1000").build();
    // content://com.example.android.sunshine.app/weather_by_day"
    private static final Uri TEST_WEATHER_BY_DAY = WeatherByDayEntry.CONTENT_URI;
    private static final Uri TEST_WEATHER_BY_DAY_WITH_LOC = WeatherByDayEntry.CONTENT_URI
            .buildUpon().appendPath("14881488").build();
    private static final Uri TEST_WEATHER_BY_DAY_WITH_LOC_AND_DATE = WeatherByDayEntry.CONTENT_URI
            .buildUpon().appendPath("14881488").appendPath("1000").build();
    // content://com.example.android.sunshine.app/weather_current"
    private static final Uri TEST_WEATHER_CURRENT = CurrentWeatherEntry.CONTENT_URI;
    private static final Uri TEST_WEATHER_CURRENT_WITH_LOC = CurrentWeatherEntry.CONTENT_URI
            .buildUpon().appendPath("14881488").build();
    private static final Uri TEST_WEATHER_CURRENT_WITH_LOC_AND_DATE = CurrentWeatherEntry.CONTENT_URI
            .buildUpon().appendPath("14881488").appendPath("1000").build();
    // content://com.example.android.sunshine.app/location"
    private static final Uri TEST_LOCATION = LocationEntry.CONTENT_URI;
    private static final Uri TEST_LOCATION_WITH_ID = LocationEntry.CONTENT_URI
            .buildUpon().appendPath("14881488").build();

    @Test
    public void testUriMatcher() {
        UriMatcher testMatcher = WeatherProvider.buildUriMatcher();

        assertEquals("Error: The WEATHER_CURRENT URI was matched incorrectly.",
                testMatcher.match(TEST_WEATHER_CURRENT), WeatherProvider.WEATHER_CURRENT);
        assertEquals("Error: The WEATHER_CURRENT_WITH_LOC URI was matched incorrectly.",
                testMatcher.match(TEST_WEATHER_CURRENT_WITH_LOC), WeatherProvider.WEATHER_CURRENT_WITH_LOC);
        assertEquals("Error: The WEATHER_CURRENT_WITH_LOC_AND_DATE URI was matched incorrectly.",
                testMatcher.match(TEST_WEATHER_CURRENT_WITH_LOC_AND_DATE), WeatherProvider.WEATHER_CURRENT_WITH_LOC_AND_DATE);

        assertEquals("Error: The WEATHER_BY_HOUR was matched incorrectly.",
                testMatcher.match(TEST_WEATHER_BY_HOUR), WeatherProvider.WEATHER_BY_HOUR);
        assertEquals("Error: The WEATHER_BY_HOUR_WITH_LOC was matched incorrectly.",
                testMatcher.match(TEST_WEATHER_BY_HOUR_WITH_LOC), WeatherProvider.WEATHER_BY_HOUR_WITH_LOC);
        assertEquals("Error: The WEATHER_BY_HOUR_WITH_LOC_AND_DATE was matched incorrectly.",
                testMatcher.match(TEST_WEATHER_BY_HOUR_WITH_LOC_AND_DATE), WeatherProvider.WEATHER_BY_HOUR_WITH_LOC_AND_DATE);

        assertEquals("Error: The WEATHER_BY_DAY was matched incorrectly.",
                testMatcher.match(TEST_WEATHER_BY_DAY), WeatherProvider.WEATHER_BY_DAY);
        assertEquals("Error: The WEATHER_BY_DAY_WITH_LOC was matched incorrectly.",
                testMatcher.match(TEST_WEATHER_BY_DAY_WITH_LOC), WeatherProvider.WEATHER_BY_DAY_WITH_LOC);
        assertEquals("Error: The WEATHER_BY_DAY_WITH_LOC_AND_DATE was matched incorrectly.",
                testMatcher.match(TEST_WEATHER_BY_DAY_WITH_LOC_AND_DATE), WeatherProvider.WEATHER_BY_DAY_WITH_LOC_AND_DATE);

        assertEquals("Error: The LOCATION URI was matched incorrectly.",
                testMatcher.match(TEST_LOCATION), WeatherProvider.LOCATION);
        assertEquals("Error: The LOCATION_WITH_ID URI was matched incorrectly.",
                testMatcher.match(TEST_LOCATION_WITH_ID), WeatherProvider.LOCATION_WITH_ID);
    }
}
