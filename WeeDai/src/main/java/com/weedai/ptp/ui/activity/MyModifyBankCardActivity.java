package com.weedai.ptp.ui.activity;

import android.os.Bundle;

import com.weedai.ptp.R;


public class MyModifyBankCardActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_modify_bank_card);

    }

    protected boolean hasBackButton() {
        return true;
    }
}