package ua.in.quireg.sunshine_mine.core;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

/**
 * Created by Arcturus on 11/23/2016.
 */

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
