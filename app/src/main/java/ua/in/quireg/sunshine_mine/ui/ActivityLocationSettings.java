package ua.in.quireg.sunshine_mine.ui;

/**
 * Created by Arcturus on 11/22/2016.
 */

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;

import ua.in.quireg.sunshine_mine.core.LocationListGeneratorForRecycleView;
import ua.in.quireg.sunshine_mine.core.models.LocationModel;

public class ActivityLocationSettings extends PreferenceActivity implements FragmentLocationSettings.OnFragmentInteractionListener,
        Preference.OnPreferenceChangeListener{
    private static final String TAG = ActivityLocationSettings.class.getSimpleName();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate hasHeaders: " + hasHeaders());


        LocationListGeneratorForRecycleView l = LocationListGeneratorForRecycleView.getLocationListGeneratorForRecycleView();

        getFragmentManager().beginTransaction()
                .add(android.R.id.content,
                new FragmentLocationSettings())
                .commit();
    }

    @Override
    public void onFragmentInteraction(LocationModel loc) {
        finish();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }
}