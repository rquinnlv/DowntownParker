package com.zappos.downtown.parker;

import android.app.Application;
import android.content.Context;

/**
 * Master application layer
 */
public class ParkingApp extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext() {
        return mContext;
    }
}
