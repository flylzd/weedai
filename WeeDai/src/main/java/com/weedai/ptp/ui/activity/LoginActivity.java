package com.weedai.ptp.ui.activity;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.weedai.ptp.R;
import com.weedai.ptp.utils.UIHelper;

public class LoginActivity extends Activity implements View.OnClickListener {

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;

    private TextView tvLoginRegister;
    private TextView tvLoginForgetPassword;

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
