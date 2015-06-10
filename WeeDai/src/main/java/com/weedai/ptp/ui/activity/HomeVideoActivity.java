package com.weedai.ptp.ui.activity;


import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.weedai.ptp.R;
import com.weedai.ptp.constant.Urls;


import java.lang.reflect.InvocationTargetException;

public class HomeVideoActivity extends Activity {

//    private WebView webView;

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_home_video);

        initView();
        loadData();
    }

//    @Override
//    protected int getActionBarTitle() {
//        return R.string.title_financial_detail;
//    }
//
//    @Override
//    protected boolean hasBackButton() {
//        return true;
//    }


    @Override
    protected void onPause() {
        super.onPause();
//        try {
//            webView.getClass().getMethod("onPause").invoke(webView,(Object[])null);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();

//        try {
//            webView.getClass().getMethod("onResume").invoke(webView,(Object[])null);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        }
    }

    private void initView() {

//        webView = (WebView) findViewById(R.id.webView);
//        // 设置WebView属性，能够执行Javascript脚本
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
////        webView.setWebChromeClient(new WebChromeClient());
//
//        webView.loadUrl(Urls.VIDEO_URL);

        videoView = (VideoView) findViewById(R.id.videoView);

        videoView.setMediaController(new MediaController(this));

        videoView.setVideoURI(Uri.parse(Urls.VIDEO_URL));

        videoView.start();

        videoView.requestFocus();

    }

    private void loadData() {

    }
}
