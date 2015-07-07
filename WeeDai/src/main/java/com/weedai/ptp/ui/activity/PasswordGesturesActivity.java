package com.weedai.ptp.ui.activity;


import android.os.Bundle;
import android.view.View;

import com.weedai.ptp.R;
import com.weedai.ptp.utils.UIHelper;

public class PasswordGesturesActivity extends BaseActivity implements View.OnClickListener {


    private View layoutPasswordGestures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_gestures);

        initView();
//        loadData();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.layoutPasswordGestures:
                UIHelper.showLock9View(PasswordGesturesActivity.this);
                break;
        }
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_password_gestures;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    private void initView() {
        layoutPasswordGestures = findViewById(R.id.layoutPasswordGestures);
        layoutPasswordGestures.setOnClickListener(this);
    }

}
