package ua.in.quireg.sunshine_mine.ui;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ua.in.quireg.sunshine_mine.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentWeatherDetail extends Fragment {

    private final static String LOG_TAG = FragmentWeatherDetail.class.getSimpleName();

    public FragmentWeatherDetail() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, "Fragment created");
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_detail, container, false);
        TextView tv = (TextView) view.findViewById(R.id.detailFragmentMainTextView);
        if (tv != null) {
            tv.setText(getActivity().getIntent().getStringExtra("weather"));
        }
        return view;
    }


}
