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
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.provider.Settings;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import ua.in.quireg.sunshine_mine.R;
import ua.in.quireg.sunshine_mine.data.WeatherContract.LocationEntry;
import ua.in.quireg.sunshine_mine.data.WeatherContract.WeatherEntry;
import ua.in.quireg.sunshine_mine.ui.ThisApplication;

/**
 * Manages a local database for weather data.
 */
public class WeatherDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "weather.db";

    private Context mContext;

    public WeatherDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_WEATHER_TABLE = "CREATE TABLE " + WeatherEntry.TABLE_NAME + " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                WeatherEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the location entry associated with this weather data
                WeatherEntry.COLUMN_LOC_KEY + " INTEGER NOT NULL, " +
                WeatherEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
                WeatherEntry.COLUMN_SHORT_DESC + " TEXT NOT NULL, " +
                WeatherEntry.COLUMN_WEATHER_ID + " INTEGER NOT NULL," +

                WeatherEntry.COLUMN_MIN_TEMP + " REAL NOT NULL, " +
                WeatherEntry.COLUMN_MAX_TEMP + " REAL NOT NULL, " +

                WeatherEntry.COLUMN_HUMIDITY + " REAL NOT NULL, " +
                WeatherEntry.COLUMN_PRESSURE + " REAL NOT NULL, " +
                WeatherEntry.COLUMN_WIND_SPEED + " REAL NOT NULL, " +
                WeatherEntry.COLUMN_DEGREES + " REAL NOT NULL, " +

                // Set up the location column as a foreign key to location table.
                " FOREIGN KEY (" + WeatherEntry.COLUMN_LOC_KEY + ") REFERENCES " +
                LocationEntry.TABLE_NAME + " (" + LocationEntry._ID + "), " +

                // To assure the application have just one weather entry per day
                // per location, it's created a UNIQUE constraint with REPLACE strategy
                " UNIQUE (" + WeatherEntry.COLUMN_DATE + ", " +
                WeatherEntry.COLUMN_LOC_KEY + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_LOCATION_TABLE = "CREATE TABLE "+ LocationEntry.TABLE_NAME + " (" +
                LocationEntry._ID + " INTEGER PRIMARY KEY," +
                LocationEntry.COLUMN_CITY_NAME + " TEXT NOT NULL, " +
                LocationEntry.COLUMN_CITY_LAT + " REAL NOT NULL, " +
                LocationEntry.COLUMN_CITY_LON + " REAL NOT NULL, " +
                LocationEntry.COLUMN_LOC_COUNTRYCODE + " TEXT NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_LOCATION_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_WEATHER_TABLE);
        try {
            importLocationData(sqLiteDatabase);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + LocationEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WeatherEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    private void importLocationData(SQLiteDatabase sqLiteDatabase) throws UnsupportedEncodingException {
        //InputStream in = ThisApplication.getAppContext().getResources().openRawResource(R.raw.city_list);
        InputStream in = mContext.getResources().openRawResource(R.raw.city_list);

        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String sCurrentLine;
        try {
            while ((sCurrentLine = br.readLine()) != null){
                String[] data = sCurrentLine.split("\t");
                sqLiteDatabase.beginTransaction();

                if(data.length != 5 ){
                    continue;
                }

                SQLiteStatement stmt = sqLiteDatabase.compileStatement("INSERT INTO " + LocationEntry.TABLE_NAME + "(" +
                        LocationEntry._ID + "," +
                        LocationEntry.COLUMN_CITY_NAME + "," +
                        LocationEntry.COLUMN_CITY_LAT + "," +
                        LocationEntry.COLUMN_CITY_LON + "," +
                        LocationEntry.COLUMN_LOC_COUNTRYCODE + ")" +
                        " VALUES (?,?,?,?,?);");
                stmt.bindLong(1, Long.parseLong(data[0]));
                stmt.bindString(2, data[1]);
                stmt.bindDouble(3, Double.parseDouble(data[2]));
                stmt.bindDouble(4, Double.parseDouble(data[3]));
                stmt.bindString(5, data[4]);

//                String sqlQuery = "INSERT INTO " + LocationEntry.TABLE_NAME + "(" +
//                        LocationEntry._ID + "," +
//                        LocationEntry.COLUMN_CITY_NAME + "," +
//                        LocationEntry.COLUMN_CITY_LAT + "," +
//                        LocationEntry.COLUMN_CITY_LON + "," +
//                        LocationEntry.COLUMN_LOC_COUNTRYCODE + ")" +
//                        " VALUES (" +
//                        data[0] + "," +
//                        data[1] + "," +
//                        data[2] + "," +
//                        data[3] + "," +
//                        data[4] + "," +
//                        ");";
//                Log.d("DB:", sqlQuery);
//                System.out.println(sqlQuery);
                stmt.execute();
                sqLiteDatabase.setTransactionSuccessful();
                sqLiteDatabase.endTransaction();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (sqLiteDatabase.inTransaction()) sqLiteDatabase.endTransaction();
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
