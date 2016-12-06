package ua.in.quireg.sunshine_mine.ui;

/**
 * Created by Arcturus on 11/22/2016.
 */

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;

import ua.in.quireg.sunshine_mine.R;
import ua.in.quireg.sunshine_mine.core.LocationListGeneratorForRecycleView;
import ua.in.quireg.sunshine_mine.core.base_objects.Location;

public class ActivityLocationSettings extends PreferenceActivity implements FragmentLocationSettings.OnFragmentInteractionListener{
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
    public void onFragmentInteraction(Location loc) {
        finish();
    }
}