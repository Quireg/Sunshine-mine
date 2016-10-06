package ua.in.quireg.sunshine_mine;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by Artur Menchenko on 9/29/2016.
 */

public final class GrabWeatherAPIData {

    private static final String APIkey = "a3e3e85f9b157042fe69042cdefee044";
    private static final String LOG_TAG = "GrabWeatherAPIData";
    private static String forecastJsonStrOld;

    static String grabData(Map<String, String> param){
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Construct the URL for the OpenWeatherMap query
        // Possible parameters are available at OWM's forecast API page, at
        // http://openweathermap.org/API#forecast
        String urlRequest = "http://api.openweathermap.org/data/2.5/forecast/daily?" + "id=" + param.get("cityID") + "&"
                + "mode=" + param.get("mode") + "&" + "cnt=" + param.get("numberOfDays") + "&" + "units=" + param.get("units") + "&APPID=" + APIkey;
        String forecastJsonStr = null;
        try {
            Log.d("GrabWeatherAPIData", "Attempting to fetch:" + urlRequest);


            URL url = new URL(urlRequest);

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                forecastJsonStr = forecastJsonStrOld;
                return forecastJsonStr;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                forecastJsonStr = forecastJsonStrOld;
                return  forecastJsonStr;
            }
            forecastJsonStr = buffer.toString();
            Log.v(LOG_TAG, "Forecast JSON string" + forecastJsonStr);
            forecastJsonStrOld = forecastJsonStr;
        } catch (IOException e) {
            Log.e("GrabWeatherAPIData", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
            forecastJsonStr = forecastJsonStrOld;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("GrabWeatherAPIData", "Error closing stream", e);
                }
            }
        }
        return forecastJsonStr;
    }
}
