package com.weedai.ptp.ui.activity;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ListView;

import com.android.volley.error.VolleyError;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.volley.ResponseListener;

public class HeroActivity extends BaseActivity {

    private final static String TAG = "HeroActivity";

    private ListView listView;

    private int dact = 0; //1. 上星期
                          // 0. 本星期

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero);

        initView();
        loadData();
    }

    protected boolean hasBackButton() {
        return true;
    }

    private void initView() {
    }

    private void loadData() {

        ApiClient.getHeroList(TAG, dact, new RefreshResponseListener() {
            @Override
            public void onResponse(Object response) {
                super.onResponse(response);


            }
        });

    }

    private abstract class RefreshResponseListener implements ResponseListener {

        @Override
        public void onStarted() {
            progressDialog = ProgressDialog.show(HeroActivity.this, null, getString(R.string.message_waiting));
        }

        @Override
        public void onResponse(Object response) {
            progressDialog.dismiss();
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            progressDialog.dismiss();
        }
    }
}
