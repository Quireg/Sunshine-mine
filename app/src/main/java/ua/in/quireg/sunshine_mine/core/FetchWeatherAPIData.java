package ua.in.quireg.sunshine_mine.core;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import ua.in.quireg.sunshine_mine.BuildConfig;
import ua.in.quireg.sunshine_mine.exceptions.FetchWeatherFromAPIException;


public final class FetchWeatherAPIData {

    private static final String LOG_TAG = FetchWeatherAPIData.class.getSimpleName();

    public static synchronized String fetch(Uri.Builder uriRequest) throws FetchWeatherFromAPIException {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String forecastJsonStr = null;
        try {
            if(BuildConfig.DEBUG) Log.d(LOG_TAG, "Attempting to fetch:" + uriRequest.toString());

            URL url = new URL(uriRequest.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode() != 200){
                if(BuildConfig.DEBUG) Log.d(LOG_TAG, "Server responded with: " + urlConnection.getResponseCode()
                        + " " + urlConnection.getResponseMessage());
                throw new FetchWeatherFromAPIException("Response code != 200");
            }

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                throw new FetchWeatherFromAPIException("Input was not created");
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
                throw new FetchWeatherFromAPIException("Input stream is empty");
            }
            forecastJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e("FetchWeatherAPIData", "Error: ", e);
            throw new FetchWeatherFromAPIException("No data received");
            // If the code didn't successfully get the weather data, there's no point in attempting
            // to parse it.
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("FetchWeatherAPIData", "Error closing stream: ", e);
                }
            }
        }
        return forecastJsonStr;
    }
}
