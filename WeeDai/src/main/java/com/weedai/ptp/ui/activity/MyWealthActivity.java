package com.weedai.ptp.ui.activity;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.model.BaseModel;
import com.weedai.ptp.model.MyWeallth;
import com.weedai.ptp.model.MyWeallthData;
import com.weedai.ptp.utils.UIHelper;
import com.weedai.ptp.volley.ResponseListener;

public class MyWealthActivity extends BaseActivity implements View.OnClickListener {

    private final static String TAG = "MyWealthActivity";

    private View layoutRecharge;

    private TextView tvTotalAmount;
    private TextView tvAvailableBalance;
    private TextView tvCollectingMoney;
    private TextView tvFreezeFunds;
    private TextView tvAvailableMicroCurrency;
    private TextView tvTotalMicroCurrency;

    private MyWeallthData data;

    private ProgressDialog progressDialog;

    String wb;
    String amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wealth);

        if (getIntent().hasExtra("wb")) {
            wb = getIntent().getStringExtra("wb");
        }
        if (getIntent().hasExtra("amount")) {
            amount = getIntent().getStringExtra("amount");
        }

        initView();
        loadData();
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

        tvTotalAmount = (TextView) findViewById(R.id.tvTotalAmount);
        tvAvailableBalance = (TextView) findViewById(R.id.tvAvailableBalance);
        tvCollectingMoney = (TextView) findViewById(R.id.tvCollectingMoney);
        tvFreezeFunds = (TextView) findViewById(R.id.tvFreezeFunds);
        tvAvailableMicroCurrency = (TextView) findViewById(R.id.tvAvailableMicroCurrency);
        tvTotalMicroCurrency = (TextView) findViewById(R.id.tvTotalMicroCurrency);

        layoutRecharge = findViewById(R.id.layoutRecharge);
        layoutRecharge.setOnClickListener(this);
    }

    private void setInfo() {
        if (data != null) {
            tvTotalAmount.setText(data.zongjine);
            tvAvailableBalance.setText(amount);
            tvCollectingMoney.setText(data.tender_wait);
            tvFreezeFunds.setText(data.no_use_money);
            tvAvailableMicroCurrency.setText(wb);
            tvTotalMicroCurrency.setText(data.wb);
        }
    }

    private void loadData() {

        ApiClient.getMyWealth(TAG, new ResponseListener() {
            @Override
            public void onStarted() {
                progressDialog = ProgressDialog.show(MyWealthActivity.this, null, getString(R.string.login_waiting));
            }

            @Override
            public void onResponse(Object response) {
                progressDialog.dismiss();

                MyWeallth result = (MyWeallth) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(MyWealthActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }
                data = result.data;
                setInfo();
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
            }
        });
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
