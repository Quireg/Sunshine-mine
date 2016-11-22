package ua.in.quireg.sunshine_mine.ui;

/**
 * Created by Arcturus on 11/22/2016.
 */

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;

import ua.in.quireg.sunshine_mine.R;

public class ActivityLocationSettings extends PreferenceActivity {
    private static final String TAG = ActivityLocationSettings.class.getSimpleName();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate hasHeaders: " + hasHeaders());
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new ActivityLocationFragment()).commit();
    }
    /*
        public void onBuildHeaders(List<Header> target) {
            Log.i(TAG, "onBuildHeaders before load");
            loadHeadersFromResource(R.xml.preference_headers, target);
            Log.i(TAG, "onBuildHeaders after load");
        }
    */
    public static class ActivityLocationFragment extends PreferenceFragment {
        private final static String TAG = ActivityLocationFragment.class.getSimpleName();

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Log.i(TAG, "fragment onCreate");
            //addPreferencesFromResource(R.xml.pref_general);
        }
    }
}