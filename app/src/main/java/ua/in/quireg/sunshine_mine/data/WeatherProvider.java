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


import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import ua.in.quireg.sunshine_mine.data.WeatherContract.LocationEntry;
import ua.in.quireg.sunshine_mine.data.WeatherContract.WeatherByDayEntry;
import ua.in.quireg.sunshine_mine.data.WeatherContract.WeatherByHourEntry;
import ua.in.quireg.sunshine_mine.data.WeatherContract.CurrentWeatherEntry;

public class WeatherProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private WeatherDbHelper mOpenHelper;

    public static final int WEATHER_BY_HOUR = 101;
    public static final int WEATHER_BY_HOUR_WITH_LOC = 102;

    public static final int WEATHER_BY_DAY = 201;
    public static final int WEATHER_BY_DAY_WITH_LOC = 202;

    public static final int WEATHER_CURRENT = 301;
    public static final int WEATHER_CURRENT_WITH_LOC = 302;

    public static final int LOCATION = 401;
    public static final int LOCATION_WITH_ID = 402;

    private static final SQLiteQueryBuilder sWeatherByHourQueryBuilder;
    private static final SQLiteQueryBuilder sWeatherByDayQueryBuilder;
    private static final SQLiteQueryBuilder sWeatherCurrentQueryBuilder;

    private static final long QUERY_DATE_THRESHOLD = System.currentTimeMillis() / 1000L - 1814400; //15 days ago


    static {
        sWeatherByHourQueryBuilder = new SQLiteQueryBuilder();
        sWeatherByDayQueryBuilder = new SQLiteQueryBuilder();
        sWeatherCurrentQueryBuilder = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        //weather INNER JOIN location ON weather.location_id = location._id
        sWeatherByHourQueryBuilder.setTables(
                WeatherByHourEntry.TABLE_NAME + " INNER JOIN " +
                        LocationEntry.TABLE_NAME +
                        " ON " + WeatherByHourEntry.TABLE_NAME +
                        "." + WeatherByHourEntry.COLUMN_LOC_KEY +
                        " = " + LocationEntry.TABLE_NAME +
                        "." + LocationEntry._ID);

        sWeatherByDayQueryBuilder.setTables(
                WeatherByDayEntry.TABLE_NAME + " INNER JOIN " +
                        LocationEntry.TABLE_NAME +
                        " ON " + WeatherByDayEntry.TABLE_NAME +
                        "." + WeatherByDayEntry.COLUMN_LOC_KEY +
                        " = " + LocationEntry.TABLE_NAME +
                        "." + LocationEntry._ID);

        sWeatherCurrentQueryBuilder.setTables(
                CurrentWeatherEntry.TABLE_NAME + " INNER JOIN " +
                        LocationEntry.TABLE_NAME +
                        " ON " + CurrentWeatherEntry.TABLE_NAME +
                        "." + CurrentWeatherEntry.COLUMN_LOC_KEY +
                        " = " + LocationEntry.TABLE_NAME +
                        "." + LocationEntry._ID);

    }

    //location.location_setting = ? AND date = ?
    private static final String sLocationSettingAndDaySelectionForHour =
            LocationEntry.TABLE_NAME +
                    "." + LocationEntry._ID + " = ? AND " +
                    WeatherByHourEntry.COLUMN_DATE + " >= ? ";

    private static final String sLocationSettingAndDaySelectionForDay =
            LocationEntry.TABLE_NAME +
                    "." + LocationEntry._ID + " = ? AND " +
                    WeatherByDayEntry.COLUMN_DATE + " >= ? ";

    private static final String sLocationSettingAndDaySelectionForCurrent =
            WeatherContract.LocationEntry.TABLE_NAME +
                    "." + WeatherContract.LocationEntry._ID + " = ? AND " +
                    CurrentWeatherEntry.COLUMN_DATE + " >= ? ";


    private Cursor getWeatherByHour(
            Uri uri, String[] projection, String sortOrder) {
        String locationSetting = WeatherContract.getLocationSettingFromUri(uri);

        return sWeatherByHourQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sLocationSettingAndDaySelectionForHour,
                new String[]{locationSetting, String.valueOf(QUERY_DATE_THRESHOLD)},
                null,
                null,
                CurrentWeatherEntry.COLUMN_DATE
        );
    }

    private Cursor getWeatherByDay(
            Uri uri, String[] projection, String sortOrder) {
        String locationSetting = WeatherContract.getLocationSettingFromUri(uri);

        return sWeatherByDayQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sLocationSettingAndDaySelectionForDay,
                new String[]{locationSetting, String.valueOf(QUERY_DATE_THRESHOLD)},
                null,
                null,
                WeatherByDayEntry.COLUMN_DATE
        );
    }

    private Cursor getWeatherCurrent(
            Uri uri, String[] projection, String sortOrder) {
        String locationSetting = WeatherContract.getLocationSettingFromUri(uri);

        return sWeatherCurrentQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sLocationSettingAndDaySelectionForCurrent,
                new String[]{locationSetting, String.valueOf(QUERY_DATE_THRESHOLD)},
                null,
                null,
                CurrentWeatherEntry.COLUMN_DATE
        );
    }

    static UriMatcher buildUriMatcher() {

        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found.  The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = WeatherContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, WeatherContract.PATH_WEATHER_BY_DAY, WEATHER_BY_DAY);
        matcher.addURI(authority, WeatherContract.PATH_WEATHER_BY_DAY + "/#", WEATHER_BY_DAY_WITH_LOC);

        matcher.addURI(authority, WeatherContract.PATH_WEATHER_BY_HOUR, WEATHER_BY_HOUR);
        matcher.addURI(authority, WeatherContract.PATH_WEATHER_BY_HOUR + "/#", WEATHER_BY_HOUR_WITH_LOC);

        matcher.addURI(authority, WeatherContract.PATH_WEATHER_CURRENT, WEATHER_CURRENT);
        matcher.addURI(authority, WeatherContract.PATH_WEATHER_CURRENT + "/#", WEATHER_CURRENT_WITH_LOC);

        matcher.addURI(authority, WeatherContract.PATH_LOCATION, LOCATION);
        matcher.addURI(authority, WeatherContract.PATH_LOCATION + "/#", LOCATION_WITH_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new WeatherDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case WEATHER_BY_HOUR:
            case WEATHER_BY_HOUR_WITH_LOC:
                return WeatherByHourEntry.CONTENT_TYPE;

            case WEATHER_BY_DAY:
            case WEATHER_BY_DAY_WITH_LOC:
                return WeatherByDayEntry.CONTENT_TYPE;

            case WEATHER_CURRENT:
            case WEATHER_CURRENT_WITH_LOC:
                return CurrentWeatherEntry.CONTENT_TYPE;

            case LOCATION:
            case LOCATION_WITH_ID:
                return LocationEntry.CONTENT_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case WEATHER_BY_HOUR:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        WeatherByHourEntry.TABLE_NAME,
                        projection,
                        null,
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            case WEATHER_BY_DAY:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        WeatherByDayEntry.TABLE_NAME,
                        projection,
                        null,
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            case WEATHER_CURRENT:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        CurrentWeatherEntry.TABLE_NAME,
                        projection,
                        null,
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            case LOCATION:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        LocationEntry.TABLE_NAME,
                        projection,
                        null,
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            case WEATHER_BY_HOUR_WITH_LOC:
                retCursor = getWeatherByHour(uri, projection, sortOrder);
                break;

            case WEATHER_BY_DAY_WITH_LOC:
                retCursor = getWeatherByDay(uri, projection, sortOrder);
                break;

            case WEATHER_CURRENT_WITH_LOC:
                retCursor = getWeatherCurrent(uri, projection, sortOrder);
                break;

            case LOCATION_WITH_ID:
                String locationSetting = WeatherContract.getLocationSettingFromUri(uri);
                retCursor = mOpenHelper.getReadableDatabase().query(
                        LocationEntry.TABLE_NAME,
                        projection,
                        LocationEntry._ID + "=?",
                        new String[]{locationSetting},
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        long _id;

        switch (match) {
            case WEATHER_BY_DAY: {
                _id = db.insert(WeatherByDayEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = WeatherByDayEntry.buildUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            case WEATHER_BY_HOUR: {
                _id = db.insert(WeatherByHourEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = WeatherByHourEntry.buildUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            case WEATHER_CURRENT: {
                _id = db.insert(CurrentWeatherEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = CurrentWeatherEntry.buildUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            case LOCATION: {
                _id = db.insert(LocationEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = LocationEntry.buildUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Location id required");
        }
        db.close();
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted

        String location = WeatherContract.getLocationSettingFromUri(uri);

        if (null == selection) selection = "1";
        switch (match) {

            case WEATHER_BY_HOUR:
                rowsDeleted = db.delete(
                        WeatherByHourEntry.TABLE_NAME,
                        null,
                        null);
                break;
            case WEATHER_BY_DAY:
                rowsDeleted = db.delete(
                        WeatherByDayEntry.TABLE_NAME,
                        null,
                        null);
                break;
            case WEATHER_CURRENT:
                rowsDeleted = db.delete(
                        CurrentWeatherEntry.TABLE_NAME,
                        null,
                        null);
                break;
            case LOCATION:
                throw new UnsupportedOperationException("Location wildcard delete is not required thus unsupported.");

            case WEATHER_BY_DAY_WITH_LOC:
                rowsDeleted = db.delete(
                        WeatherByDayEntry.TABLE_NAME,
                        WeatherByDayEntry.COLUMN_LOC_KEY + "=?",
                        new String[]{location});
                break;

            case WEATHER_BY_HOUR_WITH_LOC:
                rowsDeleted = db.delete(
                        WeatherByHourEntry.TABLE_NAME,
                        WeatherByHourEntry.COLUMN_LOC_KEY + "=?",
                        new String[]{location});
                break;

            case WEATHER_CURRENT_WITH_LOC:
                rowsDeleted = db.delete(
                        CurrentWeatherEntry.TABLE_NAME,
                        CurrentWeatherEntry.COLUMN_LOC_KEY + "=?",
                        new String[]{location});
                break;

            case LOCATION_WITH_ID:
                rowsDeleted = db.delete(
                        LocationEntry.TABLE_NAME,
                        LocationEntry._ID + "=?",
                        new String[]{location});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        db.close();
        // Because a null deletes all rows
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }


    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        String location = WeatherContract.getLocationSettingFromUri(uri);

        switch (match) {
            case LOCATION_WITH_ID:
                rowsUpdated = db.update(LocationEntry.TABLE_NAME,
                        values,
                        LocationEntry._ID + "=?",
                        new String[]{location});
                break;
            default:
                throw new UnsupportedOperationException("Only location updates with location ID supported");
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        db.close();
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        db.beginTransaction();
        int returnCount = 0;
        try {
            for (ContentValues value : values) {
                long _id = 0;
                switch (match) {
                    case LOCATION:
                        _id = db.insert(LocationEntry.TABLE_NAME, null, value);
                        break;

                    case WEATHER_BY_DAY:
                        _id = db.insert(WeatherByDayEntry.TABLE_NAME, null, value);
                        break;

                    case WEATHER_BY_HOUR:
                        _id = db.insert(WeatherByHourEntry.TABLE_NAME, null, value);
                        break;

                    case WEATHER_CURRENT:
                        _id = db.insert(CurrentWeatherEntry.TABLE_NAME, null, value);
                        break;
                    default:
                        return super.bulkInsert(uri, values);
                }
                if (_id != -1) {
                    returnCount++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        db.close();
        return returnCount;

    }

    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}