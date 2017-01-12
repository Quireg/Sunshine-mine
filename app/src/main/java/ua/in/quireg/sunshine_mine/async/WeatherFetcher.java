package ua.in.quireg.sunshine_mine.async;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import ua.in.quireg.sunshine_mine.core.FetchWeatherAPIData;
import ua.in.quireg.sunshine_mine.exceptions.FetchWeatherFromAPIException;

public class WeatherFetcher extends AsyncTask<Uri, Void, String> {

    private static final String LOG_TAG = WeatherFetcher.class.getSimpleName();

    private Context mContext;

    public WeatherFetcher(Context context) {
        mContext = context;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected String doInBackground(Uri... params) {

        try {
            return FetchWeatherAPIData.fetch(params[0]);
        } catch (FetchWeatherFromAPIException e) {
            e.printStackTrace();
        }
        return null;
    }

}