package ua.in.quireg.sunshine_mine.async;

import android.net.Uri;
import android.util.Log;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import java.io.IOException;

import ua.in.quireg.sunshine_mine.BuildConfig;
import ua.in.quireg.sunshine_mine.core.WeatherURIBuilder;
import ua.in.quireg.sunshine_mine.core.models.current_weather_models.CurrentWeatherModelAPIJsonRespondModel;
import ua.in.quireg.sunshine_mine.core.models.daily_forecast_models.DailyWeatherModelAPIJsonRespondModel;
import ua.in.quireg.sunshine_mine.core.models.hourly_forecast_models.HourlyWeatherModelAPIJsonRespondModel;
import ua.in.quireg.sunshine_mine.exceptions.ParseWeatherFromJsonException;
import ua.in.quireg.sunshine_mine.interfaces.WeatherModel;


public class WeatherJsonParser {
    ObjectMapper mapper;

    private static final String LOG_TAG = WeatherFetcher.class.getSimpleName();

    public WeatherJsonParser(){
        mapper = new ObjectMapper();
        mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationConfig.Feature.USE_ANNOTATIONS, true);
    }


    public WeatherModel parseRetrievedJson(String jsonString, Uri uriScheme) throws ParseWeatherFromJsonException {
        if(jsonString == null){
            throw new ParseWeatherFromJsonException("Received empty JSON string");
        }

        if (BuildConfig.DEBUG)
            Log.d(LOG_TAG, "Processing retrieved JSON " + uriScheme.toString());

        try {
            if (WeatherURIBuilder.uriMatcher(uriScheme) == WeatherURIBuilder.CurrentWeatherUri) {
                return mapper.readValue(jsonString, CurrentWeatherModelAPIJsonRespondModel.class);

            } else if (WeatherURIBuilder.uriMatcher(uriScheme) == WeatherURIBuilder.HourlyForecastUri) {
                return mapper.readValue(jsonString, HourlyWeatherModelAPIJsonRespondModel.class);

            } else if (WeatherURIBuilder.uriMatcher(uriScheme) == WeatherURIBuilder.DailyForecastUri) {
                return mapper.readValue(jsonString, DailyWeatherModelAPIJsonRespondModel.class);
            }else{
                throw new ParseWeatherFromJsonException("No match for uriScheme");
            }
        } catch (IOException e) {
            throw new ParseWeatherFromJsonException(e);
        }
    }
}
