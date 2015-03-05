package com.weedai.p2p.app;


import android.app.Application;
import android.content.Context;

import com.weedai.p2p.volley.VolleySingleton;


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
