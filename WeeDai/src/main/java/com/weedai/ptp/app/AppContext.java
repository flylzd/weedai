package com.weedai.ptp.app;


import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.weedai.ptp.R;
import com.weedai.ptp.utils.NetworkStateManager;
import com.weedai.ptp.volley.VolleySingleton;


public class AppContext extends Application {

    private static AppContext mInstance;
    private static Context mAppContext;


    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        init();
        initImageLoader(this);
    }


    public static AppContext getInstance() {
        return mInstance;
    }

    private void init() {
        NetworkStateManager.instance().init(this);
        VolleySingleton.init(this);
    }

    public static void initImageLoader(Context context) {

        DisplayImageOptions displayOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_launcher)
                        // 设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.ic_launcher)
                        // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.ic_launcher)
                        // 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.ARGB_8888).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(displayOptions).writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
    }


}
