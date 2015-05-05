package com.weedai.ptp.ui.activity;


import android.app.ProgressDialog;
import android.os.Bundle;

import com.android.volley.error.VolleyError;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.volley.ResponseListener;

public class MyBankCardActivity extends BaseActivity {

    private final static String TAG = "MyBankCardActivity";

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bank_card);

        initView();
        loadData();
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_about;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    private void initView() {



    }

    private void loadData() {
        getBank();
    }

    private void getBank() {
        ApiClient.getBank(TAG, new ResponseListener() {
            @Override
            public void onStarted() {
                progressDialog = ProgressDialog.show(MyBankCardActivity.this, null, getString(R.string.message_waiting));
            }

            @Override
            public void onResponse(Object response) {
                progressDialog.dismiss();


            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
            }
        });
    }
}