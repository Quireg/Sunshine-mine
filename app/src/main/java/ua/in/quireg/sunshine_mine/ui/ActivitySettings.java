package ua.in.quireg.sunshine_mine.ui;

/**
 * Created by Artur Menchenko on 10/15/2016.
 */

import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.Menu;

import ua.in.quireg.sunshine_mine.R;
import ua.in.quireg.sunshine_mine.core.base_objects.Location;

/**
 * A {@link PreferenceActivity} that presents a set of application settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class ActivitySettings extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener, FragmentLocationSettings.OnFragmentInteractionListener{
    private static final String LOG_TAG = ActivitySettings.class.getSimpleName();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "Settings launched");
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "started");
        addPreferencesFromResource(R.xml.pref_general);


        // For all preferences, attach an OnPreferenceChangeListener so the UI summary can be
        // updated when the preference changes.
        bindPreferenceSummaryToValue(findPreference(getString(R.string.settings_dayscount_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.settings_units_key)));
    }

    /**
     * Attaches a listener so the summary is always updated with the preference value.
     * Also fires the listener once, to initialize the summary (so it shows up before the value
     * is changed.)
     */
    private void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(this);

        // Trigger the listener immediately with the preference's
        // current value.
        onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == (PreferenceScreen) findPreference("location")){
            Intent intent = new Intent(this, ActivityLocationSettings.class);
            startActivity(intent);
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        String stringValue = value.toString();

//        if (preference.getKey().equals("location")){
//            getFragmentManager()
//                    .beginTransaction()
//                    .add(R.id.settings_container, new FragmentLocationSettings())
//                    .addToBackStack("setting")
//                    .commit();
//
//            return true;
//        }

        if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list (since they have separate labels/values).
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else {
            // For other preferences, set the summary to the value's simple string representation.
            preference.setSummary(stringValue);
            Log.d(LOG_TAG, "Settings changed");
        }
        return true;
    }

    @Override
    public void onFragmentInteraction(Location loc) {

    }
}