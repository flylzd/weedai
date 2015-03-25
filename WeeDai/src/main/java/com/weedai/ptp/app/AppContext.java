package com.weedai.ptp.app;


import android.app.Application;
import android.content.Context;

import com.weedai.ptp.volley.VolleySingleton;


public class AppContext extends Application {

    private static AppContext mInstance;
    private static Context mAppContext;


    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        init();
    }


    public static AppContext getInstance() {
        return mInstance;
    }

    private void init() {
        VolleySingleton.init(this);
    }


}
