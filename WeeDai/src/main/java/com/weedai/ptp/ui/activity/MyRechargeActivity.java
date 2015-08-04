package com.weedai.ptp.ui.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.constant.Urls;
import com.weedai.ptp.model.Bank;
import com.weedai.ptp.model.BaseModel;
import com.weedai.ptp.model.Valicode;
import com.weedai.ptp.utils.AppUtil;
import com.weedai.ptp.utils.DataUtil;
import com.weedai.ptp.utils.UIHelper;
import com.weedai.ptp.view.SimpleValidateCodeView;
import com.weedai.ptp.volley.ResponseListener;

public class MyRechargeActivity extends Activity {

    private final static String TAG = "MyRechargeActivity";


    //线下充值
    private TextView tvRealName;
    private TextView tvAccount;
    private EditText etAmount;
    private EditText etValicode;
    private EditText etWaterAccount;
    private Button btnOk;

    private SimpleValidateCodeView viewValicode;
    private String valicode;

    //在线充值
    private Button btnOnline;
    private TextView tvRealNameOnline;
    private EditText etValicodeOnline;
    private SimpleValidateCodeView viewValicodeOnline;
    private EditText etAmountOnline;


    private ProgressDialog progressDialog;

    private ImageView imgBack;
    private Button btnOfflineTopUp;
    private Button btnOnlineTopUp;

    private View layoutOffline;
    private View layoutOnline;

    private WebView webView;

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

        imgBack = (ImageView) findViewById(R.id.imgBack);
        btnOfflineTopUp = (Button) findViewById(R.id.btnOfflineTopUp);
        btnOnlineTopUp = (Button) findViewById(R.id.btnOnlineTopUp);
        layoutOffline = findViewById(R.id.layoutOffline);
        layoutOnline = findViewById(R.id.layoutOnline);

        layoutOffline.setVisibility(View.GONE);
        layoutOnline.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnOnlineTopUp.setSelected(true);
        btnOfflineTopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                layoutOffline.setVisibility(View.VISIBLE);
                layoutOnline.setVisibility(View.GONE);

                btnOfflineTopUp.setSelected(true);
                btnOnlineTopUp.setSelected(false);

                getImgcode();
            }
        });
        btnOnlineTopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutOffline.setVisibility(View.GONE);
                layoutOnline.setVisibility(View.VISIBLE);

                btnOfflineTopUp.setSelected(false);
                btnOnlineTopUp.setSelected(true);

                getImgcodeOnline();
            }
        });

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

        tvRealNameOnline = (TextView) findViewById(R.id.tvRealNameOnline);
        etValicodeOnline = (EditText) findViewById(R.id.etValicodeOnline);
        viewValicodeOnline = (SimpleValidateCodeView) findViewById(R.id.viewValicodeOnline);
        viewValicodeOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImgcodeOnline();
            }
        });
        etAmountOnline = (EditText) findViewById(R.id.etAmountOnline);
        btnOnline = (Button) findViewById(R.id.btnOnline);
        btnOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String money = etAmountOnline.getText().toString();
                String code = etValicodeOnline.getText().toString();

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
                        getImgcodeOnline();
                        etValicodeOnline.getText().clear();
                        return;
                    }
                }
//                rechargeOnline(money, code);
//                ApiClient.getSignatureMap();

                if (money.equals("0")) {
                    Toast.makeText(MyRechargeActivity.this, "充值金额不能为零", Toast.LENGTH_SHORT).show();
                    return;
                }


                long time = System.currentTimeMillis();
                String timestamp = String.valueOf(time);
                String signature = AppUtil.getSignature(timestamp);
                String url = Urls.ACTION_INDEX + "?" + "actions=yepoorecharge" + "&money=" + money + "&valicode=" + code + "&signature=" + signature + "&timestamp=" + timestamp;

                System.out.println("online url " + url);

                UIHelper.showRechargeOnline(MyRechargeActivity.this, url);

            }
        });

    }

    private void loadData() {
        getBank();
        getImgcodeOnline();
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

    private void rechargeOnline(String money, String valicode) {

        ApiClient.rechargeOnline(TAG, money, valicode, new ResponseListener() {
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
                    Toast.makeText(MyRechargeActivity.this, "在线充值成功", Toast.LENGTH_SHORT).show();
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

                    tvRealNameOnline.setText(DataUtil.urlDecode(result.data.realname));
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

    private void getImgcodeOnline() {

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
//                viewValicode.getValidataAndSetImage(codes);
                viewValicodeOnline.getValidataAndSetImage(codes);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
    }


}


