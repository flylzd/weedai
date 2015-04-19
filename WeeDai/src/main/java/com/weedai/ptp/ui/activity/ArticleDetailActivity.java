package com.weedai.ptp.ui.activity;


import android.os.Bundle;

import com.android.volley.error.VolleyError;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.volley.ResponseListener;

public class ArticleDetailActivity extends BaseActivity {

    private final static String TAG = "ArticleDetailActivity";

    private String aid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        if (getIntent().hasExtra("aid")) {
            aid = getIntent().getStringExtra("aid");
        }

        initView();
        loadData();
    }

    protected boolean hasBackButton() {
        return true;
    }

    private void initView() {

    }

    private void loadData() {
        getArticleDetail();
    }

    private void getArticleDetail() {

        ApiClient.getArticleDetail(TAG, aid, new RefreshResponseListener() {
            @Override
            public void onResponse(Object response) {
                super.onResponse(response);


            }
        });
    }

    private abstract class RefreshResponseListener implements ResponseListener {

        @Override
        public void onStarted() {


        }

        @Override
        public void onResponse(Object response) {


        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {

        }
    }


}
