package com.weedai.ptp.ui.activity;


import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.weedai.ptp.R;

public class MyWithdrawalActivity extends BaseActivity {

    private final static String TAG = "MyWithdrawalActivity";

    private TextView tvRealName;
    private TextView tvAccountBalance;
    private TextView tvAvailableBalance;
    private TextView tvFreezeBalance;
    private TextView tvBankName;
    private TextView tvBankNum;
    private TextView tvTradePassword;
    private TextView tvWithdrawalAmount;
    private Button btnOk;


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

    private void initView() {

    }

}
