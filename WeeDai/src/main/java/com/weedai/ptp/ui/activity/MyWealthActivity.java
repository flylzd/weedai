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

        initView();
    }


    @Override
    protected int getActionBarTitle() {
        return R.string.title_my_wealth;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    private void initView() {
        layoutRecharge = findViewById(R.id.layoutRecharge);
        layoutRecharge.setOnClickListener(this);
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
