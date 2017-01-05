
package ua.in.quireg.sunshine_mine.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ua.in.quireg.sunshine_mine.R;
import ua.in.quireg.sunshine_mine.data.WeatherContract.LocationEntry;
import ua.in.quireg.sunshine_mine.data.WeatherContract.WeatherByDayEntry;
import ua.in.quireg.sunshine_mine.data.WeatherContract.WeatherByHourEntry;
import ua.in.quireg.sunshine_mine.data.WeatherContract.CurrentWeatherEntry;

/**
 * Manages a local database for weather data.
 */
public class WeatherDbHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = WeatherDbHelper.class.getSimpleName();

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 12;

    static final String DATABASE_NAME = "weather.db";

    private Context mContext;

    public WeatherDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WeatherByDayEntry.TABLE_NAME);

        final String SQL_CREATE_WEATHER_BY_DAY_TABLE = "CREATE TABLE " + WeatherByDayEntry.TABLE_NAME + " (" +

                WeatherByDayEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                WeatherByDayEntry.COLUMN_LOC_KEY + " INTEGER NOT NULL, " +
                WeatherByDayEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
                WeatherByDayEntry.COLUMN_WEATHER_ID + " INTEGER NOT NULL," +
                WeatherByDayEntry.COLUMN_WEATHER_MAIN + " TEXT NOT NULL," +
                WeatherByDayEntry.COLUMN_WEATHER_DESC + " TEXT NOT NULL," +
                WeatherByDayEntry.COLUMN_WEATHER_ICON + " TEXT NOT NULL," +

                WeatherByDayEntry.COLUMN_MIN_TEMP + " REAL NOT NULL, " +
                WeatherByDayEntry.COLUMN_MAX_TEMP + " REAL NOT NULL, " +
                WeatherByDayEntry.COLUMN_DAY_TEMP + " REAL NOT NULL, " +
                WeatherByDayEntry.COLUMN_NIGHT_TEMP + " REAL NOT NULL, " +
                WeatherByDayEntry.COLUMN_EVE_TEMP + " REAL NOT NULL, " +
                WeatherByDayEntry.COLUMN_MORN_TEMP + " REAL NOT NULL, " +

                WeatherByDayEntry.COLUMN_CLOUDS + " REAL NOT NULL, " +
                WeatherByDayEntry.COLUMN_HUMIDITY + " REAL NOT NULL, " +
                WeatherByDayEntry.COLUMN_PRESSURE + " REAL NOT NULL, " +
                WeatherByDayEntry.COLUMN_WIND_SPEED + " REAL NOT NULL, " +
                WeatherByDayEntry.COLUMN_WIND_DEG + " REAL NOT NULL, " +

                // Set up the location column as a foreign key to location table.
                " FOREIGN KEY (" + WeatherByDayEntry.COLUMN_LOC_KEY + ") REFERENCES " +
                LocationEntry.TABLE_NAME + " (" + LocationEntry._ID + "), " +

                // To assure the application have just one weather entry per day
                // per location, it's created a UNIQUE constraint with REPLACE strategy
                " UNIQUE (" + WeatherByDayEntry.COLUMN_DATE + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_WEATHER_BY_HOUR_TABLE = "CREATE TABLE " + WeatherByHourEntry.TABLE_NAME + " (" +

                WeatherByHourEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                WeatherByHourEntry.COLUMN_LOC_KEY + " INTEGER NOT NULL, " +
                WeatherByHourEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
                WeatherByHourEntry.COLUMN_WEATHER_ID + " INTEGER NOT NULL," +
                WeatherByHourEntry.COLUMN_WEATHER_MAIN + " TEXT NOT NULL," +
                WeatherByHourEntry.COLUMN_WEATHER_DESC + " TEXT NOT NULL," +
                WeatherByHourEntry.COLUMN_WEATHER_ICON + " TEXT NOT NULL," +

                WeatherByHourEntry.COLUMN_MIN_TEMP + " REAL NOT NULL, " +
                WeatherByHourEntry.COLUMN_MAX_TEMP + " REAL NOT NULL, " +
                WeatherByHourEntry.COLUMN_AVG_TEMP + " REAL NOT NULL, " +

                WeatherByHourEntry.COLUMN_CLOUDS + " REAL NOT NULL, " +
                WeatherByHourEntry.COLUMN_HUMIDITY + " REAL NOT NULL, " +
                WeatherByHourEntry.COLUMN_PRESSURE + " REAL NOT NULL, " +
                WeatherByHourEntry.COLUMN_PRESSURE_SEA_LEVEL + " REAL NOT NULL, " +
                WeatherByHourEntry.COLUMN_PRESSURE_GRND_LEVEL + " REAL NOT NULL, " +
                WeatherByHourEntry.COLUMN_WIND_SPEED + " REAL NOT NULL, " +
                WeatherByHourEntry.COLUMN_WIND_DEG + " REAL NOT NULL, " +

                WeatherByHourEntry.COLUMN_CALCULATION_TIME + " INTEGER NOT NULL, " +


                // Set up the location column as a foreign key to location table.
                " FOREIGN KEY (" + WeatherByDayEntry.COLUMN_LOC_KEY + ") REFERENCES " +
                LocationEntry.TABLE_NAME + " (" + LocationEntry._ID + "), " +

                // To assure the application have just one weather entry per day
                // per location, it's created a UNIQUE constraint with REPLACE strategy
                " UNIQUE (" + WeatherByDayEntry.COLUMN_DATE + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_CURRENT_WEATHER_TABLE = "CREATE TABLE " + CurrentWeatherEntry.TABLE_NAME + " (" +

                CurrentWeatherEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                CurrentWeatherEntry.COLUMN_LOC_KEY + " INTEGER NOT NULL, " +
                CurrentWeatherEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
                CurrentWeatherEntry.COLUMN_WEATHER_ID + " INTEGER NOT NULL," +
                CurrentWeatherEntry.COLUMN_WEATHER_MAIN + " TEXT NOT NULL," +
                CurrentWeatherEntry.COLUMN_WEATHER_DESC + " TEXT NOT NULL," +
                CurrentWeatherEntry.COLUMN_WEATHER_ICON + " TEXT NOT NULL," +

                CurrentWeatherEntry.COLUMN_MIN_TEMP + " REAL NOT NULL, " +
                CurrentWeatherEntry.COLUMN_MAX_TEMP + " REAL NOT NULL, " +
                CurrentWeatherEntry.COLUMN_AVG_TEMP + " REAL NOT NULL, " +

                CurrentWeatherEntry.COLUMN_CLOUDS + " REAL NOT NULL, " +
                CurrentWeatherEntry.COLUMN_HUMIDITY + " REAL NOT NULL, " +
                CurrentWeatherEntry.COLUMN_PRESSURE + " REAL NOT NULL, " +
                CurrentWeatherEntry.COLUMN_PRESSURE_SEA_LEVEL + " REAL NOT NULL, " +
                CurrentWeatherEntry.COLUMN_PRESSURE_GRND_LEVEL + " REAL NOT NULL, " +
                CurrentWeatherEntry.COLUMN_WIND_SPEED + " REAL NOT NULL, " +
                CurrentWeatherEntry.COLUMN_WIND_DEG + " REAL NOT NULL, " +

                CurrentWeatherEntry.COLUMN_CALCULATION_TIME + " INTEGER NOT NULL, " +

                CurrentWeatherEntry.COLUMN_SUNRISE + " INTEGER NOT NULL, " +
                CurrentWeatherEntry.COLUMN_SUNSET + " INTEGER NOT NULL, " +


                // Set up the location column as a foreign key to location table.
                " FOREIGN KEY (" + CurrentWeatherEntry.COLUMN_LOC_KEY + ") REFERENCES " +
                LocationEntry.TABLE_NAME + " (" + LocationEntry._ID + "), " +

                // To assure the application have just one weather entry per day
                // per location, it's created a UNIQUE constraint with REPLACE strategy
                " UNIQUE (" + CurrentWeatherEntry.COLUMN_DATE + ") ON CONFLICT REPLACE);";


        sqLiteDatabase.execSQL(SQL_CREATE_WEATHER_BY_DAY_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_WEATHER_BY_HOUR_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_CURRENT_WEATHER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        onCreate(sqLiteDatabase);
    }

    public static void importDatabase(Context mContext) {
        File dbPath1 = mContext.getDatabasePath(DATABASE_NAME);
        String dbPathString = dbPath1.getAbsolutePath();
        File dbFile = new File(dbPathString);
        InputStream in = mContext.getResources().openRawResource(R.raw.weather);
        OutputStream out = null;
        try {
            //Only in case there is no db file new one will be created from asset.
            if (!dbFile.exists()) {
                //Sometimes parent folder is missing for database path so we create it.
                //Result is ignored because it will return false in case path is present and
                //true in case it was just created.
                dbFile.getParentFile().mkdirs();
                if (dbFile.createNewFile()) {
                    {
                        out = new FileOutputStream(dbFile);
                        int read = 0;
                        byte[] bytes = new byte[1024];

                        while ((read = in.read(bytes)) != -1) {
                            out.write(bytes, 0, read);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}