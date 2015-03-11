package com.weedai.p2p.ui.activity;


import android.os.Bundle;

import com.weedai.p2p.R;

public class OptimizingFinancialActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_optimizing_financial);

    }

    protected boolean hasBackButton() {
        return true;
    }
}
