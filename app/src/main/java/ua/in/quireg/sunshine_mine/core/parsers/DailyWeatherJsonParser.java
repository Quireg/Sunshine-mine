package ua.in.quireg.sunshine_mine.core.parsers;

import android.util.Log;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import java.io.IOException;

import ua.in.quireg.sunshine_mine.BuildConfig;
import ua.in.quireg.sunshine_mine.core.models.daily_forecast_models.DailyWeatherAPIJsonRespondModel;
import ua.in.quireg.sunshine_mine.core.models.daily_forecast_models.WeatherByDayModel;
import ua.in.quireg.sunshine_mine.exceptions.ParseWeatherFromJsonException;


public final class DailyWeatherJsonParser {

    private final String LOG_TAG = DailyWeatherJsonParser.class.getSimpleName();

    public WeatherByDayModel[] parserWeatherDataFromJson(String forecastJsonStr)
            throws ParseWeatherFromJsonException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationConfig.Feature.USE_ANNOTATIONS, true);

        try {
            if (BuildConfig.DEBUG) Log.d(LOG_TAG, "Processing retrieved JSON");

            DailyWeatherAPIJsonRespondModel result = mapper.readValue(forecastJsonStr, DailyWeatherAPIJsonRespondModel.class);
            return result.weatherByDayModels;

        } catch (IOException e) {
            throw new ParseWeatherFromJsonException(e);
        }
    }
}
