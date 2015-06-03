package com.weedai.ptp.ui.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.weedai.ptp.R;
import com.weedai.ptp.constant.Config;
import com.weedai.ptp.model.InvestList;
import com.weedai.ptp.utils.DataUtil;
import com.weedai.ptp.utils.UIHelper;
import com.weedai.ptp.view.NumberProgressBar;
import com.weedai.ptp.volley.ResponseListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FinancialDetailActivity extends BaseActivity {

    private final static String TAG = "FinancialDetailActivity";

    private TextView tvTitle;
    private TextView tvBreakEvenIcon;
    private TextView tvRewardIcon;
    private TextView tvFinancialAmount;
    private TextView tvFinancialOther;
    private TextView tvFinancialAnnualRate;
    private TextView tvReward;
    private TextView tvFinancialLockPeriod;

    private TextView tvAboutDistanceSelling;  //距离发售
    private NumberProgressBar numberProgressBar;  //进度
    private TextView tvAboutAudit;  //距离审核
    private TextView tvAboutReviewTime;  //复审时间

    private TextView tvInterestAgreement;

    private TextView tvReimbursement;
    private Button btnInvestment;


    private ProgressDialog progressDialog;

    //    private String id;
    private InvestList data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financial_detail);

        if (getIntent().hasExtra("data")) {
            data = (InvestList) getIntent().getSerializableExtra("data");
        }

        initView();
        loadData();
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_financial_detail;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    private void initView() {

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvBreakEvenIcon = (TextView) findViewById(R.id.tvBreakEvenIcon);
        tvRewardIcon = (TextView) findViewById(R.id.tvRewardIcon);
        tvFinancialAmount = (TextView) findViewById(R.id.tvFinancialAmount);
        tvFinancialOther = (TextView) findViewById(R.id.tvFinancialOther);
        tvFinancialAnnualRate = (TextView) findViewById(R.id.tvFinancialAnnualRate);
        tvReward = (TextView) findViewById(R.id.tvReward);
        tvFinancialLockPeriod = (TextView) findViewById(R.id.tvFinancialLockPeriod);
        tvInterestAgreement = (TextView) findViewById(R.id.tvInterestAgreement);
        tvInterestAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://www.weedai.com/benjin/index.html"; // web address
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
        btnInvestment = (Button) findViewById(R.id.btnInvestment);
        btnInvestment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Config.isLogin) {
                    Toast.makeText(FinancialDetailActivity.this, "你未登录，无法进行投资", Toast.LENGTH_SHORT).show();
                    UIHelper.showLogin(FinancialDetailActivity.this);
                    return;
                }
                UIHelper.showFinanceInvestment(FinancialDetailActivity.this, data);
            }
        });


        tvTitle.setText(DataUtil.urlDecode(data.name));

        String amount = String.format(getString(R.string.financial_detail_amount), data.account);
        String apr = String.format(getString(R.string.financial_detail_annual_rate), data.apr);
        String timeLimit = String.format(getString(R.string.financial_detail_lock_period), data.time_limit);
        tvFinancialAmount.setText(amount);
        tvFinancialAnnualRate.setText(apr);
        tvFinancialLockPeriod.setText(Html.fromHtml(timeLimit));

        String other = String.format(getString(R.string.financial_detail_other), data.other);
        tvFinancialOther.setText(other);

        String reward;
        if (TextUtils.isEmpty(data.funds)) {
            reward = getString(R.string.financial_detail_reward_empty);
            tvRewardIcon.setVisibility(View.GONE);
        } else {
            reward = String.format(getString(R.string.financial_detail_reward), data.funds);
            tvRewardIcon.setVisibility(View.VISIBLE);
        }
        tvReward.setText(Html.fromHtml(reward));


        tvAboutDistanceSelling = (TextView) findViewById(R.id.tvAboutDistanceSelling);
        numberProgressBar = (NumberProgressBar) findViewById(R.id.numberProgressBar);
        tvAboutAudit = (TextView) findViewById(R.id.tvAboutAudit);
        tvAboutReviewTime = (TextView) findViewById(R.id.tvAboutReviewTime);

        String statusStr;
        int status = data.status;
        final float scale = data.scale;
        if (status == 1) {
            if (scale == 100) {
                statusStr = "已结束";
                btnInvestment.setText(statusStr);
                btnInvestment.setEnabled(false);
            } else {
                statusStr = "开始竞标";
            }
        } else {
            if (data.repayment_account == data.repayment_yesaccount) {
                statusStr = "已结束";
                btnInvestment.setText(statusStr);
                btnInvestment.setEnabled(false);
            } else {
                statusStr = "还款中";
                btnInvestment.setText(statusStr);
                btnInvestment.setEnabled(false);
            }
        }
        tvAboutDistanceSelling.setText(statusStr);
        tvAboutAudit.setText("审核结束");
        String successTime = data.success_time;
        if (TextUtils.isEmpty(successTime)) {
            tvAboutReviewTime.setText("无");
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            String succTime = sdf.format(Long.parseLong(successTime + "000"));
            tvAboutReviewTime.setText(succTime);
        }
        numberProgressBar.setProgress(scale);

        tvReimbursement = (TextView) findViewById(R.id.tvReimbursement);
        int style = data.style;
        if (style == 0) {
            tvReimbursement.setText(getString(R.string.financial_detail_reimbursement_one));
        } else if (style == 3) {
            tvReimbursement.setText(getString(R.string.financial_detail_reimbursement_two));
        }
    }

    private void loadData() {

        getFinancialDetail();
    }

    private void getFinancialDetail() {

//        ApiClient.getFinancialDetail(TAG, id, new RefreshResponseListener() {
//            @Override
//            public void onResponse(Object response) {
//                super.onResponse(response);
//            }
//        });
    }


    private abstract class RefreshResponseListener implements ResponseListener {

        @Override
        public void onStarted() {
            progressDialog = ProgressDialog.show(FinancialDetailActivity.this, null, getString(R.string.message_waiting));


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
