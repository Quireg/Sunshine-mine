package ua.in.quireg.sunshine_mine.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import ua.in.quireg.sunshine_mine.utils.PollingCheck;

import java.util.Map;
import java.util.Set;

import ua.in.quireg.sunshine_mine.data.WeatherContract.LocationEntry;
import ua.in.quireg.sunshine_mine.data.WeatherContract.WeatherByDayEntry;
import ua.in.quireg.sunshine_mine.data.WeatherContract.WeatherByHourEntry;
import ua.in.quireg.sunshine_mine.data.WeatherContract.CurrentWeatherEntry;

public class TestUtilities extends AndroidTestCase {
    static final long TEST_DATE = 1419033600L;  // December 20th, 2014
    static final long TEST_LOCATION = 14881488;

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    static ContentValues createWeatherByHourValues(long location_Id) {
        ContentValues hourValues = new ContentValues();
        hourValues.put(WeatherByHourEntry.COLUMN_LOC_KEY, location_Id);
        hourValues.put(WeatherByHourEntry.COLUMN_DATE, TEST_DATE);
        hourValues.put(WeatherByHourEntry.COLUMN_AVG_TEMP, "298.77");
        hourValues.put(WeatherByHourEntry.COLUMN_MAX_TEMP, "298.78");
        hourValues.put(WeatherByHourEntry.COLUMN_MIN_TEMP, "298.76");
        hourValues.put(WeatherByHourEntry.COLUMN_HUMIDITY, "87");
        hourValues.put(WeatherByHourEntry.COLUMN_PRESSURE, "1005.93");
        hourValues.put(WeatherByHourEntry.COLUMN_PRESSURE_GRND_LEVEL, "1230.23");
        hourValues.put(WeatherByHourEntry.COLUMN_PRESSURE_SEA_LEVEL, "1234.31");
        hourValues.put(WeatherByHourEntry.COLUMN_RAIN, "23");
        hourValues.put(WeatherByHourEntry.COLUMN_SNOW, "123");
        hourValues.put(WeatherByHourEntry.COLUMN_CLOUDS, "88");
        hourValues.put(WeatherByHourEntry.COLUMN_WIND_SPEED, "12");
        hourValues.put(WeatherByHourEntry.COLUMN_WIND_DEG, "44");
        hourValues.put(WeatherByHourEntry.COLUMN_WEATHER_DESC, "blah");
        hourValues.put(WeatherByHourEntry.COLUMN_WEATHER_ICON, "123d");
        hourValues.put(WeatherByHourEntry.COLUMN_WEATHER_ID, "123222");
        hourValues.put(WeatherByHourEntry.COLUMN_WEATHER_MAIN, "Clouds");
        hourValues.put(WeatherByHourEntry.COLUMN_CALCULATION_TIME, TEST_DATE);

        return hourValues;
    }

    static ContentValues createWeatherByDayValues(long location_Id) {
        ContentValues dailyValues = new ContentValues();
        dailyValues.put(WeatherByDayEntry.COLUMN_LOC_KEY, location_Id);
        dailyValues.put(WeatherByDayEntry.COLUMN_DATE, TEST_DATE);
        dailyValues.put(WeatherByDayEntry.COLUMN_MAX_TEMP, "232");
        dailyValues.put(WeatherByDayEntry.COLUMN_MIN_TEMP, "213");
        dailyValues.put(WeatherByDayEntry.COLUMN_EVE_TEMP, "234");
        dailyValues.put(WeatherByDayEntry.COLUMN_MORN_TEMP, "230");
        dailyValues.put(WeatherByDayEntry.COLUMN_DAY_TEMP, "210");
        dailyValues.put(WeatherByDayEntry.COLUMN_NIGHT_TEMP, "236");
        dailyValues.put(WeatherByDayEntry.COLUMN_CLOUDS, "56");
        dailyValues.put(WeatherByDayEntry.COLUMN_HUMIDITY, "43");
        dailyValues.put(WeatherByDayEntry.COLUMN_PRESSURE, "1021.2");
        dailyValues.put(WeatherByDayEntry.COLUMN_WIND_SPEED, "12.2");
        dailyValues.put(WeatherByDayEntry.COLUMN_WIND_DEG, "22.1");
        dailyValues.put(WeatherByDayEntry.COLUMN_WEATHER_DESC, "Asteroids");
        dailyValues.put(WeatherByDayEntry.COLUMN_WEATHER_ICON, "9d1");
        dailyValues.put(WeatherByDayEntry.COLUMN_WEATHER_ID, "123000");
        dailyValues.put(WeatherByDayEntry.COLUMN_WEATHER_MAIN, "Some text");

        return dailyValues;
    }

