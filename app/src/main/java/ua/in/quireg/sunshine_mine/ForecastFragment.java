package ua.in.quireg.sunshine_mine;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.System.exit;

/**
 * Created by Artur Menchenko on 10/3/2016.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ForecastFragment extends Fragment {

    private RetrieveWeatherInBackground task;
    private Map<String, String> requestParams;

    public ForecastFragment() {
    }

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
                task = new RetrieveWeatherInBackground();
                task.execute(requestParams);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        JSONArray realDataForKyivJSON;
        /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            try {
                realDataForKyivJSON = new JSONArray(task.execute(requestParams));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/

        List<String> realDataForKyiv;

        String[] fakeData = {
                "Thursday - 8 June - 24 deg",
                "Friday - 9 June - 25 deg",
                "Saturday - 10 June - 26 deg",
                "Sunday - 11 June - 27 deg",
                "Monday - 12 June - 28 deg",
                "Tuesday - 13 June - 29 deg",
                "Wednesday - 14 June - 30 deg",
                "Thursday - 8 June - 24 deg",
                "Friday - 9 June - 25 deg",
                "Saturday - 10 June - 26 deg",
                "Sunday - 11 June - 27 deg",
                "Monday - 12 June - 28 deg",
                "Tuesday - 13 June - 29 deg",
                "Wednesday - 14 June - 30 deg",
                "Thursday - 8 June - 24 deg",
                "Friday - 9 June - 25 deg",
                "Saturday - 10 June - 26 deg",
                "Sunday - 11 June - 27 deg",
                "Monday - 12 June - 28 deg",
                "Tuesday - 13 June - 29 deg",
                "Wednesday - 14 June - 30 deg",
        };

        List<String> arrayListOfFakeData = new ArrayList<>(Arrays.asList(fakeData));


        ListView lv = (ListView) rootView.findViewById(R.id.listview_forecast);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.list_item_forecast,
                arrayListOfFakeData);
        lv.setAdapter(arrayAdapter);
        return rootView;

    }
    private void initializeWeatherParameters(){
        requestParams = new HashMap<>();
        requestParams.put("cityID", "703448");
        requestParams.put("numberOfDays", "7");
        requestParams.put("units", "metric");
        requestParams.put("mode", "json");
    }

    private class RetrieveWeatherInBackground extends AsyncTask<Map<String, String>, Void, String> {
        @Override
        protected String doInBackground(Map<String, String>... params) {
            return GrabWeatherAPIData.grabData(params[0]);
        }
    }

}