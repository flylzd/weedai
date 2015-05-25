package com.weedai.ptp.ui.activity;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.model.BaseModel;
import com.weedai.ptp.model.SecurityPhone;
import com.weedai.ptp.utils.UIHelper;
import com.weedai.ptp.volley.ResponseListener;

import java.net.URLEncoder;

public class RegisterActivity extends BaseActivity {

    private final static String TAG = "RegisterActivity";

    private EditText etEmail;
    private EditText etUsername;
    private EditText etNewPassword;
    private EditText etConfirmPassword;
    private EditText etRealName;
    private RadioButton rbMale;
    private RadioButton rbFeMale;
    private Button btnRegister;

    private String email;
    private String username;
    private String password;
    private String confirmPassword;
    private String realName;
    private int sex = 1; //1为男2为女

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.register_title;
    }

    private void initView() {

        etEmail = (EditText) findViewById(R.id.etEmail);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etNewPassword = (EditText) findViewById(R.id.etNewPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        etRealName = (EditText) findViewById(R.id.etRealName);

        rbMale = (RadioButton) findViewById(R.id.rbMale);
        rbFeMale = (RadioButton) findViewById(R.id.rbFeMale);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = etEmail.getText().toString();
                username = etUsername.getText().toString();
                password = etNewPassword.getText().toString();
                confirmPassword = etConfirmPassword.getText().toString();
                realName = etRealName.getText().toString();

                if (rbMale.isChecked()) {
                    sex = 1;
                } else if (rbFeMale.isChecked()) {
                    sex = 2;
                }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(RegisterActivity.this, "邮件地址不能为空", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (!email.matches("\\w+@\\w+\\.\\w+")) {
                        Toast.makeText(RegisterActivity.this, "邮箱格式不正确", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(RegisterActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(realName)) {
                    Toast.makeText(RegisterActivity.this, "真实姓名名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "登录密码和确认密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }

                checkUsername(username);

            }
        });
    }

    private void checkUsername(final String username) {
        ApiClient.checkUsername(TAG, username, new ResponseListener() {
            @Override
            public void onStarted() {
                progressDialog = ProgressDialog.show(RegisterActivity.this, null, getString(R.string.message_submit));
            }

            @Override
            public void onResponse(Object response) {
                progressDialog.dismiss();
                BaseModel result = (BaseModel) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    if (result.message.equals("reg_fail")) {
                        Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    }
                    return;
                }

                if (result.message.equals("user_not_exist")) {
                    realName = URLEncoder.encode(realName);
                    register(email, username, password, realName, sex);  //注册
                } else {
                    Toast.makeText(RegisterActivity.this, "用户名已存在,请重新输入", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
            }
        });
    }


    private void register(String email, String username, final String password, String realname, int sex) {

        ApiClient.register(TAG, email, username, password, realname, sex, new ResponseListener() {
            @Override
            public void onStarted() {
                progressDialog = ProgressDialog.show(RegisterActivity.this, null, getString(R.string.message_submit));
            }

            @Override
            public void onResponse(Object response) {
                progressDialog.dismiss();

                BaseModel result = (BaseModel) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(RegisterActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (result.message.equals("reg_suc")) {
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    finish();
                    UIHelper.showLogin(RegisterActivity.this);
                } else {
                    Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
            }
        });
    }


}
