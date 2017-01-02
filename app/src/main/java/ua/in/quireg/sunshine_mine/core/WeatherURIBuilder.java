package ua.in.quireg.sunshine_mine.core;

import android.net.Uri;
import java.util.HashMap;

import ua.in.quireg.sunshine_mine.BuildConfig;

public class WeatherURIBuilder {
    private static final String APIkey = BuildConfig.WEATHER_API_KEY;

    public static Uri.Builder buildWeatherURIforID(HashMap<String, String> params){
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
        return uriRequest;
    }
}
