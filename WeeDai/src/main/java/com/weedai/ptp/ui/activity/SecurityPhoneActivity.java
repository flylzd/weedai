package com.weedai.ptp.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.model.SecurityPhone;
import com.weedai.ptp.model.SecurityPhoneData;
import com.weedai.ptp.model.User;
import com.weedai.ptp.volley.ResponseListener;


public class SecurityPhoneActivity extends BaseActivity {

    private static final String TAG = "SecurityPhoneActivity";

    private EditText etSecurityPhone;
    private EditText etSecurityVerificationCode;
    private Button btnSecurityVerificationCode;
    private Button btnOk;

    private ProgressDialog progressDialog;

    private int time = 60;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            time--;
            btnSecurityVerificationCode.setEnabled(false);
            btnSecurityVerificationCode.setText("稍后重试(" + time + ")");
            if (time < 0) {
                btnSecurityVerificationCode.setEnabled(true);
                btnSecurityVerificationCode.setText("发送手机验证码");
                return;
            }
            handler.postDelayed(runnable, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_phone);

        initView();
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_security_phone;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    private void initView() {

        etSecurityPhone = (EditText) findViewById(R.id.etSecurityPhone);
        etSecurityVerificationCode = (EditText) findViewById(R.id.etSecurityVerificationCode);
        btnSecurityVerificationCode = (Button) findViewById(R.id.btnSecurityVerificationCode);
        btnOk = (Button) findViewById(R.id.btnOk);

        btnSecurityVerificationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone = etSecurityPhone.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(SecurityPhoneActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (phone.length() != 11) {
                    Toast.makeText(SecurityPhoneActivity.this, "手机号输入有误", Toast.LENGTH_SHORT).show();
                    return;
                }

                getPhoneVerificationCode(phone);

                handler.postDelayed(runnable, 1000);

            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone = etSecurityPhone.getText().toString();
                String valicodes = etSecurityVerificationCode.getText().toString();
                if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(valicodes)) {
                    Toast.makeText(SecurityPhoneActivity.this, "手机号或者验证码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                bindingPhone(phone, valicodes);
            }
        });
    }

    private void getPhoneVerificationCode(String phonenum) {
        ApiClient.getPhoneVerificationCode(TAG, phonenum, new ResponseListener() {
            @Override
            public void onStarted() {
                progressDialog = ProgressDialog.show(SecurityPhoneActivity.this, null, "获取手机验证码");
            }

            @Override
            public void onResponse(Object response) {
                progressDialog.dismiss();

                SecurityPhone result = (SecurityPhone) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(SecurityPhoneActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }
                SecurityPhoneData data = result.data;
                int id = data.id;
                if (id == 2 || id == 4) {

                } else if (id == 3) {
                    Toast.makeText(SecurityPhoneActivity.this, "手机验证码获取失败", Toast.LENGTH_SHORT).show();
                } else if (id == 1) {
                    Toast.makeText(SecurityPhoneActivity.this, result.message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
            }
        });
    }

    private void bindingPhone(String phone, String valicodes) {
        ApiClient.bindingPhone(TAG, phone, valicodes, new ResponseListener() {
            @Override
            public void onStarted() {
                progressDialog = ProgressDialog.show(SecurityPhoneActivity.this, null, "正在绑定手机");
            }

            @Override
            public void onResponse(Object response) {
                progressDialog.dismiss();

                SecurityPhone result = (SecurityPhone) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(SecurityPhoneActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }
                SecurityPhoneData data = result.data;

            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
            }
        });
    }

}
