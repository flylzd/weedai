package com.weedai.ptp.ui.activity;


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
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
import com.weedai.ptp.utils.DataUtil;
import com.weedai.ptp.utils.UIHelper;
import com.weedai.ptp.view.SimpleWaveView;
import com.weedai.ptp.volley.ResponseListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class LiCaiRecommendActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, EndOfListView.OnEndOfListListener {

    private final static String TAG = "LiCaiRecommendActivity";

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
        setContentView(R.layout.activity_financial);

        initView();
        loadData();

    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_licai_recommend;
    }

    @Override
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

        adapter = new QuickAdapter<InvestList>(LiCaiRecommendActivity.this, R.layout.listitem_financial) {
            @Override
            protected void convert(BaseAdapterHelper helper, final InvestList item) {

                String apr = String.format(getString(R.string.financial_annual_rate), item.apr);
                String timeLimit = String.format(getString(R.string.financial_deadline), item.time_limit);
                String amount = String.format(getString(R.string.financial_amount), item.account);
                String reward;
                if (item.award.equals("0")) {
                    reward = getString(R.string.financial_reward_empty);
                } else {
                    reward = String.format(getString(R.string.financial_reward), item.award);
                }
                helper.setText(R.id.tvAnnualRate, Html.fromHtml(apr));
                helper.setText(R.id.tvDeadline, timeLimit);
                helper.setText(R.id.tvAmount, amount);
                helper.setText(R.id.tvReward, reward);
                helper.setText(R.id.tvTitle, DataUtil.urlDecode(item.name));

                String addTime = item.addtime + "000";
                SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 hh:mm");
                String stateTime = sdf.format(Long.parseLong(addTime));

                View layoutProgress = helper.getView(R.id.layoutProgress);
                View imgCompleted = helper.getView(R.id.imgCompleted);
                View imgPayment = helper.getView(R.id.imgPayment);
                layoutProgress.setVisibility(View.GONE);
                imgCompleted.setVisibility(View.GONE);
                imgPayment.setVisibility(View.GONE);

                Button btnStatus = helper.getView(R.id.btnState);
                TextView tvState = helper.getView(R.id.tvState);

                String statusStr;
                String statusHint;
                int status = item.status;
                final float scale = item.scale;
                if (status == 1) {
                    if (scale == 100) {
                        statusStr = getString(R.string.financial_btn_have_full);
                        statusHint = stateTime + "已满额";
                        layoutProgress.setVisibility(View.VISIBLE);
                    } else {
                        statusStr = getString(R.string.financial_btn_join);
                        statusHint = stateTime + "开始竞标";
                        layoutProgress.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (item.repayment_account == item.repayment_yesaccount) {
                        statusStr = getString(R.string.financial_btn_completed);
                        statusHint = stateTime + "已完成";
                        imgCompleted.setVisibility(View.VISIBLE);
                    } else {
                        statusStr = getString(R.string.financial_btn_payment);
                        statusHint = stateTime + "复审成功";
                        imgPayment.setVisibility(View.VISIBLE);
                    }
                }
                btnStatus.setText(statusStr);
                tvState.setText(statusHint);

                helper.setText(R.id.tvScale, scale + "%");

                float percentage = scale / 100;
                SimpleWaveView simpleWaveView = helper.getView(R.id.simpleWaveView);
                simpleWaveView.setColor(getResources().getColor(R.color.main_text_orange));
                simpleWaveView.setPercentage(percentage);

                final Button btnState = helper.getView(R.id.btnState);
                helper.setOnClickListener(R.id.btnState, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (btnState.getText().toString().equals(getString(R.string.financial_btn_join))) {
                            UIHelper.showFinanceInvestment(LiCaiRecommendActivity.this, item);
                        }
                    }
                });
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                UIHelper.showFinancialDetail(FinancialActivity.this, adapter.getItem(position).id);
                UIHelper.showFinancialDetail(LiCaiRecommendActivity.this, adapter.getItem(position));
            }
        });
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
                    Toast.makeText(LiCaiRecommendActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }

                List<InvestList> investList = result.data.list;
                if (page == DEFAULT_PAGE) {
//                    dataList = investList;
                    for (InvestList item : investList) {
                        if (item.is_you != 1) {
                            dataList.add(item);
                        }
                    }
                    adapter.replaceAll(dataList);
                } else {
//                    dataList.addAll(investList);
                    for (InvestList item : investList) {
                        if (item.is_you != 1) {
                            dataList.add(item);
                        }
                    }
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
                showIndeterminateProgress(false);
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
