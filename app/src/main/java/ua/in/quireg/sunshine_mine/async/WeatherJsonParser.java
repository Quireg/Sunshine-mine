package ua.in.quireg.sunshine_mine.async;

import android.net.Uri;
import android.util.Log;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

import ua.in.quireg.sunshine_mine.BuildConfig;
import ua.in.quireg.sunshine_mine.core.WeatherURIBuilder;
import ua.in.quireg.sunshine_mine.core.models.current_weather_models.CurrentWeatherModel;
import ua.in.quireg.sunshine_mine.core.models.daily_forecast_models.DailyWeatherModel;
import ua.in.quireg.sunshine_mine.core.models.hourly_forecast_models.HourlyWeatherModel;
import ua.in.quireg.sunshine_mine.exceptions.ParseWeatherFromJsonException;
import ua.in.quireg.sunshine_mine.interfaces.IWeatherModel;


public class WeatherJsonParser {
    ObjectMapper mapper;

    private static final String LOG_TAG = WeatherJsonParser.class.getSimpleName();

    public WeatherJsonParser() {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    public IWeatherModel parseRetrievedJson(String jsonString, Uri uriType) throws ParseWeatherFromJsonException {
        if (jsonString == null) {
            throw new ParseWeatherFromJsonException("Received empty JSON string");
        }

        if (BuildConfig.DEBUG)
            Log.d(LOG_TAG, "Processing retrieved JSON ");

        try {


            if (uriType.equals(WeatherURIBuilder.CurrentWeatherUri)) {
                return mapper.readValue(jsonString, CurrentWeatherModel.class);

            } else if (uriType.equals(WeatherURIBuilder.HourlyForecastUri)) {
                return mapper.readValue(jsonString, HourlyWeatherModel.class);

            } else if (uriType.equals(WeatherURIBuilder.DailyForecastUri)) {
                return mapper.readValue(jsonString, DailyWeatherModel.class);
            } else {
                throw new ParseWeatherFromJsonException("No match for uriScheme");
            }
        } catch (IOException e) {
            throw new ParseWeatherFromJsonException(e);
        }
    }
}
