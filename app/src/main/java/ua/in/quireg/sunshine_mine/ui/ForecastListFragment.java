package ua.in.quireg.sunshine_mine.ui;

import android.content.Intent;
import android.content.SharedPreferences;
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

    private static final String LOG_TAG = ForecastListFragment.class.getSimpleName();

    private Map<String, String> requestParams;
    private ArrayAdapter<String> arrayAdapter;
    SharedPreferences pref;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        initializeWeatherParameters();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.action_refresh:
                refreshForecast();
                return true;

            case R.id.exit_action:
                System.exit(0);
                return true;

            case R.id.main_settings:
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.forecast_list_fragment, container, false);
        ListView lv = (ListView) rootView.findViewById(R.id.listview_forecast);
        arrayAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.list_item_forecast,
                new ArrayList<>(Arrays.asList("No Data To Display")));
        lv.setAdapter(arrayAdapter);
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
        SwipeRefreshLayout srl = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_forecast_list);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(LOG_TAG, "onRefresh called from SwipeRefreshLayout");
                refreshForecast();
            }
        });

        return rootView;
    }

    private void initializeWeatherParameters(){
        //TODO get rid of this method.
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        requestParams = new HashMap<>();

        //get location from params
        requestParams.put(WeatherAPIParams.CITY_ID, pref.getString(getString(R.string.settings_location_key),""));
        requestParams.put(WeatherAPIParams.ZIP_CODE, "01032");
        requestParams.put(WeatherAPIParams.DAYS_COUNT, pref.getString(getString(R.string.settings_dayscount_key),""));
        requestParams.put(WeatherAPIParams.UNITS, pref.getString(getString(R.string.settings_units_key),""));
        requestParams.put(WeatherAPIParams.OUTPUT_MODE, "json");
    }



    private class RetrieveWeatherInBackground extends AsyncTask<Map<String, String>, Void, String[]> {
        @Override
        protected void onPostExecute(String[] strings) {
            List<String> arrayListOfData = new ArrayList<>(Arrays.asList(strings));
            arrayAdapter.clear();
            for (int i = 0; i < arrayListOfData.size(); i++) {
                arrayAdapter.insert(arrayListOfData.get(i), i);
            }
            arrayAdapter.notifyDataSetChanged();
        }

        @Override
        protected String[] doInBackground(Map<String, String>... params) {
            try {
                String JSONData = GrabWeatherAPIData.grabData(params[0]);
                WeatherDataParser wdp = new WeatherDataParser();
                String[] result = wdp.getWeatherDataFromJson(JSONData);
                if(result != null){
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
    }

    private void refreshForecast(){
        RetrieveWeatherInBackground task = new RetrieveWeatherInBackground();
        initializeWeatherParameters();
        task.execute(requestParams);
    }


}