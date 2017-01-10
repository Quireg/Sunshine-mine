package ua.in.quireg.sunshine_mine.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Defines table and column names for the weather database.
 */
public class WeatherContract {

    public static final String CONTENT_AUTHORITY = "ua.in.quireg.sunshine_mine.app";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_WEATHER_BY_DAY = "weather_by_day";
    public static final String PATH_WEATHER_BY_HOUR = "weather_by_hour";
    public static final String PATH_WEATHER_CURRENT = "weather_current";
    public static final String PATH_LOCATION = "location";

    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.
    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    public static String getLocationSettingFromUri(Uri uri) {
        if(uri.getPathSegments().size() >= 2){
            return uri.getPathSegments().get(1);
        }
        return "0";
    }

    public static long getDateFromUri(Uri uri) {
        if(uri.getPathSegments().size() >= 3){
            return Long.parseLong(uri.getPathSegments().get(2));
        }
        return 0;
    }

    //Inner class that defines the table contents of the location table
    public static final class LocationEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_LOCATION).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LOCATION;

        public static final String _ID = "id";
        public static final String TABLE_NAME = "location";
        public static final String COLUMN_CITY_NAME = "city_name";
        public static final String COLUMN_CITY_LAT = "lat";
        public static final String COLUMN_CITY_LON = "lon";
        public static final String COLUMN_LOC_COUNTRYCODE = "countryCode";

        public static Uri buildUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }


    public static final class WeatherByDayEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_WEATHER_BY_DAY).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WEATHER_BY_DAY;


        public static final String TABLE_NAME = "weather_by_day";

        // Column with the foreign key into the location table.
        public static final String COLUMN_LOC_KEY = "location_id";
        // Date, stored as long in milliseconds since the epoch
        public static final String COLUMN_DATE = "timestamp";
        // Weather id as returned by API, to identify the icon to be used
        public static final String COLUMN_WEATHER_ID = "weather_id";
        //Group of weather parameters (Rain, Snow, Extreme etc.)
        public static final String COLUMN_WEATHER_MAIN = "weather_main";
        //Weather condition within the group
        public static final String COLUMN_WEATHER_DESC = "weather_description";
        //Weather icon id
        public static final String COLUMN_WEATHER_ICON = "weather_icon";
        //Wind speed. Unit Default: meter/sec, Metric: meter/sec, Imperial: miles/hour.
        public static final String COLUMN_WIND_SPEED = "wind_speed";
        //Wind direction, degrees (meteorological)
        public static final String COLUMN_WIND_DEG = "wind_deg";
        //Cloudiness, %
        public static final String COLUMN_CLOUDS = "clouds";

        // Temperatures for the day (stored as floats)
        public static final String COLUMN_MIN_TEMP = "min";
        public static final String COLUMN_MAX_TEMP = "max";
        public static final String COLUMN_DAY_TEMP = "day";
        public static final String COLUMN_NIGHT_TEMP = "night";
        public static final String COLUMN_EVE_TEMP = "eve";
        public static final String COLUMN_MORN_TEMP = "morn";

        // Humidity is stored as a float representing percentage
        public static final String COLUMN_HUMIDITY = "humidity";

        // Humidity is stored as a float representing percentage
        public static final String COLUMN_PRESSURE = "pressure";

        public static Uri buildUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class WeatherByHourEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_WEATHER_BY_HOUR).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WEATHER_BY_HOUR;


        public static final String TABLE_NAME = "weather_by_hour";

        // Column with the foreign key into the location table.
        public static final String COLUMN_LOC_KEY = "location_id";
        // Date, stored as long in milliseconds since the epoch
        public static final String COLUMN_DATE = "timestamp";
        // Weather id as returned by API, to identify the icon to be used
        public static final String COLUMN_WEATHER_ID = "weather_id";
        //Group of weather parameters (Rain, Snow, Extreme etc.)
        public static final String COLUMN_WEATHER_MAIN = "weather_main";
        //Weather condition within the group
        public static final String COLUMN_WEATHER_DESC = "weather_description";
        //Weather icon id
        public static final String COLUMN_WEATHER_ICON = "weather_icon";
        //Wind speed. Unit Default: meter/sec, Metric: meter/sec, Imperial: miles/hour.
        public static final String COLUMN_WIND_SPEED = "wind_speed";
        //Wind direction, degrees (meteorological)
        public static final String COLUMN_WIND_DEG = "wind_deg";
        //Cloudiness, %
        public static final String COLUMN_CLOUDS = "clouds";
        //Rain volume for last 3 hours, mm
        public static final String COLUMN_RAIN = "rain";
        //Snow volume for last 3 hours
        public static final String COLUMN_SNOW = "snow";
        //Temperature. Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit.
        public static final String COLUMN_MIN_TEMP = "temp_min";
        public static final String COLUMN_MAX_TEMP = "temp_max";
        public static final String COLUMN_AVG_TEMP = "temp_avg";

        // Humidity is stored as a float representing percentage
        public static final String COLUMN_HUMIDITY = "humidity";

        // Pressure
        public static final String COLUMN_PRESSURE = "pressure";
        public static final String COLUMN_PRESSURE_SEA_LEVEL = "pressure_sea_level";
        public static final String COLUMN_PRESSURE_GRND_LEVEL = "pressure_grnd_level";
        //Data/time of caluclation, UTC
        public static final String COLUMN_CALCULATION_TIME = "dt_txt";


        public static Uri buildUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class CurrentWeatherEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_WEATHER_CURRENT).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_WEATHER_CURRENT;

        public static final String TABLE_NAME = "weather_current";

        // Column with the foreign key into the location table.
        public static final String COLUMN_LOC_KEY = "location_id";
        // Date, stored as long in milliseconds since the epoch
        public static final String COLUMN_DATE = "timestamp";
        // Weather id as returned by API, to identify the icon to be used
        public static final String COLUMN_WEATHER_ID = "weather_id";
        //Group of weather parameters (Rain, Snow, Extreme etc.)
        public static final String COLUMN_WEATHER_MAIN = "weather_main";
        //Weather condition within the group
        public static final String COLUMN_WEATHER_DESC = "weather_description";
        //Weather icon id
        public static final String COLUMN_WEATHER_ICON = "weather_icon";
        //Wind speed. Unit Default: meter/sec, Metric: meter/sec, Imperial: miles/hour.
        public static final String COLUMN_WIND_SPEED = "wind_speed";
        //Wind direction, degrees (meteorological)
        public static final String COLUMN_WIND_DEG = "wind_deg";
        //Cloudiness, %
        public static final String COLUMN_CLOUDS = "clouds";
        //Rain volume for last 3 hours, mm
        public static final String COLUMN_RAIN = "rain";
        //Snow volume for last 3 hours
        public static final String COLUMN_SNOW = "snow";
        //Temperature. Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit.
        public static final String COLUMN_MIN_TEMP = "temp_min";
        public static final String COLUMN_MAX_TEMP = "temp_max";
        public static final String COLUMN_AVG_TEMP = "temp_avg";

        // Humidity is stored as a float representing percentage
        public static final String COLUMN_HUMIDITY = "humidity";
        //Sun, unix, UTC
        public static final String COLUMN_SUNRISE = "sunrise";
        public static final String COLUMN_SUNSET = "sunset";

        //Pressure
        public static final String COLUMN_PRESSURE = "pressure";
        public static final String COLUMN_PRESSURE_SEA_LEVEL = "pressure_sea_level";
        public static final String COLUMN_PRESSURE_GRND_LEVEL = "pressure_grnd_level";

        public static Uri buildUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
