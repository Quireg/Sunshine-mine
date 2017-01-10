package ua.in.quireg.sunshine_mine.async;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import java.io.IOException;

import ua.in.quireg.sunshine_mine.BuildConfig;
import ua.in.quireg.sunshine_mine.core.FetchWeatherAPIData;
import ua.in.quireg.sunshine_mine.core.WeatherURIBuilder;
import ua.in.quireg.sunshine_mine.core.models.current_weather_models.CurrentWeatherAPIJsonRespondModel;
import ua.in.quireg.sunshine_mine.core.models.daily_forecast_models.DailyWeatherAPIJsonRespondModel;
import ua.in.quireg.sunshine_mine.core.models.hourly_forecast_models.HourlyWeatherAPIJsonRespondModel;
import ua.in.quireg.sunshine_mine.data.WeatherDbImporter;
import ua.in.quireg.sunshine_mine.exceptions.FetchWeatherFromAPIException;
import ua.in.quireg.sunshine_mine.exceptions.ParseWeatherFromJsonException;

public class RetrieveWeatherInBackground extends AsyncTask<Uri, Void, Pair<Uri, String>[]> {

    private static final String LOG_TAG = RetrieveWeatherInBackground.class.getSimpleName();

    private Context mContext;

    public RetrieveWeatherInBackground(Context context) {
        mContext = context;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Pair<Uri, String>[] doInBackground(Uri... params) {
        try {
            Pair<Uri, String>[] result = new Pair[params.length];
            for (int i = 0; i < params.length; i++) {
                result[i] = new Pair<>(params[i], FetchWeatherAPIData.fetch(params[i]));
            }
            return result;
        } catch (FetchWeatherFromAPIException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Pair<Uri, String>[] pairs) {
        if (pairs == null) {
            Log.e(LOG_TAG, "Fetch error. See stacktrace");
            return;
        }

        try {
            parseRetrievedJson(pairs);
        } catch (ParseWeatherFromJsonException e) {
            e.printStackTrace();
        }
        //TODO notify about successful sync.

    }

    private void parseRetrievedJson(Pair<Uri, String>[] pairs) throws ParseWeatherFromJsonException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationConfig.Feature.USE_ANNOTATIONS, true);

        WeatherDbImporter weatherDbImporter = new WeatherDbImporter(this.mContext);

        for (Pair<Uri, String> pair : pairs) {
            boolean result = false;
            try {
                if (BuildConfig.DEBUG) Log.d(LOG_TAG, "Processing retrieved JSON " + pair.first.getLastPathSegment());

                if (WeatherURIBuilder.uriMatcher(pair.first) == WeatherURIBuilder.CurrentWeatherUri) {
                    CurrentWeatherAPIJsonRespondModel respondModel =
                            mapper.readValue(pair.second, CurrentWeatherAPIJsonRespondModel.class);

                    result = weatherDbImporter.proceedCurrentWeatherAPIJsonRespondModel(respondModel);

                } else if (WeatherURIBuilder.uriMatcher(pair.first) == WeatherURIBuilder.HourlyForecastUri) {
                    HourlyWeatherAPIJsonRespondModel respondModel
                            = mapper.readValue(pair.second, HourlyWeatherAPIJsonRespondModel.class);

                    result = weatherDbImporter.proceedHourlyWeatherAPIJsonRespondModel(respondModel);

                } else if (WeatherURIBuilder.uriMatcher(pair.first) == WeatherURIBuilder.DailyForecastUri) {
                    DailyWeatherAPIJsonRespondModel respondModel
                            = mapper.readValue(pair.second, DailyWeatherAPIJsonRespondModel.class);

                    result = weatherDbImporter.proceedDailyWeatherAPIJsonRespondModel(respondModel);

                }
                if(!result){
                    Log.e(LOG_TAG, "Error while exporting to the database");
                }

            } catch (IOException e) {
                e.printStackTrace();
                throw new ParseWeatherFromJsonException(e);
            }
        }
    }

}