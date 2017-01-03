package ua.in.quireg.sunshine_mine.core.parsers;

import android.util.Log;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import java.io.IOException;

import ua.in.quireg.sunshine_mine.BuildConfig;
import ua.in.quireg.sunshine_mine.core.models.hourly_forecast_models.HourlyWeatherAPIJsonRespondModel;
import ua.in.quireg.sunshine_mine.core.models.hourly_forecast_models.WeatherByHourModel;
import ua.in.quireg.sunshine_mine.exceptions.ParseWeatherFromJsonException;


public final class HourlyWeatherJsonParser {

    private final String LOG_TAG = HourlyWeatherJsonParser.class.getSimpleName();

    public WeatherByHourModel[] parserWeatherDataFromJson(String forecastJsonStr)
            throws ParseWeatherFromJsonException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationConfig.Feature.USE_ANNOTATIONS, true);

        try {
            if (BuildConfig.DEBUG) Log.d(LOG_TAG, "Processing retrieved JSON");

            HourlyWeatherAPIJsonRespondModel result = mapper.readValue(forecastJsonStr, HourlyWeatherAPIJsonRespondModel.class);
            return result.weatherByHourModels;

        } catch (IOException e) {
            throw new ParseWeatherFromJsonException(e);
        }
    }
}
