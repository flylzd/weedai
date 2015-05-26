package com.weedai.ptp.ui.activity;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.model.Bank;
import com.weedai.ptp.model.BankData;
import com.weedai.ptp.model.BaseModel;
import com.weedai.ptp.model.User;
import com.weedai.ptp.model.UserData;
import com.weedai.ptp.utils.DataUtil;
import com.weedai.ptp.volley.ResponseListener;

/**
 * 提现
 */
public class MyWithdrawalActivity extends BaseActivity {

    private final static String TAG = "MyWithdrawalActivity";

    private TextView tvRealName;
    private TextView tvAccountBalance;
    private TextView tvAvailableBalance;
    private TextView tvFreezeBalance;
    private ImageView imgBankIcon;
    private TextView tvBankAccount;
    private TextView tvBankName;
    private TextView tvTradePassword;
    private TextView tvWithdrawalAmount;
    private Button btnOk;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_withdrawal);

        initView();
        loadData();
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_withdrawal;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    private void initView() {

        tvRealName = (TextView) findViewById(R.id.tvRealName);
        tvAccountBalance = (TextView) findViewById(R.id.tvAccountBalance);
        tvAvailableBalance = (TextView) findViewById(R.id.tvAvailableBalance);
        tvFreezeBalance = (TextView) findViewById(R.id.tvFreezeBalance);
        imgBankIcon = (ImageView) findViewById(R.id.imgBankIcon);
        tvBankAccount = (TextView) findViewById(R.id.tvBankAccount);
        tvBankName = (TextView) findViewById(R.id.tvBankName);
        tvTradePassword = (EditText) findViewById(R.id.tvTradePassword);
        tvWithdrawalAmount = (EditText) findViewById(R.id.tvWithdrawalAmount);

        btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String paypassword = tvTradePassword.getText().toString();
                String money = tvWithdrawalAmount.getText().toString();

                if (TextUtils.isEmpty(paypassword)) {
                    Toast.makeText(MyWithdrawalActivity.this, "交易密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(money)) {
                    Toast.makeText(MyWithdrawalActivity.this, "提现金额不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                float amount = Float.parseFloat(money);
                if (amount < 50) {
                    Toast.makeText(MyWithdrawalActivity.this, "提现金额不能少于50元", Toast.LENGTH_SHORT).show();
                    return;
                }

                String balance = tvAvailableBalance.getText().toString();
                float availableBalance = Float.parseFloat(balance.substring(0, balance.indexOf("元")).trim());
                if (amount > availableBalance) {
                    Toast.makeText(MyWithdrawalActivity.this, "提现金额不能大于可用余额", Toast.LENGTH_SHORT).show();
                    return;
                }

                withdrawal(paypassword, money);
            }
        });
    }

    private void loadData() {
        getUser();
    }

    private void getUser() {
        ApiClient.getUser(TAG, new RefreshResponseListener() {
            @Override
            public void onResponse(Object response) {
                super.onResponse(response);

                User result = (User) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(MyWithdrawalActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }

                UserData data = result.data;
                if (data != null) {
                    String total = data.total;
                    String useMoney = data.use_money;
                    String noUseMoney = data.no_use_money;
                    total = TextUtils.isEmpty(total) ? "0" : total;
                    useMoney = TextUtils.isEmpty(useMoney) ? "0" : useMoney;
                    noUseMoney = TextUtils.isEmpty(noUseMoney) ? "0" : noUseMoney;
                    tvAccountBalance.setText(total + "  元");
                    tvAvailableBalance.setText(useMoney + "  元");
                    tvFreezeBalance.setText(noUseMoney + "  元");

                    getBank();
                }
            }
        });
    }

    private void getBank() {
        ApiClient.getBank(TAG, new ResponseListener() {
            @Override
            public void onStarted() {
                progressDialog = ProgressDialog.show(MyWithdrawalActivity.this, null, getString(R.string.message_waiting));
            }

            @Override
            public void onResponse(Object response) {
                progressDialog.dismiss();

                Bank result = (Bank) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(MyWithdrawalActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }

                BankData data = result.data;
                if (data != null) {

                    tvRealName.setText(DataUtil.urlDecode(data.realname));

                    if (!TextUtils.isEmpty(data.banksname)){
                        int resId = Constant.bankImgMap.get(DataUtil.urlDecode(data.banksname));
                        imgBankIcon.setImageResource(resId);

                        tvBankAccount.setText(DataUtil.urlDecode(data.account));
                        tvBankName.setText(DataUtil.urlDecode(data.banksname));
                    }
                    System.out.println("branch " + DataUtil.urlDecode(data.branch));
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
            }
        });
    }

    private void withdrawal(String paypassword, String money) {
        ApiClient.withdrawal(TAG, paypassword, money, new ResponseListener() {
            @Override
            public void onStarted() {
                progressDialog = ProgressDialog.show(MyWithdrawalActivity.this, null, getString(R.string.message_submit));
            }

            @Override
            public void onResponse(Object response) {
                progressDialog.dismiss();

                BaseModel result = (BaseModel) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(MyWithdrawalActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }

                String message = result.message;
                if (message.equals("paypsw_notsuit")) {
                    Toast.makeText(MyWithdrawalActivity.this, "交易密码错误", Toast.LENGTH_SHORT).show();
                    return;
                } else if (message.equals("no_account")) {
                    Toast.makeText(MyWithdrawalActivity.this, "银行账号还没填写", Toast.LENGTH_SHORT).show();
                    return;
                } else if (message.equals("post_success")) {
                    Toast.makeText(MyWithdrawalActivity.this, "提现成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
            }
        });
    }


    private abstract class RefreshResponseListener implements ResponseListener {

        @Override
        public void onStarted() {
            progressDialog = ProgressDialog.show(MyWithdrawalActivity.this, null, getString(R.string.message_waiting));
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
