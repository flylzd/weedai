package com.weedai.ptp.ui.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.weedai.ptp.R;

public class AccountActivity extends BaseActivity implements View.OnClickListener {

    private final static String TAG = "AccountActivity";

    private RelativeLayout layoutAccountDateBirth;
    private RelativeLayout layoutAccountPhone;
    private RelativeLayout layoutAccountEmail;
    private RelativeLayout layoutAccountModifyLoginPassword;
    private RelativeLayout layoutAccountPasswordProtection;
    private RelativeLayout layoutAccountPasswordManagementGestures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        initView();
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_account;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    private void initView() {

        layoutAccountDateBirth = (RelativeLayout) findViewById(R.id.layoutAccountDateBirth);
        layoutAccountPhone = (RelativeLayout) findViewById(R.id.layoutAccountPhone);
        layoutAccountEmail = (RelativeLayout) findViewById(R.id.layoutAccountEmail);
        layoutAccountModifyLoginPassword = (RelativeLayout) findViewById(R.id.layoutAccountModifyLoginPassword);
        layoutAccountPasswordProtection = (RelativeLayout) findViewById(R.id.layoutAccountPasswordProtection);
        layoutAccountPasswordManagementGestures = (RelativeLayout) findViewById(R.id.layoutAccountPasswordManagementGestures);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layoutAccountDateBirth:
                break;
            case R.id.layoutAccountPhone:
                break;
            case R.id.layoutAccountEmail:
                break;
            case R.id.layoutAccountModifyLoginPassword:
                break;
            case R.id.layoutAccountPasswordProtection:
                break;
            case R.id.layoutAccountPasswordManagementGestures:
                break;
        }
    }
}
