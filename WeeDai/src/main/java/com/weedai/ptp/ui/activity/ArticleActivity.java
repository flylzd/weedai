package com.weedai.ptp.ui.activity;

import android.os.Bundle;

import com.android.volley.error.VolleyError;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.volley.ResponseListener;


public class ArticleActivity extends BaseActivity {

    private final static String TAG = "ArticleActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        loadData();
    }

    protected boolean hasBackButton() {
        return true;
    }

    private void loadData() {
        getArticleList();
    }

    private void getArticleList() {

        ApiClient.getArticleList(TAG, new ResponseListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onResponse(Object response) {

            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
    }

}
