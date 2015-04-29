package com.weedai.ptp.ui.activity;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.model.ReceivableSearch;
import com.weedai.ptp.model.ReceivableSearchData;
import com.weedai.ptp.volley.ResponseListener;

public class ReceivableSearchActivity extends BaseActivity {

    private final static String TAG = "ReceivableSearchActivity";

    private TextView tvReceivable;
    private TextView tvReceivableMicroCurrency;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receivable_search);

        initView();
        loadData();
    }

    protected boolean hasBackButton() {
        return true;
    }

    protected int getActionBarTitle() {
        return R.string.title_receivable_searcht;
    }

    private void initView() {
        tvReceivable = (TextView) findViewById(R.id.tvReceivable);
        tvReceivableMicroCurrency = (TextView) findViewById(R.id.tvReceivableMicroCurrency);
    }

    private void loadData() {
        searchReceiable();
    }

    private void searchReceiable() {
        ApiClient.searchReceiable(TAG, new RefreshResponseListener() {
            @Override
            public void onResponse(Object response) {
                super.onResponse(response);

                ReceivableSearch result = (ReceivableSearch) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(ReceivableSearchActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }

                ReceivableSearchData data = result.data;
                String receivable = String.format(getString(R.string.user_receivable), data.huikuan);
                String microCurrency = String.format(getString(R.string.user_receivable_micro_currency), data.wb_shouyi);
                tvReceivable.setText(Html.fromHtml(receivable));
                tvReceivableMicroCurrency.setText(Html.fromHtml(microCurrency));
            }
        });
    }

    private abstract class RefreshResponseListener implements ResponseListener {

        @Override
        public void onStarted() {
            progressDialog = ProgressDialog.show(ReceivableSearchActivity.this, null, getString(R.string.message_waiting));
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
