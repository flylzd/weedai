package com.weedai.ptp.ui.activity;


import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.lemon.aklib.adapter.BaseAdapterHelper;
import com.lemon.aklib.adapter.QuickAdapter;
import com.lemon.aklib.widget.EndOfListView;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.model.FinancialManager;
import com.weedai.ptp.model.FinancialManagerList;
import com.weedai.ptp.utils.DataUtil;
import com.weedai.ptp.volley.ResponseListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyFinancialManagementActivity extends BaseActivity implements EndOfListView.OnEndOfListListener {

    private final static String TAG = "MyFinancialManagementActivity";

    private RadioGroup radioGroup;
    private int selectType = 0;

    private EndOfListView listView;
    private QuickAdapter<FinancialManagerList> adapter;
    private List<FinancialManagerList> dataList = new ArrayList<FinancialManagerList>();

    private EndOfListView listView2;
    private QuickAdapter<FinancialManagerList> adapter2;
    private List<FinancialManagerList> dataList2 = new ArrayList<FinancialManagerList>();

    private final static int DEFAULT_PAGE = 0;
    private int page = DEFAULT_PAGE;
    private int page2 = DEFAULT_PAGE;

    private boolean isBottomLoadingComplete1 = false;
    private boolean isBottomLoadingComplete2 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_financial_management);

        initView();
