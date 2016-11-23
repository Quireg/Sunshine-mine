package ua.in.quireg.sunshine_mine.core;

import android.database.Cursor;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ua.in.quireg.sunshine_mine.core.base_objects.Location;
import ua.in.quireg.sunshine_mine.data.WeatherContract;
import ua.in.quireg.sunshine_mine.data.WeatherProvider;
import ua.in.quireg.sunshine_mine.ui.ThisApplication;

/**
 * Created by Arcturus on 11/23/2016.
 */

public class LocationListGeneratorForRecycleView {
    private static final String LOG_TAG = LocationListGeneratorForRecycleView.class.getSimpleName();

    private static LocationListGeneratorForRecycleView mInstance;
    public static List<Location> location_list = Collections.synchronizedList(new ArrayList<Location>());

    private LocationListGeneratorForRecycleView(){
        EventBus.getDefault().register(this);
    }

    public static LocationListGeneratorForRecycleView getLocationListGeneratorForRecycleView(){
        if (mInstance == null) {
            mInstance = new LocationListGeneratorForRecycleView();
            Log.d(LOG_TAG, "created");
        }
        return mInstance;
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onMessageEvent(EventBusEvents.LocationTextViewUpdated event) {
        location_list.clear();
        String locationText = event.textView.getText().toString();
        if(locationText.isEmpty()) {
            //No need to fetch anything.
            //TODO implement previous picked locations cache.
            return;
        }
        Cursor c =  event.context.getContentResolver().query(WeatherContract.LocationEntry.CONTENT_URI, null, WeatherContract.LocationEntry.COLUMN_CITY_NAME + " LIKE ?", new String[]{locationText + "%"}, null);


        if (c != null ) {
            if  (c.moveToFirst()) {
                do {
                    Location loc = new Location(
                            c.getLong(c.getColumnIndex(WeatherContract.LocationEntry._ID)),
                            c.getString(c.getColumnIndex(WeatherContract.LocationEntry.COLUMN_CITY_NAME)),
                            c.getDouble(c.getColumnIndex(WeatherContract.LocationEntry.COLUMN_CITY_LAT)),
                            c.getDouble(c.getColumnIndex(WeatherContract.LocationEntry.COLUMN_CITY_LON)),
                            c.getString(c.getColumnIndex(WeatherContract.LocationEntry.COLUMN_LOC_COUNTRYCODE))
                    );
                    location_list.add(loc);
                    Log.d(LOG_TAG, loc.toString());
                }while (c.moveToNext());
            }
            c.close();
            EventBus.getDefault().post(new EventBusEvents.LocationListUpdated());
        }else{
            Log.e(LOG_TAG, "Empty Cursor!");
        }

    };

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        EventBus.getDefault().unregister(this);
    }
}
