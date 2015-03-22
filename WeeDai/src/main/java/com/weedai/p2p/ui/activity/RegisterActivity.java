package com.weedai.p2p.ui.activity;


import android.os.Bundle;

import com.weedai.p2p.R;

public class RegisterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

//        initFragmentSwitcher();
//        initRadioGroup();
    }

    protected boolean hasBackButton() {
        return true;
    }

    protected int getActionBarTitle() {
        return R.string.register_title;
    }
}
