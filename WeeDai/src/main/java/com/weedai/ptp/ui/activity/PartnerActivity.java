package com.weedai.ptp.ui.activity;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;


import com.android.volley.error.VolleyError;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Config;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.constant.Urls;
import com.weedai.ptp.model.About;
import com.weedai.ptp.model.BaseModel;
import com.weedai.ptp.utils.DataUtil;
import com.weedai.ptp.volley.ResponseListener;

public class PartnerActivity extends BaseActivity {

    private final static String TAG = "PartnerActivity";

    private WebView webView;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner);

        initView();
        loadData();
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_partner;
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
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
    }

    private void loadData() {
        getPartner();
    }

    private void getPartner() {

        ApiClient.getPartner(TAG, new ResponseListener() {
            @Override
            public void onStarted() {
                progressDialog = ProgressDialog.show(PartnerActivity.this, null, getString(R.string.message_waiting));
            }

            @Override
            public void onResponse(Object response) {
                progressDialog.dismiss();

                About result = (About) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(PartnerActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (result.message.equals("friends_list_succ")) {

                    String htmlString = DataUtil.urlDecode(result.data.content);
                    System.out.println("htmlString " + htmlString);
//                    webView.loadData(htmlString, "text/html; charset=utf-8", "utf-8");

                    webView.loadDataWithBaseURL(Urls.SERVER_URL + "/", htmlString, "text/html", "utf-8", null);

                } else {
                    Toast.makeText(PartnerActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
            }
        });

    }
}


