package com.weedai.ptp.ui.activity;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.model.Bank;
import com.weedai.ptp.model.BaseModel;
import com.weedai.ptp.model.Discount;
import com.weedai.ptp.volley.ResponseListener;

import org.w3c.dom.Text;

/**
 * 手机充值
 */
public class PhoneRechargeActivity extends BaseActivity implements View.OnClickListener {

    private final static String TAG = "PhoneRechargeActivity";

    private TextView tvDiscount;
    private EditText etPhone;
    private TextView tvPhone30;
    private TextView tvPhone50;
    private TextView tvPhone100;
    private Button btnSubmit;

    private String money = "30";

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_recharge);
        initView();
        getDiscount();
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_phone_recharge;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    private void initView() {
        tvDiscount = (TextView) findViewById(R.id.tvDiscount);
        etPhone = (EditText) findViewById(R.id.etPhone);
        tvPhone30 = (TextView) findViewById(R.id.tvPhone30);
        tvPhone50 = (TextView) findViewById(R.id.tvPhone50);
        tvPhone100 = (TextView) findViewById(R.id.tvPhone100);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        tvPhone30.setSelected(true);

        tvPhone30.setOnClickListener(this);
        tvPhone50.setOnClickListener(this);
        tvPhone100.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

    }

    private void setPhoneRechargeNotSelected() {
        tvPhone30.setSelected(false);
        tvPhone50.setSelected(false);
        tvPhone100.setSelected(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvPhone30:
                setPhoneRechargeNotSelected();
                tvPhone30.setSelected(true);
                money = "30";
                break;
            case R.id.tvPhone50:
                setPhoneRechargeNotSelected();
                tvPhone50.setSelected(true);
                money = "50";
                break;
            case R.id.tvPhone100:
                setPhoneRechargeNotSelected();
                tvPhone100.setSelected(true);
                money = "100";
                break;
            case R.id.btnSubmit: {

                phoneRecharge(money);
            }

            break;
        }
    }

    private void phoneRecharge(String money) {
        String phoneNum = etPhone.getText().toString();
        if (TextUtils.isEmpty(phoneNum)) {
            Toast.makeText(PhoneRechargeActivity.this, "充值手机号码不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (phoneNum.length() != 11) {
            Toast.makeText(PhoneRechargeActivity.this, "手机号码出错!", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiClient.phoneRecharges(TAG, phoneNum, money, new ResponseListener() {
            @Override
            public void onStarted() {
                progressDialog = ProgressDialog.show(PhoneRechargeActivity.this, null, getString(R.string.message_submit));
            }

            @Override
            public void onResponse(Object response) {
                progressDialog.dismiss();
                BaseModel result = (BaseModel) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    if (result.message.equals("no_money")){
                        Toast.makeText(PhoneRechargeActivity.this, "你的账号没有余额了。", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(PhoneRechargeActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (result.message.equals("success")){
                    Toast.makeText(PhoneRechargeActivity.this, "充值成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(PhoneRechargeActivity.this, "充值失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
            }
        });

    }

    private void getDiscount() {
        ApiClient.getDiscount(TAG, new ResponseListener() {
            @Override
            public void onStarted() {
                progressDialog = ProgressDialog.show(PhoneRechargeActivity.this, null, getString(R.string.message_waiting));
            }

            @Override
            public void onResponse(Object response) {
                progressDialog.dismiss();

                Discount result = (Discount) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(PhoneRechargeActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }

                String discount = String.format(getResources().getString(R.string.phone_recharge_discount), result.data.get("discount"));
                tvDiscount.setText(discount);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
            }
        });
    }


}
