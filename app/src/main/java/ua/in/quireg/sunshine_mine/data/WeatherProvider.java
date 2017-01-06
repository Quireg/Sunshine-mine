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

    public static final int WEATHER_BY_HOUR = 201;
    public static final int WEATHER_BY_DAY = 202;
    public static final int WEATHER_CURRENT = 203;
    public static final int LOCATION = 300;

    private static final SQLiteQueryBuilder sWeatherByHourQueryBuilder;
    private static final SQLiteQueryBuilder sWeatherByDayQueryBuilder;
    private static final SQLiteQueryBuilder sWeatherCurrentQueryBuilder;

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
                    WeatherByHourEntry.COLUMN_DATE + " = ? ";

    private static final String sLocationSettingAndDaySelectionForDay =
            LocationEntry.TABLE_NAME +
                    "." + LocationEntry._ID + " = ? AND " +
                    WeatherByDayEntry.COLUMN_DATE + " = ? ";

    private static final String sLocationSettingAndDaySelectionForCurrent =
            WeatherContract.LocationEntry.TABLE_NAME +
                    "." + WeatherContract.LocationEntry._ID + " = ? AND " +
                    CurrentWeatherEntry.COLUMN_DATE + " = ? ";


    private Cursor getWeatherByHour(
            Uri uri, String[] projection, String sortOrder) {
        String locationSetting = WeatherContract.getLocationSettingFromUri(uri);
        long date = WeatherContract.getDateFromUri(uri);

        return sWeatherByHourQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sLocationSettingAndDaySelectionForHour,
                new String[]{locationSetting, Long.toString(date)},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getWeatherByDay(
            Uri uri, String[] projection, String sortOrder) {
        String locationSetting = WeatherContract.getLocationSettingFromUri(uri);
        long date = WeatherContract.getDateFromUri(uri);

        return sWeatherByDayQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sLocationSettingAndDaySelectionForDay,
                new String[]{locationSetting, Long.toString(date)},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getWeatherCurrent(
            Uri uri, String[] projection, String sortOrder) {
        String locationSetting = WeatherContract.getLocationSettingFromUri(uri);
        long date = WeatherContract.getDateFromUri(uri);

        return sWeatherCurrentQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sLocationSettingAndDaySelectionForCurrent,
                new String[]{locationSetting, Long.toString(date)},
                null,
                null,
                sortOrder
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
        matcher.addURI(authority, WeatherContract.PATH_WEATHER_BY_HOUR, WEATHER_BY_HOUR);
        matcher.addURI(authority, WeatherContract.PATH_WEATHER_CURRENT, WEATHER_CURRENT);

        matcher.addURI(authority, WeatherContract.PATH_LOCATION, LOCATION);
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
                return WeatherByHourEntry.CONTENT_ITEM_TYPE;
            case WEATHER_BY_DAY:
                return WeatherByDayEntry.CONTENT_TYPE;
            case WEATHER_CURRENT:
                return CurrentWeatherEntry.CONTENT_TYPE;
            case LOCATION:
                return WeatherContract.LocationEntry.CONTENT_TYPE;
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
            case WEATHER_BY_HOUR: {
                retCursor = getWeatherByHour(uri, projection, sortOrder);
                break;
            }
            case WEATHER_BY_DAY: {
                retCursor = getWeatherByDay(uri, projection, sortOrder);
                break;
            }
            case WEATHER_CURRENT: {
                retCursor = getWeatherCurrent(uri, projection, sortOrder);

                break;
            }
            case LOCATION: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        WeatherContract.LocationEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

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

        switch (match) {
            case WEATHER_BY_DAY: {
                long _id = db.insert(WeatherByDayEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = WeatherByDayEntry.buildWeatherUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case WEATHER_BY_HOUR: {
                long _id = db.insert(WeatherByHourEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = WeatherByHourEntry.buildWeatherUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case WEATHER_CURRENT: {
                long _id = db.insert(CurrentWeatherEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = CurrentWeatherEntry.buildWeatherUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case LOCATION: {
                long _id = db.insert(WeatherContract.LocationEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = WeatherContract.LocationEntry.buildLocationUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if (null == selection) selection = "1";
        switch (match) {
            case WEATHER_BY_DAY:
                rowsDeleted = db.delete(
                        WeatherByDayEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case WEATHER_BY_HOUR:
                rowsDeleted = db.delete(
                        WeatherByHourEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case WEATHER_CURRENT:
                rowsDeleted = db.delete(
                        CurrentWeatherEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case LOCATION:
                rowsDeleted = db.delete(
                        LocationEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }


    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case WEATHER_BY_DAY:
                rowsUpdated = db.update(WeatherByDayEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case WEATHER_BY_HOUR:
                rowsUpdated = db.update(WeatherByHourEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case WEATHER_CURRENT:
                rowsUpdated = db.update(CurrentWeatherEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case LOCATION:
                rowsUpdated = db.update(WeatherContract.LocationEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
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
                switch (match){
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