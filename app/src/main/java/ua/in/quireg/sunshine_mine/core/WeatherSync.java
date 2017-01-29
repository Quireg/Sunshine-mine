package ua.in.quireg.sunshine_mine.core;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import ua.in.quireg.sunshine_mine.BuildConfig;
import ua.in.quireg.sunshine_mine.async.WeatherJsonParser;
import ua.in.quireg.sunshine_mine.core.models.current_weather_models.CurrentWeatherModel;
import ua.in.quireg.sunshine_mine.core.models.daily_forecast_models.DailyWeatherModel;
import ua.in.quireg.sunshine_mine.core.models.hourly_forecast_models.HourlyWeatherModel;
import ua.in.quireg.sunshine_mine.data.WeatherDbImporter;
import ua.in.quireg.sunshine_mine.exceptions.ParseWeatherFromJsonException;
import ua.in.quireg.sunshine_mine.exceptions.WeatherJsonDownloaderException;
import ua.in.quireg.sunshine_mine.interfaces.IWeatherModel;
import ua.in.quireg.sunshine_mine.interfaces.IWeatherSyncCallback;

public class WeatherSync extends AsyncTask<Void, Void, Void> {
    private static final String LOG_TAG = WeatherSync.class.getSimpleName();
    WeatherURIBuilder uriBuilder;
    WeatherJsonParser parser;
    WeatherJsonDownloader downloader;
    WeatherDbImporter importer;
    long lastSyncTime = 0; //have not synced yet.
    IWeatherSyncCallback mCallback;

    private Context mContext;

    public WeatherSync(Context context, IWeatherSyncCallback callback) {
        this.mContext = context;
        this.mCallback = callback;

        this.uriBuilder = new WeatherURIBuilder(this.mContext);
        this.parser = new WeatherJsonParser();
        this.downloader = new WeatherJsonDownloader();
        this.importer = new WeatherDbImporter(this.mContext);

    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            if (!isSyncRequired()) {
                return null;
            }
            if (BuildConfig.DEBUG) {
                Log.d(LOG_TAG, "Weather sync started");
            }

            syncDailyWeather();
            syncHourlyWeather();
            syncCurrentWeather();

            lastSyncTime = System.currentTimeMillis() / 1000L;


        } catch (WeatherJsonDownloaderException e) {
            e.printStackTrace();
        } catch (ParseWeatherFromJsonException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mCallback.syncCompleted();
    }

    private boolean isSyncRequired() {
        long currentTime = System.currentTimeMillis() / 1000L;
        if(lastSyncTime == 0){
            return true;
        }
        if(currentTime - lastSyncTime > 100000){
            return true;
        }
        return false;
    }

    private void syncDailyWeather() throws WeatherJsonDownloaderException, ParseWeatherFromJsonException {
        //Uri to be retrieved
        Uri uri = this.uriBuilder.buildDailyWeather();
        //JSON retrieved
        String retrievedJson = this.downloader.fetch(uri);
        //Model from JSON
        IWeatherModel weatherModel = this.parser.parseRetrievedJson(retrievedJson, WeatherURIBuilder.DailyForecastUri);
        //Model to database
        this.importer.proceedDailyWeather((DailyWeatherModel) weatherModel );
    }

    private void syncHourlyWeather() throws WeatherJsonDownloaderException, ParseWeatherFromJsonException {

        //Uri to be retrieved
        Uri uri = this.uriBuilder.buildHourlyWeather();
        //JSON retrieved
        String retrievedJson = this.downloader.fetch(uri);
        //Model from JSON
        IWeatherModel weatherModel = this.parser.parseRetrievedJson(retrievedJson, WeatherURIBuilder.HourlyForecastUri);
        //Model to database
        this.importer.proceedHourlyWeather((HourlyWeatherModel) weatherModel );

    }

    private void syncCurrentWeather() throws WeatherJsonDownloaderException, ParseWeatherFromJsonException {

        //Uri to be retrieved
        Uri uri = this.uriBuilder.buildCurrentWeather();
        //JSON retrieved
        String retrievedJson = this.downloader.fetch(uri);
        //Model from JSON
        IWeatherModel weatherModel = this.parser.parseRetrievedJson(retrievedJson, WeatherURIBuilder.CurrentWeatherUri);
        //Model to database
        this.importer.proceedCurrentWeather((CurrentWeatherModel) weatherModel );

    }

}
