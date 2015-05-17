package com.weedai.ptp.ui.activity;

import android.os.Bundle;


import com.weedai.ptp.R;

public class PartnerActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner);

        initView();
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_partner;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    private void initView() {

    }
}


