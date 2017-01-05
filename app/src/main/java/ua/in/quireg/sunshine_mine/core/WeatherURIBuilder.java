package ua.in.quireg.sunshine_mine.core;

import android.net.Uri;
import java.util.HashMap;

import ua.in.quireg.sunshine_mine.BuildConfig;

public class WeatherURIBuilder {
    private static final String APIkey = BuildConfig.WEATHER_API_KEY;

    public static final int CurrentWeatherUri = 1;
    public static final int HourlyForecastUri = 2;
    public static final int DailyForecastUri = 3;

    public static Uri buildWeatherURIforID(HashMap<String, String> params){
        Uri.Builder uriRequest = new Uri.Builder()
                .scheme("http")
                .authority("api.openweathermap.org")
                .appendPath("data")
                .appendPath("2.5")
                .appendPath("forecast")
                .appendPath("daily");

        uriRequest.appendQueryParameter("id", params.get("id"))
                .appendQueryParameter("mode", params.get("mode"))
                .appendQueryParameter("cnt", params.get("cnt"))
                .appendQueryParameter("units", params.get("units"));

        uriRequest.appendQueryParameter("APPID", APIkey);
        return uriRequest.build();
    }

    public static int uriMatcher(Uri.Builder builder){
        switch (builder.build().getLastPathSegment()){
            case "http://api.openweathermap.org/data/2.5/forecast/daily":
                return DailyForecastUri;
            case "http://api.openweathermap.org/data/2.5/forecast":
                return HourlyForecastUri;
            case "http://api.openweathermap.org/data/2.5/weather":
                return CurrentWeatherUri;
            default:
                return 0;
        }
    }
}
