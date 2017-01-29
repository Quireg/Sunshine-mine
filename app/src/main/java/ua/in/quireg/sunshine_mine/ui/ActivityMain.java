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

import ua.in.quireg.sunshine_mine.R;
import ua.in.quireg.sunshine_mine.core.WeatherAPIParams;
import ua.in.quireg.sunshine_mine.core.WeatherSync;
import ua.in.quireg.sunshine_mine.core.WeatherURIBuilder;
import ua.in.quireg.sunshine_mine.data.WeatherDbHelper;
import ua.in.quireg.sunshine_mine.interfaces.IWeatherSyncCallback;


public class ActivityMain extends AppCompatActivity implements IWeatherSyncCallback {

    private static final String LOG_TAG = ActivityMain.class.getSimpleName();

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

        AsyncTask syncWeather = new WeatherSync(getApplicationContext(), this);
        syncWeather.execute();


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


    public ArrayAdapter<String> getArrayAdapter() {
        return arrayAdapter;
    }

    public void showPrefLocOnMap() {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("geo:0,0?q=" + coordinates.get("lat") + "," + coordinates.get("lon") + " (" + "Preferred City" + ")"));
        startActivity(intent);
    }


    @Override
    public void syncCompleted() {
        //TODO do something
    }
}
