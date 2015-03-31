package com.weedai.ptp.ui.activity;


import android.os.Bundle;

import com.weedai.ptp.R;

public class MyMicroCurrencyHistoryActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_micro_currency_history);

    }

    protected boolean hasBackButton() {
        return true;
    }
}
