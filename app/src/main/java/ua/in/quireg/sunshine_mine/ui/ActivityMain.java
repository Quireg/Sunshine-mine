package ua.in.quireg.sunshine_mine.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import ua.in.quireg.sunshine_mine.R;
import ua.in.quireg.sunshine_mine.async.RetrieveWeatherInBackground;
import ua.in.quireg.sunshine_mine.core.WeatherAPIParams;
import ua.in.quireg.sunshine_mine.core.WeatherURIBuilder;
import ua.in.quireg.sunshine_mine.data.WeatherDbHelper;


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

        //Initiate DB creation.
        WeatherDbHelper.importDatabase(getApplicationContext());


        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                R.layout.fragment_forecast_list_item,
                new ArrayList<>(Arrays.asList(forecast))
        );

        setContentView(R.layout.activity_main);

        Bundle args = new Bundle();

        flf = new FragmentForecastList();
        flf.setArguments(args);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, flf)
                    .commit();
        }
    }

    private void initializeWeatherParameters() {
        //TODO get rid of this method.
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        requestParams = new HashMap<String, String>();

        //get location from params
        requestParams.put(WeatherAPIParams.CITY_ID, pref.getString(getString(R.string.settings_location_key), initializeLocationSetting()));
        requestParams.put(WeatherAPIParams.ZIP_CODE, "01032");
        requestParams.put(WeatherAPIParams.DAYS_COUNT, pref.getString(getString(R.string.settings_dayscount_key), "15"));
        requestParams.put(WeatherAPIParams.UNITS, "metric");
        requestParams.put(WeatherAPIParams.OUTPUT_MODE, "json");
    }

    private String initializeLocationSetting() {
        //TODO get default location from GPS
        String result = "703448";

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (!preferences.contains(getString(R.string.settings_location_key))) {
            SharedPreferences.Editor editor = preferences.edit();
            if (editor != null) {
                editor.putString(getString(R.string.settings_location_key), result);
                editor.apply();
            }
        }
        return result;
    }

    //callback to retrieve new data and propagate it to array adapter
    public void refreshForecast() {
        Log.d(LOG_TAG, "Refresh Forecast invoked!");

        RetrieveWeatherInBackground task = new RetrieveWeatherInBackground();
        initializeWeatherParameters();
        Uri.Builder builder = WeatherURIBuilder.buildWeatherURIforID(requestParams);
        task.execute(builder);
    }

    public ArrayAdapter<String> getArrayAdapter() {
        return arrayAdapter;
    }

    public void showPrefLocOnMap() {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("geo:0,0?q=" + coordinates.get("lat") + "," + coordinates.get("lon") + " (" + "Preferred City" + ")"));
        startActivity(intent);
    }




}
