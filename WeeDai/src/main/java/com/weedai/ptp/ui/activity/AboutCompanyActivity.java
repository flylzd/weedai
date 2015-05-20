package com.weedai.ptp.ui.activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.constant.Urls;
import com.weedai.ptp.model.About;
import com.weedai.ptp.utils.DataUtil;
import com.weedai.ptp.volley.ResponseListener;

public class AboutCompanyActivity extends BaseActivity {


    private final static String TAG = "AboutCompanyActivity";

    private WebView webView;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_company);

        initView();
        loadData();
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_about_company;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    private void initView() {

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
        webView.setWebViewClient(webViewClient);
    }

    private void loadData() {
        getAboutCompany();
    }

    private void getAboutCompany() {

        ApiClient.getAboutCompany(TAG, new ResponseListener() {
            @Override
            public void onStarted() {
                progressDialog = ProgressDialog.show(AboutCompanyActivity.this, null, getString(R.string.message_waiting));
            }

            @Override
            public void onResponse(Object response) {
                progressDialog.dismiss();

                About result = (About) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(AboutCompanyActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (result.message.equals("aboutus_list_succ")) {
                    String htmlString = DataUtil.urlDecode(result.data.content);

                    System.out.println("htmlString " + htmlString);

//                    webView.loadData(htmlString, "text/html; charset=utf-8", "utf-8");

                                    webView.loadDataWithBaseURL(Urls.SERVER_URL, htmlString, "text/html", "utf-8", null);

                } else {
                    Toast.makeText(AboutCompanyActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
            }
        });
    }
}


