package ua.in.quireg.sunshine_mine.async;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import ua.in.quireg.sunshine_mine.core.WeatherJsonDownloader;
import ua.in.quireg.sunshine_mine.exceptions.WeatherJsonDownloaderException;

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
            return new WeatherJsonDownloader().fetch(params[0]);
        } catch (WeatherJsonDownloaderException e) {
            e.printStackTrace();
        }
        return null;
    }

}