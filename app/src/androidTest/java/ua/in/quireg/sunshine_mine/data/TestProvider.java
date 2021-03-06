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

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import ua.in.quireg.sunshine_mine.data.WeatherContract.LocationEntry;
import ua.in.quireg.sunshine_mine.data.WeatherContract.WeatherByDayEntry;
import ua.in.quireg.sunshine_mine.data.WeatherContract.WeatherByHourEntry;
import ua.in.quireg.sunshine_mine.data.WeatherContract.CurrentWeatherEntry;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestProvider{

    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    private Context mContext;

    public TestProvider(){
        this.mContext = InstrumentationRegistry.getTargetContext();
        WeatherDbHelper.importDatabase(this.mContext);
    }

    @Before
    public void setUp() {
        //deleteTheDatabase();
        deleteAllRecordsFromProvider();
    }

    @After
    public void deleteTheDatabase() {
        //this.mContext.deleteDatabase(WeatherDbHelper.DATABASE_NAME);
    }

    @Test
    public void deleteAllRecordsFromProvider() {

        ContentResolver contentResolver = mContext.getContentResolver();

        contentResolver.delete(
                CurrentWeatherEntry.CONTENT_URI,
                null,
                null
        );
        contentResolver.delete(
                WeatherByHourEntry.CONTENT_URI,
                null,
                null
        );
        contentResolver.delete(
                WeatherByDayEntry.CONTENT_URI,
                null,
                null
        );
        //Only test location is deleted to keep DB consistent.
        contentResolver.delete(LocationEntry.CONTENT_URI
                        .buildUpon()
                        .appendPath(String.valueOf(TestUtilities.TEST_LOCATION))
                        .build(),
                null,
                null
        );

        Cursor cursor = contentResolver.query(
                CurrentWeatherEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from CurrentWeather table during delete", 0, cursor != null ? cursor.getCount() : -1);
        cursor.close();

        cursor = mContext.getContentResolver().query(
                WeatherByDayEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from WeatherByDay table during delete", 0, cursor != null ? cursor.getCount() : -1);
        cursor.close();

        cursor = mContext.getContentResolver().query(
                WeatherByHourEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from WeatherByHour table during delete", 0, cursor != null ? cursor.getCount() : -1);
        cursor.close();
    }

    /*
        This test checks to make sure that the content provider is registered correctly.
     */
    @Test
    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        // We define the component name based on the package name from the context and the
        // WeatherProvider class.
        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                WeatherProvider.class.getName());
        try {
            // Fetch the provider info using the component name from the PackageManager
            // This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            // Make sure that the registered authority matches the authority from the Contract.
            assertEquals("Error: WeatherProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + WeatherContract.CONTENT_AUTHORITY,
                    providerInfo.authority, WeatherContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            // I guess the provider isn't registered correctly.
            assertTrue("Error: WeatherProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }

    /*
            This test doesn't touch the database.  It verifies that the ContentProvider returns
            the correct type for each type of URI that it can handle.
         */
    @Test
    public void testGetType() {
        // content://com.example.android.sunshine.app/weather_current/#
        Uri uri = CurrentWeatherEntry.CONTENT_URI;
        String type = mContext.getContentResolver().getType(uri);
        // vnd.android.cursor.dir/com.example.android.sunshine.app/weather_current
        assertEquals("Error: the CurrentWeatherEntry CONTENT_URI should return CurrentWeatherEntry.CONTENT_TYPE",
                CurrentWeatherEntry.CONTENT_TYPE, type);

        // content://com.example.android.sunshine.app/weather_by_day/
        type = mContext.getContentResolver().getType(Uri.parse(WeatherByDayEntry.CONTENT_URI + "/#"));
        // vnd.android.cursor.dir/com.example.android.sunshine.app/weather_by_day
        assertEquals("Error: the WeatherByDayEntry CONTENT_URI should return WeatherByDayEntry.CONTENT_TYPE",
                WeatherByDayEntry.CONTENT_TYPE, type);

        // content://com.example.android.sunshine.app/weather_by_hour/
        type = mContext.getContentResolver().getType(Uri.parse(WeatherByHourEntry.CONTENT_URI + "/#"));
        // vnd.android.cursor.dir/com.example.android.sunshine.app/weather_by_hour
        assertEquals("Error: the WeatherByHourEntry CONTENT_URI should return WeatherByHourEntry.CONTENT_TYPE",
                WeatherByHourEntry.CONTENT_TYPE, type);

        // content://com.example.android.sunshine.app/location/
        type = mContext.getContentResolver().getType(Uri.parse(LocationEntry.CONTENT_URI + "/#"));
        // vnd.android.cursor.dir/com.example.android.sunshine.app/location
        assertEquals("Error: the LocationEntry CONTENT_URI should return LocationEntry.CONTENT_TYPE",
                WeatherContract.LocationEntry.CONTENT_TYPE, type);
    }

    @Test
    public void testBasicWeatherByHourQuery() {
        // insert our test records into the database
        WeatherDbHelper dbHelper = new WeatherDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        TestUtilities.insertTestLocationValues(mContext);

        ContentValues weatherValues = TestUtilities.createWeatherByHourValues(TestUtilities.TEST_LOCATION);

        long weatherRowId = db.insert(WeatherByHourEntry.TABLE_NAME, null, weatherValues);
        assertTrue("Unable to Insert WeatherByHourEntry into the Database", weatherRowId != -1);

        db.close();

        // Test the basic content provider query
        Cursor weatherCursor = mContext.getContentResolver().query(
                WeatherByHourEntry.buildUri(TestUtilities.TEST_LOCATION),
                null, // leaving "columns" null just returns all the columns.
                LocationEntry._ID, // cols for "where" clause
                new String[]{String.valueOf(TestUtilities.TEST_LOCATION)}, // values for "where" clause
                null  // sort order
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicWeatherByHourQuery", weatherCursor, weatherValues);
    }

    @Test
    public void testBasicWeatherByDayQuery() {
        // insert our test records into the database
        WeatherDbHelper dbHelper = new WeatherDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        TestUtilities.insertTestLocationValues(mContext);

        ContentValues weatherValues = TestUtilities.createWeatherByDayValues(TestUtilities.TEST_LOCATION);

        long weatherRowId = db.insert(WeatherByDayEntry.TABLE_NAME, null, weatherValues);
        assertTrue("Unable to Insert WeatherByDayEntry into the Database", weatherRowId != -1);

        db.close();

        // Test the basic content provider query
        Cursor weatherCursor = mContext.getContentResolver().query(
                WeatherByDayEntry.buildUri(TestUtilities.TEST_LOCATION),
                null, // leaving "columns" null just returns all the columns.
                LocationEntry._ID, // cols for "where" clause
                new String[]{String.valueOf(TestUtilities.TEST_LOCATION)}, // values for "where" clause
                null  // sort order
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicWeatherByDayQuery", weatherCursor, weatherValues);
    }

    @Test
    public void testBasicWeatherCurrentQuery() {
        // insert our test records into the database
        WeatherDbHelper dbHelper = new WeatherDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        TestUtilities.insertTestLocationValues(mContext);

        ContentValues weatherValues = TestUtilities.createWeatherCurrentValues(TestUtilities.TEST_LOCATION);

        long weatherRowId = db.insert(CurrentWeatherEntry.TABLE_NAME, null, weatherValues);
        assertTrue("Unable to Insert CurrentWeatherEntry into the Database", weatherRowId != -1);

        db.close();

        // Test the basic content provider query
        Cursor weatherCursor = mContext.getContentResolver().query(
                CurrentWeatherEntry.buildUri(TestUtilities.TEST_LOCATION),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicWeatherCurrentQuery", weatherCursor, weatherValues);
    }

    @Test
    public void testBasicLocationQueries() {
        // insert our test records into the database
        WeatherDbHelper dbHelper = new WeatherDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues locationValues = TestUtilities.createTestLocationValues();
        long locationRowId = TestUtilities.insertTestLocationValues(mContext);

        // Test the basic content provider query
        Cursor locationCursor = mContext.getContentResolver().query(
                LocationEntry.buildUri(TestUtilities.TEST_LOCATION),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicLocationQueries, location query", locationCursor, locationValues);

        // Has the NotificationUri been set correctly? --- we can only test this easily against API
        // level 19 or greater because getNotificationUri was added in API level 19.
        if (Build.VERSION.SDK_INT >= 19) {
            assertEquals("Error: LocationModel Query did not properly set NotificationUri",
                    locationCursor.getNotificationUri(),
                    LocationEntry.CONTENT_URI.buildUpon()
                            .appendPath(String.valueOf(TestUtilities.TEST_LOCATION)).build());
        }
    }

    /*
        This test uses the provider to insert and then update the data. Uncomment this test to
        see if your update location is functioning correctly.
     */
    @Test
    public void testUpdateLocation() {
        // Create a new map of values, where column names are the keys
        ContentValues values = TestUtilities.createTestLocationValues();

        Uri locationUri = mContext.getContentResolver().insert(
                LocationEntry.CONTENT_URI,
                values
        );
        long locationRowId = ContentUris.parseId(locationUri);

        // Verify we got a row back.
        assertTrue(locationRowId != -1);
        Log.d(LOG_TAG, "New row id: " + locationRowId);

        ContentValues updatedValues = new ContentValues(values);
        updatedValues.put(LocationEntry._ID, locationRowId);
        updatedValues.put(LocationEntry.COLUMN_CITY_NAME, "Santa's Village");

        // Create a cursor with observer to make sure that the content provider is notifying
        // the observers as expected
        Cursor locationCursor = mContext.getContentResolver().query(
                LocationEntry.buildUri(locationRowId),
                null,
                null,
                null,
                null
        );

        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        locationCursor.registerContentObserver(tco);

        int count = mContext.getContentResolver().update(
                LocationEntry.buildUri(locationRowId),
                updatedValues,
                LocationEntry._ID + "= ?",
                new String[]{Long.toString(locationRowId)});
        assertEquals(count, 1);

        // Test to make sure our observer is called.  If not, we throw an assertion.
        //

        tco.waitForNotificationOrFail();

        locationCursor.unregisterContentObserver(tco);
        locationCursor.close();

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                LocationEntry.buildUri(locationRowId),
                null,   // projection
                null,
                null,   // Values for the "where" clause
                null    // sort order
        );

        TestUtilities.validateCursor("testUpdateLocation.  Error validating location entry update.",
                cursor, updatedValues);

        cursor.close();
    }


    // Make sure we can still delete after adding/updating stuff
    @Test
    public void testInsertReadProvider() {
        ContentValues testValues = TestUtilities.createTestLocationValues();

        // Register a content observer for our insert.  This time, directly with the content resolver
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(LocationEntry.CONTENT_URI, true, tco);
        Uri locationUri = mContext.getContentResolver().insert(LocationEntry.CONTENT_URI, testValues);

        // Did our content observer get called?  Students:  If this fails, your insert location
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        long locationRowId = ContentUris.parseId(locationUri);

        // Verify we got a row back.
        assertTrue(locationRowId != -1);

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                LocationEntry.buildUri(TestUtilities.TEST_LOCATION),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating LocationEntry.",
                cursor, testValues);

        // Fantastic.  Now that we have a location, add some weather!
        ContentValues weatherValues = TestUtilities.createWeatherCurrentValues(locationRowId);
        // The TestContentObserver is a one-shot class
        tco = TestUtilities.getTestContentObserver();

        mContext.getContentResolver().registerContentObserver(CurrentWeatherEntry.CONTENT_URI, true, tco);

        Uri weatherInsertUri = mContext.getContentResolver()
                .insert(CurrentWeatherEntry.CONTENT_URI, weatherValues);
        assertTrue(weatherInsertUri != null);

        // Did our content observer get called? If this fails, your insert weather
        // in your ContentProvider isn't calling
        // getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        // A cursor is your primary interface to the query results.
        Cursor weatherCursor = mContext.getContentResolver().query(
                CurrentWeatherEntry.buildUri(TestUtilities.TEST_LOCATION),  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null // columns to group by
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating CurrentWeatherEntry insert.",
                weatherCursor, weatherValues);



    }

    // Make sure we can still delete after adding/updating stuff
    @Test
    public void testDeleteRecords() {
        testInsertReadProvider();

//        // Register a content observer for our location delete.
//        TestUtilities.TestContentObserver locationObserver = TestUtilities.getTestContentObserver();
//        mContext.getContentResolver().registerContentObserver(LocationEntry.CONTENT_URI, true, locationObserver);

        // Register a content observer for our weather_current delete.
        TestUtilities.TestContentObserver currentWeatherObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(CurrentWeatherEntry.CONTENT_URI, true, currentWeatherObserver);

        // Register a content observer for our weather_current delete.
        TestUtilities.TestContentObserver weatherByDayObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(WeatherByDayEntry.CONTENT_URI, true, weatherByDayObserver);

        // Register a content observer for our weather_current delete.
        TestUtilities.TestContentObserver weatherByHourObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(WeatherByHourEntry.CONTENT_URI, true, weatherByHourObserver);

        deleteAllRecordsFromProvider();

        currentWeatherObserver.waitForNotificationOrFail();
        weatherByDayObserver.waitForNotificationOrFail();
        weatherByHourObserver.waitForNotificationOrFail();

        mContext.getContentResolver().unregisterContentObserver(currentWeatherObserver);
        mContext.getContentResolver().unregisterContentObserver(weatherByDayObserver);
        mContext.getContentResolver().unregisterContentObserver(weatherByHourObserver);
    }

    static private final int BULK_INSERT_RECORDS_TO_INSERT = 10;

    static ContentValues[] createBulkInsertCurrentWeatherValues() {

        ContentValues[] returnContentValues = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];

        for (int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, TestUtilities.TEST_TIME += 1){
            returnContentValues[i] = TestUtilities.createWeatherCurrentValues(TestUtilities.TEST_LOCATION);
        }
        return returnContentValues;
    }

    @Test
    public void testBulkInsert() {
        // first, let's create a location value
        ContentValues testValues = TestUtilities.createTestLocationValues();
        Uri locationUri = mContext.getContentResolver().insert(LocationEntry.CONTENT_URI, testValues);
        long locationRowId = ContentUris.parseId(locationUri);

        // Verify we got a row back.
        assertTrue(locationRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                LocationEntry.buildUri(TestUtilities.TEST_LOCATION),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testBulkInsert. Error validating LocationEntry.",
                cursor, testValues);
        cursor.close();


        ContentValues[] bulkInsertContentValues = createBulkInsertCurrentWeatherValues();

        // Register a content observer for our bulk insert.
        TestUtilities.TestContentObserver weatherObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(CurrentWeatherEntry.CONTENT_URI, true, weatherObserver);

        int insertCount = mContext.getContentResolver().bulkInsert(CurrentWeatherEntry.CONTENT_URI, bulkInsertContentValues);

        // Students:  If this fails, it means that you most-likely are not calling the
        // getContext().getContentResolver().notifyChange(uri, null); in your BulkInsert
        // ContentProvider method.
        weatherObserver.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(weatherObserver);

        assertEquals(insertCount, BULK_INSERT_RECORDS_TO_INSERT);

        // A cursor is your primary interface to the query results.
        cursor = mContext.getContentResolver().query(
                CurrentWeatherEntry.buildUri(TestUtilities.TEST_LOCATION),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null
        );
        // we should have as many records in the database as we've inserted
        assertEquals(cursor.getCount(), BULK_INSERT_RECORDS_TO_INSERT);

        // and let's make sure they match the ones we created
        cursor.moveToFirst();
        for (int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, cursor.moveToNext()) {
            TestUtilities.validateCurrentRecord("testBulkInsert.  Error validating CurrentWeatherEntry " + i,
                    cursor, bulkInsertContentValues[i]);
        }
        cursor.close();
    }
}
