package com.weedai.ptp.ui.activity;


import android.os.Build;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.weedai.ptp.R;
import com.weedai.ptp.constant.Config;

import java.util.HashMap;
import java.util.Map;

public class RechargeOnlineActivity extends BaseActivity {

    private WebView webView;
    private String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_online);

        if (getIntent().hasExtra("url")) {
            url = getIntent().getStringExtra("url");
        }

        webView = (WebView) findViewById(R.id.webView);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
            webView.getSettings().setLoadsImagesAutomatically(true);
        } else {
//            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
            webView.getSettings().setLoadsImagesAutomatically(false);
        }

        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (!webView.getSettings().getLoadsImagesAutomatically()) {
                    webView.getSettings().setLoadsImagesAutomatically(true);
                }
            }
        };
        webView.removeJavascriptInterface("searchBoxJavaBredge_");
        webView.setWebViewClient(webViewClient);


        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setCookie(url, Config.cookie);
        cookieManager.setCookie(url, Config.cookie);
        CookieSyncManager.getInstance().sync();

        webView.loadUrl(url);

//        webView.loadDataWithBaseURL(Urls.SERVER_URL, htmlString, "text/html", "utf-8", null);
    }


    private class MyWebViewClient extends WebViewClient {

    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_my_recharge_online;
    }


    @Override
    protected boolean hasBackButton() {
        return true;
    }
}
