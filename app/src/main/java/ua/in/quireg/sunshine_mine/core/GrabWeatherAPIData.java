package ua.in.quireg.sunshine_mine.core;

import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;


/**
 * Created by Artur Menchenko on 9/29/2016.
 */

public final class GrabWeatherAPIData {

    private static final String APIkey = "a3e3e85f9b157042fe69042cdefee044";
    private static final String LOG_TAG = GrabWeatherAPIData.class.getSimpleName();
    private static final String PREF_FILE_NAME = "pref_general.xml";

    private static String forecastJsonStrOld;
    private static SharedPreferences preferences ;

    //URIBuilder is unsafe for concurrency so it`s better to make whole method synchronized.
    public static synchronized String grabData(Map<String, String> param) throws IOException {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;


        Uri.Builder uriRequest = new Uri.Builder()
                .scheme("http")
                .authority("api.openweathermap.org")
                .appendPath("data")
                .appendPath("2.5")
                .appendPath("forecast")
                .appendPath("daily");

        if (param.get("id") != null) {
            uriRequest.appendQueryParameter("id", param.get("id"));
        }else if(param.get("zip") !=null && param.get("id") == null){
            uriRequest.appendQueryParameter("zip", param.get("zip"));
        }else{
            throw new IOException();
        }

        uriRequest.appendQueryParameter("mode", param.get("mode"))
                .appendQueryParameter("cnt", param.get("cnt"))
                .appendQueryParameter("units", param.get("units"));

        uriRequest.appendQueryParameter("APPID", APIkey);

        String forecastJsonStr = null;
        try {
            Log.d("GrabWeatherAPIData", "Attempting to fetch:" + uriRequest.toString());

            URL url = new URL(uriRequest.toString());

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
                return forecastJsonStr;
            }
            forecastJsonStr = buffer.toString();
            Log.d(LOG_TAG, "Forecast JSON string:" + forecastJsonStr);
            forecastJsonStrOld = forecastJsonStr;
        } catch(FileNotFoundException e){
            Log.d(LOG_TAG, "Received Error from API");
            e.printStackTrace();
            return forecastJsonStr;
        } catch (IOException e) {
            Log.e("GrabWeatherAPIData", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
            forecastJsonStr = forecastJsonStrOld;
        } finally {
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
