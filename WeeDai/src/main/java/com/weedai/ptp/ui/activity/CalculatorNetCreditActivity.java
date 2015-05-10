package com.weedai.ptp.ui.activity;


import android.os.Bundle;

import com.weedai.ptp.R;

public class CalculatorNetCreditActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator_net_credit);

        initView();
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_calculator_net_credit;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    private void initView() {

    }
}
