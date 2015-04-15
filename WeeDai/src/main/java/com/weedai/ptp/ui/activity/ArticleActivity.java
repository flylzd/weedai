package com.weedai.ptp.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.lemon.aklib.adapter.BaseAdapterHelper;
import com.lemon.aklib.adapter.QuickAdapter;
import com.lemon.aklib.widget.EndOfListView;
import com.lemon.aklib.widget.PMSwipeRefreshLayout;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.model.Article;
import com.weedai.ptp.model.ArticleData;
import com.weedai.ptp.volley.ResponseListener;

import java.util.ArrayList;
import java.util.List;


public class ArticleActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private final static String TAG = "ArticleActivity";

    private PMSwipeRefreshLayout pullRefreshLayout;
    private EndOfListView listView;
    private QuickAdapter<ArticleData> adapter;
    private List<ArticleData> dataList = new ArrayList<ArticleData>();

    private int page = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        initView();
        loadData();
    }

    protected boolean hasBackButton() {
        return true;
    }

    @Override
    public void onRefresh() {
        getArticleList();
    }

    private void initView() {

        pullRefreshLayout = (PMSwipeRefreshLayout) findViewById(R.id.pullRefreshLayout);
        pullRefreshLayout.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        pullRefreshLayout.setOnRefreshListener(this);
        pullRefreshLayout.setRefreshing(true);
        listView = (EndOfListView) findViewById(R.id.listView);

        adapter = new QuickAdapter<ArticleData>(ArticleActivity.this, R.layout.listitem_article, dataList) {
            @Override
            protected void convert(BaseAdapterHelper helper, ArticleData item) {

                helper.setText(R.id.tvArticleTitle, item.name);
                helper.setText(R.id.tvArticleSubTitle, item.summary);
                helper.setText(R.id.tvArticleDate, item.publish);
                helper.setText(R.id.tvComments, item.comment);
            }
        };
        listView.setAdapter(adapter);

    }

    private void loadData() {
        getArticleList();
    }

    private void getArticleList() {

        ApiClient.getArticleList(TAG, page, new RefreshResponseListener() {

            @Override
            public void onResponse(Object response) {
                super.onResponse(response);

                Article result = (Article) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(ArticleActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }
                adapter.replaceAll(result.data);
            }
        });
    }


    private abstract class RefreshResponseListener implements ResponseListener {

        @Override
        public void onStarted() {

            if (!pullRefreshLayout.isRefreshing()) {
                pullRefreshLayout.setRefreshing(true);
            }
        }

        @Override
        public void onResponse(Object response) {
            pullRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            pullRefreshLayout.setRefreshing(false);
        }
    }

}
