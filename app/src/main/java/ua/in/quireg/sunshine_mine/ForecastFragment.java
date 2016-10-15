package ua.in.quireg.sunshine_mine;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

/**
 * Created by Artur Menchenko on 10/3/2016.
 */

public class ForecastFragment extends Fragment {

    private Map<String, String> requestParams;
    private ArrayAdapter<String> arrayAdapter;

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
        System.out.println(item.getItemId());
        switch (item.getItemId()) {

            case R.id.action_refresh:
                RetrieveWeatherInBackground task = new RetrieveWeatherInBackground();
                task.execute(requestParams);
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
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
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

        return rootView;
    }

    private void initializeWeatherParameters(){
        requestParams = new HashMap<>();
        requestParams.put("cityID", "703448");
        requestParams.put("cityPostCode", "01032");
        requestParams.put("numberOfDays", "15");
        requestParams.put("units", "metric");
        requestParams.put("mode", "json");
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
                return wdp.getWeatherDataFromJson(JSONData);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}