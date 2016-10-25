package ua.in.quireg.sunshine_mine.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ua.in.quireg.sunshine_mine.core.GrabWeatherAPIData;
import ua.in.quireg.sunshine_mine.R;
import ua.in.quireg.sunshine_mine.core.WeatherAPIParams;
import ua.in.quireg.sunshine_mine.core.WeatherDataParser;

/**
 * Created by Artur Menchenko on 10/3/2016.
 */

public class ForecastListFragment extends Fragment{
    //log tag
    private static final String LOG_TAG = ForecastListFragment.class.getSimpleName();

    //retrieve arguments passed from activity
    Bundle args = getArguments();

    private String[] forecast;

    SharedPreferences pref;
    SwipeRefreshLayout srl;

    @Override
    public void onStart() {
        if(forecast == null) {
            refreshRequested();
        }
        super.onStart();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.forecast_list_fragment, container, false);
        ListView lv = (ListView) rootView.findViewById(R.id.listview_forecast);

        lv.setAdapter(((MainActivity)getActivity()).getArrayAdapter());

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast toast = Toast.makeText(getContext(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT);
                toast.show();

                Intent newIntent = new Intent(getContext(), DetailActivity.class);
                newIntent.putExtra("weather", parent.getItemAtPosition(position).toString());
                startActivity(newIntent);

            }
        });
        srl = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_forecast_list);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(LOG_TAG, "onRefresh called from SwipeRefreshLayout");
                refreshRequested();

            }
        });

        return rootView;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.action_refresh:
                refreshRequested();
                return true;

            case R.id.exit_action:
                System.exit(0);
                return true;

            case R.id.main_settings:
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_show_on_map:
                ((MainActivity)getActivity()).showPrefLocOnMap();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void refreshRequested(){
        ((MainActivity)getActivity()).refreshForecast();
    }


    public void refreshColmpleted(){
        if(srl != null) srl.setRefreshing(false);
    }



}