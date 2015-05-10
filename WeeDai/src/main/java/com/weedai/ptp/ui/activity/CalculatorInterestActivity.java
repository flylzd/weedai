package com.weedai.ptp.ui.activity;


import android.os.Bundle;

import com.weedai.ptp.R;

public class CalculatorInterestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator_interest);

        initView();
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_calculator_interest;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    private void initView() {

    }
}
