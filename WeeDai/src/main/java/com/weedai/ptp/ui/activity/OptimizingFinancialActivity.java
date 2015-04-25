package com.weedai.ptp.ui.activity;


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.lemon.aklib.adapter.BaseAdapterHelper;
import com.lemon.aklib.adapter.QuickAdapter;
import com.lemon.aklib.widget.EndOfListView;
import com.lemon.aklib.widget.PMSwipeRefreshLayout;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.model.Invest;
import com.weedai.ptp.model.InvestList;
import com.weedai.ptp.view.NumberCircleProgressBar;
import com.weedai.ptp.volley.ResponseListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class OptimizingFinancialActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, EndOfListView.OnEndOfListListener {

    private final static String TAG = "OptimizingFinancialActivity";

    private PMSwipeRefreshLayout pullRefreshLayout;
    private EndOfListView listView;
    private QuickAdapter<InvestList> adapter;
    private List<InvestList> dataList = new ArrayList<InvestList>();

    private final static int DEFAULT_PAGE = 1;
    private int page = DEFAULT_PAGE;

    private boolean isFirstLoadingomplete = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_optimizing_financial);

        initView();
        loadData();

    }

    protected boolean hasBackButton() {
        return true;
    }

    @Override
    public void onRefresh() {

        page = DEFAULT_PAGE;
        getInvestList();
    }

    @Override
    public void onEndOfList(Object lastItem) {

        if (isFirstLoadingomplete) {
            showIndeterminateProgress(true);
            page++;
            getInvestList();
        }
    }

    private void showIndeterminateProgress(boolean visibility) {
        adapter.showIndeterminateProgress(visibility);
    }


    private void initView() {

        pullRefreshLayout = (PMSwipeRefreshLayout) findViewById(R.id.pullRefreshLayout);
        pullRefreshLayout.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        pullRefreshLayout.setOnRefreshListener(this);
        pullRefreshLayout.setRefreshing(true);

        listView = (EndOfListView) findViewById(R.id.listView);
        listView.setOnEndOfListListener(this);

        adapter = new QuickAdapter<InvestList>(OptimizingFinancialActivity.this, R.layout.listitem_optimizing_financial) {
            @Override
            protected void convert(BaseAdapterHelper helper, InvestList item) {

//            String apr = item.apr;
//                String apr = getString(R.string.financial_annual_rate)

//                String apr = String.format(getString(R.string.financial_annual_rate), item.apr);
//                String timeLimit = String.format(getString(R.string.financial_deadline), item.time_limit);
//                String amount = String.format(getString(R.string.financial_amount), item.account);
//                String reward = String.format(getString(R.string.financial_reward), item.award);
//                helper.setText(R.id.tvAnnualRate, Html.fromHtml(apr).toString());
//                helper.setText(R.id.tvDeadline, timeLimit);
//                helper.setText(R.id.tvAmount, amount);
//                helper.setText(R.id.tvReward, reward);
//
//                Button btnStatus = helper.getView(R.id.btnState);
//
//                int status = item.status;
                final float scale = item.scale;
//                if (status == 1) {
//                    if (scale == 100) {
//                        btnStatus.setText(getString(R.string.financial_btn_have_full));
//                    } else {
//                        btnStatus.setText(getString(R.string.financial_btn_join));
//                    }
//                } else {
//                    if (item.repayment_account == item.repayment_yesaccount) {
//                        btnStatus.setText(getString(R.string.financial_btn_completed));
//                    } else {
//                        btnStatus.setText(getString(R.string.financial_btn_payment));
//                    }
//                }

                final NumberCircleProgressBar numberCircleProgressBar = helper.getView(R.id.numberCircleProgress);
                numberCircleProgressBar.setProgress((int) scale);
//                numberCircleProgressBar.incrementProgressBy((int) scale);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        numberCircleProgressBar.setProgress((int) scale);
//                        numberCircleProgressBar.postInvalidate();
//                    }
//                });
            }
        };
        listView.setAdapter(adapter);

    }

    private void loadData() {
        getInvestList();
    }

    private void getInvestList() {

        ApiClient.getInvestList(TAG, page, Constant.InvestType.TYPE_YX, new RefreshResponseListener() {
            @Override
            public void onResponse(Object response) {
                super.onResponse(response);

                Invest result = (Invest) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(OptimizingFinancialActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }

                List<InvestList> investList = result.data.list;
                if (page == DEFAULT_PAGE) {
                    dataList = investList;
                    adapter.replaceAll(dataList);
                } else {
                    dataList.addAll(investList);
                    adapter.replaceAll(dataList);
                }
                isFirstLoadingomplete = true;
            }
        });

    }

    private abstract class RefreshResponseListener implements ResponseListener {

        @Override
        public void onStarted() {

            if (page != DEFAULT_PAGE) {
                return;
            }
            if (!pullRefreshLayout.isRefreshing()) {
                pullRefreshLayout.setRefreshing(true);
            }
        }

        @Override
        public void onResponse(Object response) {

            if (page != DEFAULT_PAGE) {
//                showIndeterminateProgress(false);
                return;
            }
            pullRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            pullRefreshLayout.setRefreshing(false);
        }
    }


}
