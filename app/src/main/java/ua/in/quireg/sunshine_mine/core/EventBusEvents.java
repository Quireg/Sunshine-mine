package ua.in.quireg.sunshine_mine.core;

import android.content.Context;
import android.widget.TextView;


public class EventBusEvents {

    public static class LocationTextViewUpdated{
        Context context;
        TextView textView;

        public LocationTextViewUpdated(Context context, TextView textView) {
            this.context = context;
            this.textView = textView;
        }
    }

    public static class LocationListUpdated{}

}
