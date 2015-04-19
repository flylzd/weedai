package com.weedai.ptp.ui.activity;


import android.os.Bundle;

import com.weedai.ptp.R;

public class HeroActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero);

        initView();
        loadData();
    }

    protected boolean hasBackButton() {
        return true;
    }

    private void initView() {
    }

    private void loadData() {
    }
}
