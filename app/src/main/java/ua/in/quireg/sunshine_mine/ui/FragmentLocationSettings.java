package ua.in.quireg.sunshine_mine.ui;

import android.content.Context;
import android.os.Bundle;

import android.preference.PreferenceFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;

import ua.in.quireg.sunshine_mine.R;
import ua.in.quireg.sunshine_mine.core.EventBusEvents;
import ua.in.quireg.sunshine_mine.core.models.LocationModel;

public class FragmentLocationSettings extends PreferenceFragment implements TextWatcher{

    private final static String LOG_TAG = FragmentLocationSettings.class.getSimpleName();

    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private EditText editText;
    public FragmentLocationSettings() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_location_settings, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_settings_location);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setAdapter(new LocationSettingsRecycleViewAdapter(mListener));
        Log.d(LOG_TAG, recyclerView.getAdapter().toString());

        editText = (EditText) view.findViewById(R.id.location_settings_search_text);
        editText.addTextChangedListener(this);


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
         EventBus.getDefault().post(new EventBusEvents.LocationTextViewUpdated(getActivity().getApplicationContext(), editText));
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(LocationModel loc);
    }
}
