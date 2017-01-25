package ua.in.quireg.sunshine_mine.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.net.Uri;
import android.preference.PreferenceManager;

import java.util.HashMap;

import ua.in.quireg.sunshine_mine.BuildConfig;
import ua.in.quireg.sunshine_mine.R;

public class WeatherURIBuilder {

    private static final String APIkey = BuildConfig.WEATHER_API_KEY;

    public static final Uri BaseWeatherUri = new Uri.Builder()
            .scheme("http")
            .authority("api.openweathermap.org")
            .appendPath("data")
            .appendPath("2.5")
            .build();

    public static final Uri CurrentWeatherUri = BaseWeatherUri.buildUpon().appendPath("weather").build();
    public static final Uri HourlyForecastUri = BaseWeatherUri.buildUpon().appendPath("forecast").build();
    public static final Uri DailyForecastUri = BaseWeatherUri.buildUpon().appendPath("forecast").appendPath("daily").build();

    private SharedPreferences preferences;
    private Context mContext;

    public WeatherURIBuilder(Context context){
        this.mContext = context;
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }



    public Uri buildCurrentWeather(){

        Uri result = CurrentWeatherUri.buildUpon()
                .appendQueryParameter(WeatherAPIParams.CITY_ID, preferences.getString(this.mContext.getString(R.string.settings_location_key), ""))
                .appendQueryParameter(WeatherAPIParams.OUTPUT_MODE, "json")
                .appendQueryParameter(WeatherAPIParams.UNITS, "metric")
                .appendQueryParameter("APPID", APIkey)
                .build();
        return result;
    }

    public Uri buildHourlyWeather(){
        Uri result = HourlyForecastUri.buildUpon()
                .appendQueryParameter(WeatherAPIParams.CITY_ID, preferences.getString(this.mContext.getString(R.string.settings_location_key), ""))
                .appendQueryParameter(WeatherAPIParams.OUTPUT_MODE, "json")
                .appendQueryParameter(WeatherAPIParams.UNITS, "metric")
                .appendQueryParameter("APPID", APIkey)
                .build();
        return result;
    }

    public Uri buildDailyWeather(){

        Uri result = DailyForecastUri.buildUpon()
                .appendQueryParameter(WeatherAPIParams.CITY_ID, preferences.getString(this.mContext.getString(R.string.settings_location_key), ""))
                .appendQueryParameter(WeatherAPIParams.DAYS_COUNT, preferences.getString(this.mContext.getString(R.string.settings_dayscount_key), "15"))
                .appendQueryParameter(WeatherAPIParams.OUTPUT_MODE, "json")
                .appendQueryParameter(WeatherAPIParams.UNITS, "metric")
                .appendQueryParameter("APPID", APIkey)
                .build();
        return result;
    }


    public static Uri getType(Uri uri){
        Uri fullUri = BaseWeatherUri.buildUpon().appendPath(uri.getPath()).build();

        if(fullUri.equals(CurrentWeatherUri)){
            return CurrentWeatherUri;
        }else if(fullUri.equals(DailyForecastUri)){
            return DailyForecastUri;
        }else if(fullUri.equals(HourlyForecastUri)) {
            return HourlyForecastUri;
        }else return null;
    }

}
