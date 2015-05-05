package com.weedai.ptp.ui.activity;


import android.os.Bundle;

import com.weedai.ptp.R;

public class MyWithdrawalActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_withdrawal);

    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_withdrawal;
    }

    protected boolean hasBackButton() {
        return true;
    }

}