//        loadData();
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_financial_management;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    public void onEndOfList(Object lastItem) {

        if (selectType == 0) {
            if (isBottomLoadingComplete1) {
                showIndeterminateProgress(false);
                return;
            }
            page++;
            getFinancialSuccess();
        } else {
            if (isBottomLoadingComplete2) {
                showIndeterminateProgress(false);
                return;
            }
            page2++;
            getFinancialGathering();
        }
    }

    private void showIndeterminateProgress(boolean visibility) {
        if (selectType == 0) {
            adapter.showIndeterminateProgress(visibility);
        } else {
            adapter2.showIndeterminateProgress(visibility);
        }
    }

    private void initView() {

        adapter = new QuickAdapter<FinancialManagerList>(MyFinancialManagementActivity.this, R.layout.listitem_my_financial_management) {
            @Override
            protected void convert(BaseAdapterHelper helper, FinancialManagerList item) {

                String userNmae = String.format(getString(R.string.user_my_financial_management_borrowers), item.username);
                String borrowAccount = String.format(getString(R.string.user_my_financial_management_amount), item.account);
                String timeLimit = String.format(getString(R.string.user_my_financial_management_time_limit), item.time_limit);
                String anum = String.format(getString(R.string.user_my_financial_management_loan_amount), item.anum);
                String credit = String.format(getString(R.string.user_my_financial_management_borrowers_integral), item.credit);
                String apr = String.format(getString(R.string.user_my_financial_management_annual_interest_rate), item.apr);
//                String tenderTime = String.format(getString(R.string.user_my_financial_management_bid_time), item.tender_time);
                String repaymentAccount = String.format(getString(R.string.user_my_financial_management_receivable_interest), item.repayment_account);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String time = sdf.format(Long.parseLong(item.tender_time + "000"));
                String tenderTime = time;

                helper.setText(R.id.tvTitle, DataUtil.urlDecode(item.borrow_name));
                helper.setText(R.id.tvBorrowUsername, DataUtil.urlDecode(userNmae));
                helper.setText(R.id.tvBorrowAccount, borrowAccount);
                helper.setText(R.id.tvBorrowTimeLimit, timeLimit);
                helper.setText(R.id.tvBorrowAnum, anum);

                helper.setText(R.id.tvBorrowCredit, credit);
                helper.setText(R.id.tvBorrowApr, apr);
                helper.setText(R.id.tvBorrowTenderTime, tenderTime);
                helper.setText(R.id.tvBorrowRepaymentAccount, Html.fromHtml(repaymentAccount));

            }
        };

        adapter2 = new QuickAdapter<FinancialManagerList>(MyFinancialManagementActivity.this, R.layout.listitem_my_financial_borrow_gathering) {
            @Override
            protected void convert(BaseAdapterHelper helper, FinancialManagerList item) {

                String status;
                if (item.status == 0) {
                    status = "未还";
                } else {
                    status = "已收";
                }

//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                String repay_time = sdf.format(Long.parseLong(item.repayment_times + "000"));
//
//                String receivableDate = String.format(getString(R.string.user_my_financial_borrowers_receivable_date), repay_time);
                String receivableDate = String.format(getString(R.string.user_my_financial_borrowers_receivable_date), item.repayment_times_d);

                String order = (item.order + 1) + "/" + item.time_limit;
                order = String.format(getString(R.string.user_my_financial_borrowers_installment), order);
                String capital = String.format(getString(R.string.user_my_financial_borrowers_capital), item.capital);
                String lateInterest = String.format(getString(R.string.user_my_financial_borrowers_late_interest), item.late_interest);
                status = String.format(getString(R.string.user_my_financial_borrowers_status), status);

                String username = String.format(getString(R.string.user_my_financial_borrowers_borrower_name), item.username);
                String repayAccount = String.format(getString(R.string.user_my_financial_borrowers_total_payment), item.repay_account);
                String interest = String.format(getString(R.string.user_my_financial_borrowers_interest), item.interest);
                String lateDays = String.format(getString(R.string.user_my_financial_borrowers_late_days), item.late_days);

                helper.setText(R.id.tvTitle, DataUtil.urlDecode(item.borrow_name));
                helper.setText(R.id.tvBorrowReceivableDate, receivableDate);
                helper.setText(R.id.tvBorrowInstallment, order);
                helper.setText(R.id.tvBorrowCapital, capital);
                helper.setText(R.id.tvBorrowLateInterest, lateInterest);
                helper.setText(R.id.tvBorrowStatus, status);

                helper.setText(R.id.tvBorrowUsername, DataUtil.urlDecode(username));
                helper.setText(R.id.tvBorrowTotalPayment, repayAccount);
                helper.setText(R.id.tvBorrowInterest, interest);
                helper.setText(R.id.tvBorrowLateDays, lateDays);
            }
        };

        listView = (EndOfListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnEndOfListListener(this);

        listView2 = (EndOfListView) findViewById(R.id.listView2);
        listView2.setAdapter(adapter2);
        listView2.setOnEndOfListListener(this);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbConditionsBorrowing:
                        selectType = 0;
                        listView.setVisibility(View.VISIBLE);
                        listView2.setVisibility(View.GONE);
                        break;
                    case R.id.rbConditionsCollection:
                        selectType = 1;
                        listView.setVisibility(View.GONE);
                        listView2.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

    }

    private void loadData() {
        getFinancialSuccess();
    }

    private void getFinancialSuccess() {

        ApiClient.getFinancialSuccess(TAG, page, new RefreshResponseListener() {
            @Override
            public void onResponse(Object response) {
                super.onResponse(response);

                FinancialManager result = (FinancialManager) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(MyFinancialManagementActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }

                List<FinancialManagerList> list = result.data.list;
                dataList.addAll(list);
                adapter.replaceAll(dataList);

                int currentPage = result.data.page;
                int totalPage = result.data.total_page;
                if (currentPage == totalPage || totalPage == 0) {
                    isBottomLoadingComplete1 = true;
                }
            }
        });
    }

    private void getFinancialGathering() {

        ApiClient.getFinancialGathering(TAG, page2, new RefreshResponseListener() {
            @Override
            public void onResponse(Object response) {
                super.onResponse(response);

                FinancialManager result = (FinancialManager) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(MyFinancialManagementActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }

                List<FinancialManagerList> list = result.data.list;
                dataList2.addAll(list);
                adapter2.replaceAll(dataList2);

                int currentPage = result.data.page;
                int totalPage = result.data.total_page;
                if (currentPage == totalPage || totalPage == 0) {
                    isBottomLoadingComplete2 = true;
                }
            }
        });
    }


    private abstract class RefreshResponseListener implements ResponseListener {

        @Override
        public void onStarted() {
            showIndeterminateProgress(true);
        }

        @Override
        public void onResponse(Object response) {
            showIndeterminateProgress(false);
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            showIndeterminateProgress(false);
        }
    }
}
