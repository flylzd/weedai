package com.weedai.ptp.ui.activity;


import android.os.Bundle;
import android.view.View;

import com.weedai.ptp.R;
import com.weedai.ptp.utils.UIHelper;

public class MyWealthActivity extends BaseActivity implements View.OnClickListener {


    private View layoutRecharge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wealth);

        init();
    }

    private void init() {

        layoutRecharge = findViewById(R.id.layoutRecharge);
        layoutRecharge.setOnClickListener(this);
    }

    protected boolean hasBackButton() {
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layoutRecharge:
                UIHelper.showMyRecharge(MyWealthActivity.this);
                break;
        }
    }
}
