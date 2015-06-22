package com.weedai.ptp.ui.activity;


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.lemon.aklib.adapter.BaseAdapterHelper;
import com.lemon.aklib.adapter.QuickAdapter;
import com.lemon.aklib.widget.EndOfListView;
import com.lemon.aklib.widget.PMSwipeRefreshLayout;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Config;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.model.Invest;
import com.weedai.ptp.model.InvestData;
import com.weedai.ptp.model.InvestList;
import com.weedai.ptp.utils.DataUtil;
import com.weedai.ptp.utils.UIHelper;
import com.weedai.ptp.view.SimpleWaveView;
import com.weedai.ptp.volley.ResponseListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FinancialActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, EndOfListView.OnEndOfListListener {

    private final static String TAG = "FinancialActivity";

    private PMSwipeRefreshLayout pullRefreshLayout;
    private EndOfListView listView;
    private QuickAdapter<InvestList> adapter;
    private List<InvestList> dataList1 = new ArrayList<InvestList>();
    private List<InvestList> dataList2 = new ArrayList<InvestList>();
    private List<InvestList> dataList3 = new ArrayList<InvestList>();
    private List<InvestList> dataList4 = new ArrayList<InvestList>();

    private final static int DEFAULT_PAGE = 0;
    private int page;
    private int page1 = DEFAULT_PAGE;
    private int page2 = DEFAULT_PAGE;
    private int page3 = DEFAULT_PAGE;
    private int page4 = DEFAULT_PAGE;

    private RadioGroup radioGroup;
    //    private String xmtype = Constant.XMTYPE.Borrow;
    private String xmtype;
    private boolean isFirstLoadingomplete = false;

    private boolean isBottomLoadingComplete1 = false;
    private boolean isBottomLoadingComplete2 = false;
    private boolean isBottomLoadingComplete3 = false;
    private boolean isBottomLoadingComplete4 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financial);

        initView();
