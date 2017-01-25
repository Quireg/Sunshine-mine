package ua.in.quireg.sunshine_mine.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.Toast;

import ua.in.quireg.sunshine_mine.R;

/**
 * Created by Artur Menchenko on 10/3/2016.
 */

public class FragmentForecastList extends Fragment{

    private static final String LOG_TAG = FragmentForecastList.class.getSimpleName();

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
        View rootView = inflater.inflate(R.layout.fragment_forecast_list, container, false);
        ListView lv = (ListView) rootView.findViewById(R.id.listview_forecast);

        lv.setAdapter(((ActivityMain)getActivity()).getArrayAdapter());

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast toast = Toast.makeText(getContext(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT);
                toast.show();

                Intent newIntent = new Intent(getContext(), ActivityWeatherDetail.class);
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
                Intent intent = new Intent(getContext(), ActivitySettings.class);
                startActivity(intent);
                return true;

            case R.id.action_show_on_map:
                ((ActivityMain)getActivity()).showPrefLocOnMap();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void refreshRequested(){
        //TODO get latest data from DB
    }


    public void refreshColmpleted(){
        if(srl != null) srl.setRefreshing(false);
    }



}