package com.weedai.ptp.ui.activity;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.model.Calculator;
import com.weedai.ptp.volley.ResponseListener;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    private TextView tvZshouyi;
    private TextView tvAwards;
    private TextView tvNianlilv;
    private TextView tvYuelilv;

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
        etBidDate.setOnClickListener(this);
        btnCalculator.setOnClickListener(this);
        btnReset.setOnClickListener(this);

        tvZshouyi = (TextView) findViewById(R.id.tvZshouyi);
        tvAwards = (TextView) findViewById(R.id.tvAwards);
        tvNianlilv = (TextView) findViewById(R.id.tvNianlilv);
        tvYuelilv = (TextView) findViewById(R.id.tvYuelilv);

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

            case R.id.etBidDate:
                showDateDialog();
                break;
            case R.id.btnCalculator:
                calculator();
                break;
            case R.id.btnReset:
                reset();
                break;
        }
    }

    private void reset() {
        etApr.getText().clear();
        etLimit.getText().clear();
        etBidAmount.getText().clear();
        etBidDate.getText().clear();
        etBidAward.getText().clear();
        etManager.getText().clear();
    }

    private void showDateDialog() {

        String format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String date = sdf.format(new Date());

        String[] arrInstallationDate = date.toString().split("-");
        int year = Integer.parseInt(arrInstallationDate[0]);
        int month = Integer.parseInt(arrInstallationDate[1]);
        int day = Integer.parseInt(arrInstallationDate[2]);

        new DatePickerDialog(CalculatorNetCreditActivity.this, onDateSetListener, year, month - 1, day)
                .show();
    }

    DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            String startDate = year + "-" + (monthOfYear + 1) + "-"
                    + dayOfMonth;
            etBidDate.setText(startDate);
        }
    };

    private void calculator() {

        String nianxi = etApr.getText().toString();
        String times = etLimit.getText().toString();
        String account = etBidAmount.getText().toString();
        String addtime = etBidDate.getText().toString();
        String award = etBidAward.getText().toString();
        String manage = etManager.getText().toString();

        if (TextUtils.isEmpty(nianxi)) {
            Toast.makeText(CalculatorNetCreditActivity.this, "请输入年利率", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(times) || times.equals("0")) {
            Toast.makeText(CalculatorNetCreditActivity.this, "请输入期限", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(account)) {
            Toast.makeText(CalculatorNetCreditActivity.this, "请输入投标金额", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(addtime)) {
            Toast.makeText(CalculatorNetCreditActivity.this, "请输入投标日期", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(award)) {
            award = "0";
        }
        if (TextUtils.isEmpty(manage)) {
            manage = "0";
        }

        calculator(nianxi, times, type, account, addtime, award, manage);

    }

    private void calculator(String nianxi, String times, int type, String account, String addtime, String award, String manage) {

        ApiClient.calculatorNetCredit(TAG, nianxi, times, type, account, addtime, award, manage, new ResponseListener() {
            @Override
            public void onStarted() {
                progressDialog = ProgressDialog.show(CalculatorNetCreditActivity.this, null, "正在提交数据");
            }

            @Override
            public void onResponse(Object response) {
                progressDialog.dismiss();

                Calculator result = (Calculator) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(CalculatorNetCreditActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }

                tvZshouyi.setText(result.data.zshouyi);
                String awards = result.data.awards;
                String nianlilv = result.data.nianlilv;
                String yuelilv = result.data.yuelilv;
                if (TextUtils.isEmpty(awards)) {
                    awards = "0";
                }
                if (TextUtils.isEmpty(nianlilv)) {
                    nianlilv = "0";
                }
                if (TextUtils.isEmpty(yuelilv)) {
                    yuelilv = "0";
                }
                tvAwards.setText("含奖励:" + awards + "元");
                tvNianlilv.setText("年化利率:" + nianlilv + "%");
                tvYuelilv.setText("年月化利率:" + yuelilv + "%");
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
            }
        });

    }
}
