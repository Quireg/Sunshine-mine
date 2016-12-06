package ua.in.quireg.sunshine_mine.ui;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.List;

import ua.in.quireg.sunshine_mine.R;
import ua.in.quireg.sunshine_mine.core.EventBusEvents;
import ua.in.quireg.sunshine_mine.core.LocationListGeneratorForRecycleView;
import ua.in.quireg.sunshine_mine.core.base_objects.Location;

public class LocationSettingsRecycleViewAdapter extends RecyclerView.Adapter<LocationSettingsRecycleViewAdapter.ViewHolder> {

    private final static String LOG_TAG = LocationSettingsRecycleViewAdapter.class.getSimpleName();

    private List<Location> locations = LocationListGeneratorForRecycleView.location_list;
    private final FragmentLocationSettings.OnFragmentInteractionListener mListener;
    private Context appContext;
    private SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;


    public LocationSettingsRecycleViewAdapter(FragmentLocationSettings.OnFragmentInteractionListener listener) {
        mListener = listener;
        EventBus.getDefault().register(this);
    }

    @Override
    public LocationSettingsRecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        appContext = parent.getContext();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(appContext);

        View view = LayoutInflater.from(appContext)
                .inflate(R.layout.fragment_location_settings_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final LocationSettingsRecycleViewAdapter.ViewHolder holder, int position) {
        holder.mItem = locations.get(position);
        holder.mIdView.setText(locations.get(position).getName() + " -//- "+ locations.get(position).getCountryCode());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onFragmentInteraction(holder.mItem);
                    editor = sharedPreferences.edit();
                    if(editor != null) {
                        editor.putString("location", String.valueOf(holder.mItem.getId()));
                        editor.commit();
                    }else{
                        Log.e(LOG_TAG, "Cannot obtain preferences");
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public Location mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.location_settings_item_textview);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mIdView.getText() + "'";
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onMessageEvent(EventBusEvents.LocationListUpdated event) {
        this.notifyDataSetChanged();
    };

    @Override
    protected void finalize() throws Throwable {
        EventBus.getDefault().unregister(this);
        super.finalize();
    }
}