//        loadData();
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_financial;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    public void onRefresh() {
//        page = DEFAULT_PAGE;
//        getInvestList();
    }

    @Override
    public void onEndOfList(Object lastItem) {

        if (TextUtils.isEmpty(xmtype)) {
            if (isBottomLoadingComplete1) {
                showIndeterminateProgress(false);
                return;
            }
            page1++;
            page = page1;
        } else if (xmtype.equals(Constant.XMTYPE.Borrow)) {
            if (isBottomLoadingComplete2) {
                showIndeterminateProgress(false);
                return;
            }
            page2++;
            page = page2;
        } else if (xmtype.equals(Constant.XMTYPE.Now)) {
            if (isBottomLoadingComplete3) {
                showIndeterminateProgress(false);
                return;
            }
            page3++;
            page = page3;
        } else if (xmtype.equals(Constant.XMTYPE.Yes)) {
            if (isBottomLoadingComplete4) {
                showIndeterminateProgress(false);
                return;
            }
            page4++;
            page = page4;
        }
        getInvestList();
    }

    private void showIndeterminateProgress(boolean visibility) {
        adapter.showIndeterminateProgress(visibility);
    }


    private void initView() {

        pullRefreshLayout = (PMSwipeRefreshLayout) findViewById(R.id.pullRefreshLayout);
        pullRefreshLayout.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
//        pullRefreshLayout.setOnRefreshListener(this);
        pullRefreshLayout.setRefreshing(true);
        pullRefreshLayout.setEnabled(false);

        listView = (EndOfListView) findViewById(R.id.listView);
        listView.setOnEndOfListListener(this);

        adapter = new QuickAdapter<InvestList>(FinancialActivity.this, R.layout.listitem_financial) {
            @Override
            protected void convert(BaseAdapterHelper helper, final InvestList item) {

                String apr = String.format(getString(R.string.financial_annual_rate), item.apr);
                String timeLimit = String.format(getString(R.string.financial_deadline), item.time_limit);
                String amount = String.format(getString(R.string.financial_amount), item.account);
                String reward;
                if (TextUtils.isEmpty(item.funds)) {
                    reward = getString(R.string.financial_reward_empty);
                } else {
                    reward = String.format(getString(R.string.financial_reward), item.funds);
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

                int percentage = (int) (scale / 10);
//                SimpleWaveView simpleWaveView = helper.getView(R.id.simpleWaveView);
//                simpleWaveView.setColor(getResources().getColor(R.color.main_text_orange));
//                simpleWaveView.setPercentage(percentage);
                ImageView imgGifScale = helper.getView(R.id.imgGifScale);
                int resId = R.drawable.p1;
                if (percentage == 10) {  //100%
                    resId = R.drawable.p11;
                } else if (percentage == 9) {
                    resId = R.drawable.p10;
                } else if (percentage == 8) {
                    resId = R.drawable.p9;
                } else if (percentage == 7) {
                    resId = R.drawable.p8;
                } else if (percentage == 6) {
                    resId = R.drawable.p7;
                } else if (percentage == 6) {
                    resId = R.drawable.p7;
                } else if (percentage == 5) {
                    resId = R.drawable.p6;
                } else if (percentage == 4) {
                    resId = R.drawable.p5;
                } else if (percentage == 3) {
                    resId = R.drawable.p4;
                } else if (percentage == 2) {
                    resId = R.drawable.p3;
                } else if (percentage == 1) {
                    resId = R.drawable.p2;
                } else if (percentage == 0) {
                    resId = R.drawable.p1;
                }
                imgGifScale.setImageResource(resId);

                final Button btnState = helper.getView(R.id.btnState);
                helper.setOnClickListener(R.id.btnState, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (btnState.getText().toString().equals(getString(R.string.financial_btn_join))) {
                            if (!Config.isLogin) {
                                Toast.makeText(FinancialActivity.this, "你未登录，无法进行投资", Toast.LENGTH_SHORT).show();
                                UIHelper.showLogin(FinancialActivity.this);
                                return;
                            }
                            UIHelper.showFinanceInvestment(FinancialActivity.this, item);
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
                UIHelper.showFinancialDetail(FinancialActivity.this, adapter.getItem(position));
            }
        });

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbConditionsAll:
                        xmtype = null;
                        adapter.replaceAll(dataList1);
                        break;
                    case R.id.rbConditionsBorrowing:
                        xmtype = Constant.XMTYPE.Borrow;
                        if (dataList2.size() != 0) {
                            adapter.replaceAll(dataList2);
                        } else {
                            page2++;
                            page = page2;
                            adapter.clear();
                            getInvestList();
                        }
                        break;
                    case R.id.rbConditionsReimbursing:
                        xmtype = Constant.XMTYPE.Now;
                        if (dataList3.size() != 0) {
                            adapter.replaceAll(dataList3);
                        } else {
                            page3++;
                            page = page3;
                            adapter.clear();
                            getInvestList();
                        }
                        break;
                    case R.id.rbConditionsCompleteReimbursement:
                        xmtype = Constant.XMTYPE.Yes;
                        if (dataList4.size() != 0) {
                            adapter.replaceAll(dataList4);
                        } else {
                            page4++;
                            page = page4;
                            adapter.clear();
                            getInvestList();
                        }
                        break;
                }
            }
        });
    }

    private void loadData() {
//        getInvestList();
    }

    private void loadFirstData() {
        page1++;
        page = page1;
        adapter.clear();
        getInvestList();
    }


    private void getInvestList() {

        ApiClient.getInvestList(TAG, page, xmtype, new RefreshResponseListener() {
            @Override
            public void onResponse(Object response) {
                super.onResponse(response);

                Invest result = (Invest) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(FinancialActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }
                InvestData data = result.data;
                int currentPage = data.page;
                int totalPage = data.total_page;
                System.out.println("total " + result.data.total);
                System.out.println("page " + result.data.page);
                System.out.println("epage " + result.data.epage);
                System.out.println("total_page " + result.data.total_page);

                List<InvestList> investListTmp = result.data.list;
                List<InvestList> investList = new ArrayList<InvestList>();
                if (investListTmp != null && investListTmp.size() != 0) {
                    for (InvestList item : investListTmp) {
                        if (item.is_you == 1) {
                            investList.add(item);
                        }
                    }

                    if (TextUtils.isEmpty(xmtype)) {
                        if (currentPage == totalPage) {
                            isBottomLoadingComplete1 = true;
                        }
                        dataList1.addAll(investList);
                        adapter.replaceAll(dataList1);

                    } else if (xmtype.equals(Constant.XMTYPE.Borrow)) {
                        if (currentPage == totalPage) {
                            isBottomLoadingComplete2 = true;
                        }
                        dataList2.addAll(investList);
                        adapter.replaceAll(dataList2);

                    } else if (xmtype.equals(Constant.XMTYPE.Now)) {
                        if (currentPage == totalPage) {
                            isBottomLoadingComplete3 = true;
                        }
                        dataList3.addAll(investList);
                        adapter.replaceAll(dataList3);

                    } else if (xmtype.equals(Constant.XMTYPE.Yes)) {
                        if (currentPage == totalPage) {
                            isBottomLoadingComplete4 = true;
                        }
                        dataList4.addAll(investList);
                        adapter.replaceAll(dataList4);
                    }
                }

                if (TextUtils.isEmpty(xmtype)) {
                    if (currentPage == totalPage || totalPage == 0) {
                        isBottomLoadingComplete1 = true;
                    }
                } else if (xmtype.equals(Constant.XMTYPE.Borrow)) {
                    if (currentPage == totalPage || totalPage == 0) {
                        isBottomLoadingComplete2 = true;
                    }
                } else if (xmtype.equals(Constant.XMTYPE.Now)) {
                    if (currentPage == totalPage || totalPage == 0) {
                        isBottomLoadingComplete3 = true;
                    }
                } else if (xmtype.equals(Constant.XMTYPE.Yes)) {
                    if (currentPage == totalPage || totalPage == 0) {
                        isBottomLoadingComplete4 = true;
                    }
                }
            }
        });
    }

    private abstract class RefreshResponseListener implements ResponseListener {

        @Override
        public void onStarted() {

            showIndeterminateProgress(true);
            if (!pullRefreshLayout.isRefreshing()) {
                pullRefreshLayout.setRefreshing(true);
            }
        }

        @Override
        public void onResponse(Object response) {
            pullRefreshLayout.setRefreshing(false);
            showIndeterminateProgress(false);
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            pullRefreshLayout.setRefreshing(false);
            showIndeterminateProgress(false);
        }
    }


}
