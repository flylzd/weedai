package com.weedai.ptp.ui.activity;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ListView;

import com.android.volley.error.VolleyError;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.volley.ResponseListener;

public class MoneyRecordActivity extends BaseActivity {

    private final static String TAG = "MoneyRecordActivity";

    private ListView listView;

    private int page = 1;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_record);

        initView();
        loadData();
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_money_record;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    private void initView() {

        listView = (ListView) findViewById(R.id.listView);
    }

    private void loadData() {
        getMoneyRecord();
    }

    private void getMoneyRecord() {
        ApiClient.getMoneyRecord(TAG, page, new RefreshResponseListener() {
            @Override
            public void onResponse(Object response) {
                super.onResponse(response);
            }
        });
    }

    private abstract class RefreshResponseListener implements ResponseListener {

        @Override
        public void onStarted() {
            progressDialog = ProgressDialog.show(MoneyRecordActivity.this, null, getString(R.string.message_waiting));
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
