package com.weedai.ptp.ui.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.utils.UIHelper;
import com.weedai.ptp.volley.ResponseListener;

public class LoginActivity extends Activity implements View.OnClickListener {

    private final static String TAG = "LoginActivity";

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;

    private TextView tvLoginRegister;
    private TextView tvLoginForgetPassword;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        initView();

    }

    private void initView() {

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        tvLoginRegister = (TextView) findViewById(R.id.tvLoginRegister);
        tvLoginForgetPassword = (TextView) findViewById(R.id.tvLoginForgetPassword);

        btnLogin.setOnClickListener(this);
        tvLoginRegister.setOnClickListener(this);
        tvLoginForgetPassword.setOnClickListener(this);
    }

    private void login() {


        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(LoginActivity.this, getString(R.string.login_user_empty), Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, getString(R.string.login_password_empty), Toast.LENGTH_SHORT).show();
            return;
        }

        ApiClient.login(TAG, username, password, new ResponseListener() {
            @Override
            public void onStarted() {
                progressDialog = ProgressDialog.show(LoginActivity.this, null, getString(R.string.login_waiting));
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


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnLogin:
                UIHelper.showMain(LoginActivity.this);
                break;
            case R.id.tvLoginRegister:
                UIHelper.showRegister(LoginActivity.this);
                break;
            case R.id.tvLoginForgetPassword:
                break;
        }
    }
}
