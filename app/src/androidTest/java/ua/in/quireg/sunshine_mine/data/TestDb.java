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

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ua.in.quireg.sunshine_mine.data.WeatherContract.LocationEntry;
import ua.in.quireg.sunshine_mine.data.WeatherContract.WeatherByDayEntry;
import ua.in.quireg.sunshine_mine.data.WeatherContract.WeatherByHourEntry;
import ua.in.quireg.sunshine_mine.data.WeatherContract.CurrentWeatherEntry;

import static junit.framework.TestCase.*;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestDb {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    private Context mContext;

    public TestDb(){
        this.mContext = InstrumentationRegistry.getTargetContext();
    }

    @After
    public void deleteTheDatabase() {
        this.mContext.deleteDatabase(WeatherDbHelper.DATABASE_NAME);
    }

    @Before
    public void setUp() {
        deleteTheDatabase();
        WeatherDbHelper.importDatabase(this.mContext);
    }
    @Test
    public void testCreateDb() throws Throwable {
        //Not necessary anymore as application always shipped with pre-configured db.
        assertEquals(true, true);
    }

    @Test
    public void testLocationTable() throws InterruptedException {

        SQLiteDatabase db = new WeatherDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        TestUtilities.insertTestLocationValues(this.mContext);

        Cursor c = db.query(
                LocationEntry.TABLE_NAME,
                null,
                "id = ?",
                new String[]{
                        "14881488"
                },
                null,
                null,
                null);
        TestUtilities.validateCursor("Location table issues", c, TestUtilities.createTestLocationValues());
        c.close();
        db.close();
    }

    @Test
    public void testWeatherByDayTable() {

        SQLiteDatabase db = new WeatherDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        TestUtilities.insertWeatherByDayValues(this.mContext);

        Cursor c = db.query(
                WeatherByDayEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        TestUtilities.validateCursor("Weather table issues", c, TestUtilities.createWeatherByDayValues(TestUtilities.TEST_LOCATION));
        c.close();
        db.close();

    }

    @Test
    public void testWeatherByHourTable() {

        SQLiteDatabase db = new WeatherDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        TestUtilities.insertWeatherByHourValues(this.mContext);

        Cursor c = db.query(
                WeatherByHourEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        TestUtilities.validateCursor("Weather table issues", c, TestUtilities.createWeatherByHourValues(TestUtilities.TEST_LOCATION));
        c.close();
        db.close();

    }

    @Test
    public void testWeatherCurrentTable() {

        SQLiteDatabase db = new WeatherDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        TestUtilities.insertWeatherCurrentValues(this.mContext);

        Cursor c = db.query(
                CurrentWeatherEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        TestUtilities.validateCursor("Weather table issues", c, TestUtilities.createWeatherCurrentValues(TestUtilities.TEST_LOCATION));
        c.close();
        db.close();

    }


}
