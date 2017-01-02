package ua.in.quireg.sunshine_mine.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ua.in.quireg.sunshine_mine.R;
import ua.in.quireg.sunshine_mine.core.FetchWeatherAPIData;
import ua.in.quireg.sunshine_mine.core.WeatherAPIParams;
import ua.in.quireg.sunshine_mine.core.WeatherJsonParser;
import ua.in.quireg.sunshine_mine.core.models.WeatherByDayModel;
import ua.in.quireg.sunshine_mine.exceptions.FetchWeatherFromAPIException;
import ua.in.quireg.sunshine_mine.exceptions.ParseWeatherFromJsonException;


public class ActivityMain extends AppCompatActivity {

    private static final String LOG_TAG = ActivityMain.class.getSimpleName();

    private HashMap<String, String> requestParams;

    public static HashMap<String, Double> coordinates = new HashMap<>();

    private String[] forecast = {"Nothing to see here"};
    SharedPreferences pref;
    FragmentForecastList flf;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                R.layout.fragment_forecast_list_item,
                new ArrayList<>(Arrays.asList(forecast))
        );

        setContentView(R.layout.activity_main);

        Bundle args = new Bundle();

        flf = new FragmentForecastList();
        flf.setArguments(args);

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, flf)
                    .commit();
        }
    }

    private void initializeWeatherParameters(){
        //TODO get rid of this method.
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        requestParams = new HashMap<String, String>();

        //get location from params
        requestParams.put(WeatherAPIParams.CITY_ID, pref.getString(getString(R.string.settings_location_key), initializeLocationSetting()));
        requestParams.put(WeatherAPIParams.ZIP_CODE, "01032");
        requestParams.put(WeatherAPIParams.DAYS_COUNT, pref.getString(getString(R.string.settings_dayscount_key),"15"));
        requestParams.put(WeatherAPIParams.UNITS, "metric");
        requestParams.put(WeatherAPIParams.OUTPUT_MODE, "json");
    }
    private String initializeLocationSetting(){
        //TODO get default location from GPS
        String result = "703448";

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if(!preferences.contains(getString(R.string.settings_location_key))) {
            SharedPreferences.Editor editor = preferences.edit();
            if(editor != null) {
                editor.putString(getString(R.string.settings_location_key), result);
                editor.apply();
            }
        }
        return result;
    }

    //callback to retrieve new data and propagate it to array adapter
    public void refreshForecast(){
        Log.d(LOG_TAG, "Refresh Forecast invoked!");

        RetrieveWeatherInBackground task = new RetrieveWeatherInBackground();
        initializeWeatherParameters();
        task.execute(requestParams);
    }

    public ArrayAdapter<String> getArrayAdapter() {
        return arrayAdapter;
    }

    public void showPrefLocOnMap(){
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("geo:0,0?q="+ coordinates.get("lat") + "," + coordinates.get("lon") + " (" + "Preferred City" + ")"));
        startActivity(intent);
    }


    private class RetrieveWeatherInBackground extends AsyncTask<HashMap<String, String>, Void, String[]> {
        @Override
        protected String[] doInBackground(HashMap<String, String>... params) {
            try {
                String JSONData = FetchWeatherAPIData.fetch(params[0]);
                WeatherJsonParser parser = new WeatherJsonParser();
                boolean isMetric = (pref.getString(getString(R.string.settings_units_key), "metric").equalsIgnoreCase("metric"));
                WeatherByDayModel[] result = parser.parserWeatherDataFromJson(JSONData);
                if(result != null){
                    String[] retrievedInfoToBeDisplayed = new String[result.length];
                    for (int i = 0; i < result.length; i++) {
                        retrievedInfoToBeDisplayed[i] =  formatHighLows(result[i].temperatureModel.min, result[i].temperatureModel.max);
                    }
                    forecast = retrievedInfoToBeDisplayed;
                    return retrievedInfoToBeDisplayed;
                }
                return new String[]{"No Data Received"};
            } catch (FetchWeatherFromAPIException e) {
                e.printStackTrace();
            } catch (ParseWeatherFromJsonException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String[] strings) {
            //Update ListView adapter
            List<String> arrayListOfData = new ArrayList<>(Arrays.asList(strings));
            arrayAdapter.clear();
            for (int i = 0; i < arrayListOfData.size(); i++) {
                arrayAdapter.insert(arrayListOfData.get(i), i);
            }
            arrayAdapter.notifyDataSetChanged();

            flf.refreshColmpleted();
        }

        private String formatHighLows(double high, double low) {
            // For presentation, assume the user doesn't care about tenths of a degree.
            long roundedHigh = Math.round(high);
            long roundedLow = Math.round(low);

            String highLowStr = roundedHigh + "  --/--  " + roundedLow;
            return highLowStr;
        }

    }

}
