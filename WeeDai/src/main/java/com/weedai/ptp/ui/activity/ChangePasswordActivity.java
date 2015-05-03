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
import com.weedai.ptp.volley.ResponseListener;

public class ChangePasswordActivity extends BaseActivity {

    private static final String TAG = "ChangePasswordActivity";

    private EditText etOldPassword;
    private EditText etNewPassword;
    private EditText etConfirmPassword;
    private EditText etVerificationCode;
    private Button btnVerificationCode;
    private Button btnOk;

    private int time = 60;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            time--;
            btnVerificationCode.setEnabled(false);
            btnVerificationCode.setText("稍后重试(" + time + ")");
            if (time < 0) {
                btnVerificationCode.setEnabled(true);
                btnVerificationCode.setText("发送手机验证码");
                return;
            }
            handler.postDelayed(runnable, 1000);
        }
    };

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        initView();
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_change_login_password;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    private void initView() {

        etOldPassword = (EditText) findViewById(R.id.etOldPassword);
        etNewPassword = (EditText) findViewById(R.id.etNewPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        etVerificationCode = (EditText) findViewById(R.id.etVerificationCode);
        btnVerificationCode = (Button) findViewById(R.id.btnVerificationCode);
        btnOk = (Button) findViewById(R.id.btnOk);

        btnVerificationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone = etVerificationCode.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(ChangePasswordActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (phone.length() != 11) {
                    Toast.makeText(ChangePasswordActivity.this, "手机号输入有误", Toast.LENGTH_SHORT).show();
                    return;
                }

                getPhoneVerificationCode(phone);

                handler.postDelayed(runnable, 1000);

            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String oldPassword = etOldPassword.getText().toString();
                String newPassword = etNewPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();

                if (TextUtils.isEmpty(oldPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(ChangePasswordActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(ChangePasswordActivity.this, "新密码和确认密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }

                changePassowrd(oldPassword, newPassword);

            }
        });
    }

    private void changePassowrd(String oldPassword, String newPassword) {
        ApiClient.changePassword(TAG, oldPassword, newPassword, new ResponseListener() {
            @Override
            public void onStarted() {
                progressDialog = ProgressDialog.show(ChangePasswordActivity.this, null, "正在修改密码...");
            }

            @Override
            public void onResponse(Object response) {
                progressDialog.dismiss();
                SecurityPhone result = (SecurityPhone) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(ChangePasswordActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (result.message.equals("pasw_editsuccess")) {
                    Toast.makeText(ChangePasswordActivity.this, "密码修改成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "密码修改失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
            }
        });
    }

    private void getPhoneVerificationCode(String phonenum) {
        ApiClient.getPhoneVerificationCode(TAG, phonenum, new ResponseListener() {
            @Override
            public void onStarted() {
                progressDialog = ProgressDialog.show(ChangePasswordActivity.this, null, "获取手机验证码");
            }

            @Override
            public void onResponse(Object response) {
                progressDialog.dismiss();

                SecurityPhone result = (SecurityPhone) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(ChangePasswordActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }
                SecurityPhoneData data = result.data;
                int id = data.id;
                if (id == 2 || id == 4) {

                } else if (id == 3) {
                    Toast.makeText(ChangePasswordActivity.this, "手机验证码获取失败", Toast.LENGTH_SHORT).show();
                } else if (id == 1) {
                    Toast.makeText(ChangePasswordActivity.this, result.message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
            }
        });
    }
}
