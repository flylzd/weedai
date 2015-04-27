package com.weedai.ptp.ui.activity;


import android.app.ProgressDialog;
import android.os.Bundle;

import com.android.volley.error.VolleyError;
import com.weedai.ptp.R;
import com.weedai.ptp.model.InvestList;
import com.weedai.ptp.volley.ResponseListener;

public class FinancialDetailActivity extends BaseActivity {

    private final static String TAG = "FinancialDetailActivity";

    private ProgressDialog progressDialog;

    //    private String id;
    private InvestList data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financial_detail);

        if (getIntent().hasExtra("data")) {
            data = (InvestList) getIntent().getSerializableExtra("data");
        }

        initView();
        loadData();
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_financial_detail;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    private void initView() {

    }

    private void loadData() {

        getFinancialDetail();
    }

    private void getFinancialDetail() {

//        ApiClient.getFinancialDetail(TAG, id, new RefreshResponseListener() {
//            @Override
//            public void onResponse(Object response) {
//                super.onResponse(response);
//            }
//        });
    }


    private abstract class RefreshResponseListener implements ResponseListener {

        @Override
        public void onStarted() {
            progressDialog = ProgressDialog.show(FinancialDetailActivity.this, null, getString(R.string.message_waiting));


        }

        @Override
        public void onResponse(Object response) {
            progressDialog.dismiss();

        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            progressDialog.dismiss();
        }
    }

}
