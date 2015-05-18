package com.weedai.ptp.ui.activity;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
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
    private View layoutWithdrawal;

    private TextView tvTotalAmount;
    private TextView tvAvailableBalance;
    private TextView tvCollectingMoney;
    private TextView tvFreezeFunds;
    private TextView tvAvailableMicroCurrency;
    private TextView tvTotalMicroCurrency;
    private ImageView imgCurrencyConversion;
    private TextView tvCheckCurrency;

    private MyWeallthData data;

    private ProgressDialog progressDialog;
    private AlertDialog alertDialog;

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
        imgCurrencyConversion = (ImageView) findViewById(R.id.imgCurrencyConversion);
        tvCheckCurrency = (TextView) findViewById(R.id.tvCheckCurrency);

        layoutRecharge = findViewById(R.id.layoutRecharge);
        layoutWithdrawal = findViewById(R.id.layoutWithdrawal);
        layoutRecharge.setOnClickListener(this);
        layoutWithdrawal.setOnClickListener(this);
        imgCurrencyConversion.setOnClickListener(this);
        tvCheckCurrency.setOnClickListener(this);

        progressDialog = ProgressDialog.show(MyWealthActivity.this, null, getString(R.string.message_waiting));
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
                progressDialog.show();
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
            case R.id.layoutWithdrawal:
                UIHelper.showMyWithdrawal(MyWealthActivity.this);
                break;
            case R.id.imgCurrencyConversion:
                changeCurrency();
                break;
            case R.id.tvCheckCurrency:
                UIHelper.showMyMicroCurrencyHistory(MyWealthActivity.this, tvAvailableMicroCurrency.getText().toString());
                break;
        }
    }

    private void changeCurrency() {

        String microCurrency = tvAvailableMicroCurrency.getText().toString();
        if (TextUtils.isEmpty(microCurrency) || "0".equals(microCurrency)) {
            Toast.makeText(MyWealthActivity.this, "可用微币为0,不能进行转换。", Toast.LENGTH_SHORT).show();
            return;
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(MyWealthActivity.this);
        builder.setTitle("提示");
        builder.setMessage("你有" + microCurrency + "微币将转换成余额" + Float.parseFloat(microCurrency) * 0.1 + "元，该操作不可逆,请选择是否确定操作!");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();

                ApiClient.changeWb(TAG, new ResponseListener() {
                    @Override
                    public void onStarted() {
                        progressDialog.setMessage("正在提交数据...");
                        progressDialog.show();
                    }

                    @Override
                    public void onResponse(Object response) {
                        progressDialog.dismiss();

                        BaseModel result = (BaseModel) response;
                        if (result.code != Constant.CodeResult.SUCCESS) {
                            Toast.makeText(MyWealthActivity.this, result.message, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (result.message.equals("change_success")) {
                            Toast.makeText(MyWealthActivity.this, "微币转换成功", Toast.LENGTH_SHORT).show();

                            tvAvailableMicroCurrency.setText("0");
                        } else {
                            Toast.makeText(MyWealthActivity.this, "微币转换失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.dismiss();
                    }
                });
            }
        });
        builder.create();
        alertDialog = builder.show();

    }
}
