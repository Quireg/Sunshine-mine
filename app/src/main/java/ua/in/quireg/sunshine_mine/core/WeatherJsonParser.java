package ua.in.quireg.sunshine_mine.core;

import android.util.Log;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import java.io.IOException;
import java.text.SimpleDateFormat;

import ua.in.quireg.sunshine_mine.BuildConfig;
import ua.in.quireg.sunshine_mine.core.models.WeatherAPIJsonRespondModel;
import ua.in.quireg.sunshine_mine.core.models.WeatherByDayModel;
import ua.in.quireg.sunshine_mine.exceptions.ParseWeatherFromJsonException;


public final class WeatherJsonParser {
    private final String LOG_TAG = WeatherJsonParser.class.getSimpleName();

    /* The date/time conversion code is going to be moved outside the asynctask later,
     * so for convenience we're breaking it out into its own method now.
     */
    private String getReadableDateString(long time){
        // Because the API returns a unix timestamp (measured in seconds),
        // it must be converted to milliseconds in order to be converted to valid date.
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
        return shortenedDateFormat.format(time);
    }



     public WeatherByDayModel[] parserWeatherDataFromJson(String forecastJsonStr)
            throws ParseWeatherFromJsonException {

         ObjectMapper mapper = new ObjectMapper();
         mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
         mapper.configure(SerializationConfig.Feature.USE_ANNOTATIONS, true);

         try {
             if(BuildConfig.DEBUG) Log.d(LOG_TAG, "Processing retrieved JSON");

             WeatherAPIJsonRespondModel result = mapper.readValue(forecastJsonStr, WeatherAPIJsonRespondModel.class);
             return result.weatherByDayModels;

         } catch (IOException e) {
             throw new ParseWeatherFromJsonException(e);
         }
     }
}
