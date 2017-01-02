package ua.in.quireg.sunshine_mine.ui;

/**
 * Created by Artur Menchenko on 10/15/2016.
 */

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;

import ua.in.quireg.sunshine_mine.R;
import ua.in.quireg.sunshine_mine.core.models.LocationModel;
import ua.in.quireg.sunshine_mine.data.WeatherContract;


public class ActivitySettings extends PreferenceActivity implements
        FragmentLocationSettings.OnFragmentInteractionListener,
        SharedPreferences.OnSharedPreferenceChangeListener{
    private static final String LOG_TAG = ActivitySettings.class.getSimpleName();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "Settings launched");
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "started");
        addPreferencesFromResource(R.xml.pref_general);
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        onSharedPreferenceChanged(prefs, getString(R.string.settings_dayscount_key));
        onSharedPreferenceChanged(prefs, getString(R.string.settings_location_key));
        onSharedPreferenceChanged(prefs, getString(R.string.settings_units_key));
    }

    @Override
    public void onFragmentInteraction(LocationModel loc) {

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);

        if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list (since they have separate labels/values).
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(sharedPreferences.getString(key, ""));
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else {
            // For other preferences, set the summary to the value's simple string representation.
            String cityName = "not initialized";
            if(key.equals(getString(R.string.settings_location_key))){
                Cursor c = getContentResolver().query(
                        WeatherContract.LocationEntry.CONTENT_URI,
                    null,
                    WeatherContract.LocationEntry._ID + "= ?",
                    new String[]{String.valueOf(sharedPreferences.getString("location", ""))},
                    null
                );
                if(c != null){
                    int idx = c.getColumnIndex(WeatherContract.LocationEntry.COLUMN_CITY_NAME);
                        if(c.moveToFirst()){
                            cityName = c.getString(idx);
                            preference.setSummary(cityName);
                        }
                    c.close();
                }
            return;
            }
            preference.setSummary((sharedPreferences.getString(key, "")));
            Log.d(LOG_TAG, "Settings changed");
        }

    }
}