package com.weedai.ptp.ui.activity;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.error.VolleyError;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.volley.ResponseListener;

public class CalculatorNetCreditActivity extends BaseActivity implements View.OnClickListener {

    private final static String TAG = "CalculatorNetCreditActivity";

    private EditText etApr;
    private EditText etLimit;
    private EditText etBidAmount;
    private EditText etBidDate;
    private EditText etBidAward;
    private EditText etManager;
    private TextView tvType;

    private Button btnCalculator;
    private Button btnReset;

    private int type = 3;   //0等额本息
    //3到期还本,按月付息

    private AlertDialog alertDialog;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator_net_credit);

        initView();
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_calculator_net_credit;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    private void initView() {

        etApr = (EditText) findViewById(R.id.etApr);
        etLimit = (EditText) findViewById(R.id.etLimit);
        etBidAmount = (EditText) findViewById(R.id.etBidAmount);
        etBidDate = (EditText) findViewById(R.id.etBidDate);
        etBidAward = (EditText) findViewById(R.id.etBidAward);
        etManager = (EditText) findViewById(R.id.etManager);
        tvType = (TextView) findViewById(R.id.tvType);
        btnCalculator = (Button) findViewById(R.id.btnCalculator);
        btnReset = (Button) findViewById(R.id.btnReset);

        tvType.setOnClickListener(this);
        btnCalculator.setOnClickListener(this);
        btnReset.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvType: {
                final AlertDialog.Builder builder = new AlertDialog.Builder(CalculatorNetCreditActivity.this);
                builder.setTitle("还款方式");
                View view = getLayoutInflater().inflate(R.layout.dialog_calculator_interest, null);
                final TextView tvType3 = (TextView) view.findViewById(R.id.tvType3);
                final TextView tvType0 = (TextView) view.findViewById(R.id.tvType0);
                tvType0.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        type = 0;
                        tvType.setText(tvType0.getText().toString());
                        alertDialog.dismiss();
                    }
                });
                tvType3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        type = 3;
                        tvType.setText(tvType3.getText().toString());
                        alertDialog.dismiss();
                    }
                });
                builder.setView(view);
                builder.create();
                alertDialog = builder.show();
            }
            break;
            case R.id.btnCalculator:

                break;
            case R.id.btnReset:
                break;
        }
    }

    private void calculator(String account, String lilv, String times, int type) {

        ApiClient.calculatorInterest(TAG, account, lilv, times, type, new ResponseListener() {
            @Override
            public void onStarted() {
                progressDialog = ProgressDialog.show(CalculatorNetCreditActivity.this, null, "正在提交数据");
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
