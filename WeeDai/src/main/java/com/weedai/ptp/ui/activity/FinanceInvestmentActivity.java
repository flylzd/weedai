package com.weedai.ptp.ui.activity;

import android.os.Bundle;

import com.weedai.ptp.R;

/**
 * 投资理财
 */
public class FinanceInvestmentActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_investment);

        initView();
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_finance_investment;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    private void initView() {

    }
}
