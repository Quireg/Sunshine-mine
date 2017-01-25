package ua.in.quireg.sunshine_mine.core;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ua.in.quireg.sunshine_mine.BuildConfig;
import ua.in.quireg.sunshine_mine.exceptions.WeatherJsonDownloaderException;


public final class WeatherJsonDownloader {

    private static final String LOG_TAG = WeatherJsonDownloader.class.getSimpleName();

    public synchronized String fetch(Uri uriRequest) throws WeatherJsonDownloaderException {
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
                throw new WeatherJsonDownloaderException("Response code != 200");
            }

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                throw new WeatherJsonDownloaderException("Input was not created");
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                throw new WeatherJsonDownloaderException("Input stream is empty");
            }
            forecastJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e("WeatherJsonDownloader", "Error: ", e);
            throw new WeatherJsonDownloaderException("No data received");
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
                    Log.e("WeatherJsonDownloader", "Error closing stream: ", e);
                }
            }
        }
        return forecastJsonStr;
    }
}
