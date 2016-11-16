package ua.in.quireg.sunshine_mine.ui;

import android.app.Application;
import android.content.Context;

/**
 * Created by Arcturus on 11/15/2016.
 */

public class ThisApplication extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        ThisApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return ThisApplication.context;
    }
}
