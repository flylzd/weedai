package com.weedai.ptp.ui.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.weedai.ptp.model.Valicode;
import com.weedai.ptp.utils.DataUtil;
import com.weedai.ptp.view.SimpleValidateCodeView;
import com.weedai.ptp.volley.ResponseListener;

public class MyRechargeActivity extends Activity {

    private final static String TAG = "MyRechargeActivity";

    private TextView tvRealName;
    private TextView tvAccount;
    private EditText etAmount;
    private EditText etValicode;
    private EditText etWaterAccount;
    private Button btnOk;

    private SimpleValidateCodeView viewValicode;
    private String valicode;

    private ProgressDialog progressDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my_recharge);

        initView();
        loadData();
    }

//    @Override
//    protected int getActionBarTitle() {
//        return R.string.title_my_recharge;
//    }
//
//    @Override
//    protected boolean hasBackButton() {
//        return true;
//    }


    private void initView() {

        tvRealName = (TextView) findViewById(R.id.tvRealName);
        tvAccount = (TextView) findViewById(R.id.tvAccount);
        etAmount = (EditText) findViewById(R.id.etAmount);
        etValicode = (EditText) findViewById(R.id.etValicode);
        etWaterAccount = (EditText) findViewById(R.id.etWaterAccount);
        viewValicode = (SimpleValidateCodeView) findViewById(R.id.viewValicode);
        viewValicode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImgcode();
            }
        });
        btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String money = etAmount.getText().toString();
                String code = etValicode.getText().toString();
                String remark = etWaterAccount.getText().toString();

                if (TextUtils.isEmpty(money)) {
                    Toast.makeText(MyRechargeActivity.this, "请填写充值金额", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(MyRechargeActivity.this, getString(R.string.login_valicode_empty), Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (!valicode.equalsIgnoreCase(code)) {
                        Toast.makeText(MyRechargeActivity.this, getString(R.string.login_valicode_not_match), Toast.LENGTH_SHORT).show();
                        getImgcode();
                        etValicode.getText().clear();
                        return;
                    }
                }

                recharge(money, code, remark);
            }
        });

    }

    private void loadData() {
        getBank();
        getImgcode();
    }

    private void recharge(String money, String valicode, String remark) {

        ApiClient.recharge(TAG, money, valicode, remark, new ResponseListener() {
            @Override
            public void onStarted() {
                progressDialog = ProgressDialog.show(MyRechargeActivity.this, null, getString(R.string.message_submit));

            }

            @Override
            public void onResponse(Object response) {
                progressDialog.dismiss();

                BaseModel result = (BaseModel) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(MyRechargeActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (result.message.equals("sendsuccess")) {
                    Toast.makeText(MyRechargeActivity.this, "线下充值成功", Toast.LENGTH_SHORT).show();
                    MyRechargeActivity.this.finish();
                } else if (result.message.equals("moneyerror")) {
                    Toast.makeText(MyRechargeActivity.this, "金额错误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
            }
        });
    }

    private void getBank() {
        ApiClient.getBank(TAG, new ResponseListener() {
            @Override
            public void onStarted() {
                progressDialog = ProgressDialog.show(MyRechargeActivity.this, null, getString(R.string.message_waiting));
            }

            @Override
            public void onResponse(Object response) {
                progressDialog.dismiss();

                Bank result = (Bank) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(MyRechargeActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (result.data != null) {
                    tvRealName.setText(DataUtil.urlDecode(result.data.realname));
                    tvAccount.setText(result.data.account);
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
            }
        });
    }

    private void getImgcode() {

        ApiClient.getImgcode(TAG, new ResponseListener() {
            @Override
            public void onStarted() {
            }

            @Override
            public void onResponse(Object response) {

                Valicode result = (Valicode) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(MyRechargeActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }
                valicode = result.data.code;
                System.out.println("valicode : " + valicode);
                String code = result.data.code;
                int length = code.length();
                String[] codes = new String[length];
                for (int i = 0; i < length; i++) {
                    codes[i] = String.valueOf(code.charAt(i));
                }
                viewValicode.getValidataAndSetImage(codes);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
    }


}


