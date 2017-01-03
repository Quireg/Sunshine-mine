package ua.in.quireg.sunshine_mine.async;

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
import ua.in.quireg.sunshine_mine.core.models.daily_forecast_models.DailyWeatherAPIJsonRespondModel;
import ua.in.quireg.sunshine_mine.core.models.hourly_forecast_models.HourlyWeatherAPIJsonRespondModel;
import ua.in.quireg.sunshine_mine.exceptions.FetchWeatherFromAPIException;
import ua.in.quireg.sunshine_mine.exceptions.ParseWeatherFromJsonException;

public class RetrieveDailyForecastInBackground extends AsyncTask<Uri.Builder, Void, Pair<Uri.Builder, String>[]> {

    private static final String LOG_TAG = RetrieveDailyForecastInBackground.class.getSimpleName();

    @Override
    @SuppressWarnings("unchecked")
    protected Pair<Uri.Builder, String>[] doInBackground(Uri.Builder... params) {
        try {
            Pair<Uri.Builder, String>[] result = new Pair[params.length];
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
    protected void onPostExecute(Pair<Uri.Builder, String>[] pairs) {
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

//        //Update ListView adapter
//        List<String> arrayListOfData = new ArrayList<>(Arrays.asList(strings));
//        arrayAdapter.clear();
//        for (int i = 0; i < arrayListOfData.size(); i++) {
//            arrayAdapter.insert(arrayListOfData.get(i), i);
//        }
//        arrayAdapter.notifyDataSetChanged();
//
//        flf.refreshColmpleted();
    }

    private void parseRetrievedJson(Pair<Uri.Builder, String>[] pairs) throws ParseWeatherFromJsonException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationConfig.Feature.USE_ANNOTATIONS, true);
        for (Pair<Uri.Builder, String> pair : pairs) {
            try {
                if (BuildConfig.DEBUG) Log.d(LOG_TAG, "Processing retrieved JSON");

                if (WeatherURIBuilder.uriMatcher(pair.first) == WeatherURIBuilder.CurrentWeatherUri) {
                    //TODO create models for current weather
                    //TODO import respond into database
                } else if (WeatherURIBuilder.uriMatcher(pair.first) == WeatherURIBuilder.HourlyForecastUri) {
                    HourlyWeatherAPIJsonRespondModel respondModel
                            = mapper.readValue(pair.second, HourlyWeatherAPIJsonRespondModel.class);
                    //TODO import respond into database
                } else if (WeatherURIBuilder.uriMatcher(pair.first) == WeatherURIBuilder.DailyForecastUri) {
                    DailyWeatherAPIJsonRespondModel respondModel
                            = mapper.readValue(pair.second, DailyWeatherAPIJsonRespondModel.class);
                    //TODO import respond into database
                }

            } catch (IOException e) {
                e.printStackTrace();
                throw new ParseWeatherFromJsonException(e);
            }
        }
    }

}