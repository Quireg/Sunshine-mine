package ua.in.quireg.sunshine_mine.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ua.in.quireg.sunshine_mine.R;
import ua.in.quireg.sunshine_mine.core.GrabWeatherAPIData;
import ua.in.quireg.sunshine_mine.core.WeatherAPIParams;
import ua.in.quireg.sunshine_mine.core.WeatherDataParser;
import ua.in.quireg.sunshine_mine.data.WeatherDbHelper;


public class ActivityMain extends AppCompatActivity {

    private static final String LOG_TAG = ActivityMain.class.getSimpleName();

    private Map<String, String> requestParams;

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
        requestParams = new HashMap<>();

        //get location from params
        requestParams.put(WeatherAPIParams.CITY_ID, pref.getString(getString(R.string.settings_location_key),""));
        requestParams.put(WeatherAPIParams.ZIP_CODE, "01032");
        requestParams.put(WeatherAPIParams.DAYS_COUNT, pref.getString(getString(R.string.settings_dayscount_key),""));
        requestParams.put(WeatherAPIParams.UNITS, "metric");
        requestParams.put(WeatherAPIParams.OUTPUT_MODE, "json");
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


    private class RetrieveWeatherInBackground extends AsyncTask<Map<String, String>, Void, String[]> {
        @Override
        protected String[] doInBackground(Map<String, String>... params) {
            try {
                String JSONData = GrabWeatherAPIData.grabData(params[0]);
                WeatherDataParser wdp = new WeatherDataParser();
                boolean isMetricBool = (pref.getString(getString(R.string.settings_units_key), "metric").equalsIgnoreCase("metric"));
                String[] result = wdp.getWeatherDataFromJson(JSONData, isMetricBool);
                if(result != null){
                    forecast = result;
                    return result;
                }
                return new String[]{"No Data Received"};
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
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

    }

}