    static ContentValues createWeatherCurrentValues(long location_Id) {
        ContentValues currentValues = new ContentValues();
        currentValues.put(CurrentWeatherEntry.COLUMN_LOC_KEY, location_Id);
        currentValues.put(CurrentWeatherEntry.COLUMN_DATE, TEST_DATE);
        currentValues.put(CurrentWeatherEntry.COLUMN_AVG_TEMP, "21");
        currentValues.put(CurrentWeatherEntry.COLUMN_MAX_TEMP, "12");
        currentValues.put(CurrentWeatherEntry.COLUMN_MIN_TEMP, "6");
        currentValues.put(CurrentWeatherEntry.COLUMN_HUMIDITY, "12");
        currentValues.put(CurrentWeatherEntry.COLUMN_PRESSURE, "12");
        currentValues.put(CurrentWeatherEntry.COLUMN_PRESSURE_GRND_LEVEL, "13");
        currentValues.put(CurrentWeatherEntry.COLUMN_PRESSURE_SEA_LEVEL, "13.3");
        currentValues.put(CurrentWeatherEntry.COLUMN_CLOUDS, "22");
        currentValues.put(CurrentWeatherEntry.COLUMN_RAIN, "123");
        currentValues.put(CurrentWeatherEntry.COLUMN_SNOW, "213");
        currentValues.put(CurrentWeatherEntry.COLUMN_WIND_SPEED, "199");
        currentValues.put(CurrentWeatherEntry.COLUMN_WIND_DEG, "25");
        currentValues.put(CurrentWeatherEntry.COLUMN_WEATHER_DESC, "Nasty");
        currentValues.put(CurrentWeatherEntry.COLUMN_WEATHER_ICON, "icon");
        currentValues.put(CurrentWeatherEntry.COLUMN_WEATHER_ID, "32321");
        currentValues.put(CurrentWeatherEntry.COLUMN_WEATHER_MAIN, "Some text#2");
        currentValues.put(CurrentWeatherEntry.COLUMN_SUNRISE, "8741298712");
        currentValues.put(CurrentWeatherEntry.COLUMN_SUNSET, "18273982938");
        return currentValues;
    }

    static ContentValues createTestLocationValues() {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(WeatherContract.LocationEntry._ID, TEST_LOCATION);
        testValues.put(WeatherContract.LocationEntry.COLUMN_CITY_NAME, "Myhosransk");
        testValues.put(WeatherContract.LocationEntry.COLUMN_CITY_LAT, "14.88");
        testValues.put(WeatherContract.LocationEntry.COLUMN_CITY_LON, "2.28");
        testValues.put(WeatherContract.LocationEntry.COLUMN_LOC_COUNTRYCODE, "WUT");
        return testValues;
    }

    static long insertTestLocationValues(Context context) {
        // insert our test records into the database
        WeatherDbHelper dbHelper = new WeatherDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM location WHERE id=14881488");
        ContentValues testValues = TestUtilities.createTestLocationValues();

        long locationRowId;
        locationRowId = db.insert(LocationEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert Test Location Values", locationRowId != -1);

        return locationRowId;
    }

    static long insertWeatherCurrentValues(Context context) {
        insertTestLocationValues(context);
        WeatherDbHelper dbHelper = new WeatherDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createWeatherCurrentValues(TEST_LOCATION);

        long rowId;
        rowId = db.insert(CurrentWeatherEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert Current Weather Values", rowId != -1);

        return rowId;
    }

    static long insertWeatherByHourValues(Context context) {
        insertTestLocationValues(context);
        WeatherDbHelper dbHelper = new WeatherDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createWeatherByHourValues(TEST_LOCATION); //Novosibirsk location ID

        long rowId;
        rowId = db.insert(WeatherByHourEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert WeatherByHourEntry Values", rowId != -1);

        return rowId;
    }

    static long insertWeatherByDayValues(Context context) {
        insertTestLocationValues(context);
        WeatherDbHelper dbHelper = new WeatherDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createWeatherByDayValues(TEST_LOCATION);

        long rowId;
        rowId = db.insert(WeatherByDayEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert WeatherByDayEntry Values", rowId != -1);

        return rowId;
    }

    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}
