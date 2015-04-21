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
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.model.Valicode;
import com.weedai.ptp.utils.UIHelper;
import com.weedai.ptp.view.SimpleValidateCodeView;
import com.weedai.ptp.view.ValidateCodeView;
import com.weedai.ptp.volley.ResponseListener;

import java.util.Arrays;

public class LoginActivity extends Activity implements View.OnClickListener {

    private final static String TAG = "LoginActivity";

    private EditText etUsername;
    private EditText etPassword;
    private EditText etValicode;
    private SimpleValidateCodeView viewValicode;
    private Button btnLogin;

    private String valicode;

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
        etValicode = (EditText) findViewById(R.id.etValicode);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        tvLoginRegister = (TextView) findViewById(R.id.tvLoginRegister);
        tvLoginForgetPassword = (TextView) findViewById(R.id.tvLoginForgetPassword);

        btnLogin.setOnClickListener(this);
        tvLoginRegister.setOnClickListener(this);
        tvLoginForgetPassword.setOnClickListener(this);

        viewValicode = (SimpleValidateCodeView) findViewById(R.id.viewValicode);
//        viewValicode.getValidataAndSetImage(new String[]{"4","7","0","2"});
        viewValicode.setOnClickListener(this);

        getImgcode();  //获取验证码
    }

    private void login() {

        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        String code = etValicode.getText().toString();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(LoginActivity.this, getString(R.string.login_user_empty), Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, getString(R.string.login_password_empty), Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(code)) {
            Toast.makeText(LoginActivity.this, getString(R.string.login_valicode_empty), Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (!valicode.equals(code)) {
                Toast.makeText(LoginActivity.this, getString(R.string.login_valicode_not_match), Toast.LENGTH_SHORT).show();
                return;
            }
        }

        ApiClient.login(TAG, username, password, code, new ResponseListener() {
            @Override
            public void onStarted() {
                progressDialog = ProgressDialog.show(LoginActivity.this, null, getString(R.string.login_waiting));
            }

            @Override
            public void onResponse(Object response) {
                progressDialog.dismiss();

                //                UIHelper.showMain(LoginActivity.this);
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
                    Toast.makeText(LoginActivity.this, result.message, Toast.LENGTH_SHORT).show();
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


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnLogin:
//                UIHelper.showMain(LoginActivity.this);
                login();
                break;
            case R.id.tvLoginRegister:
                UIHelper.showRegister(LoginActivity.this);
                break;
            case R.id.tvLoginForgetPassword:
                break;
            case R.id.viewValicode:
//                viewValicode.getValidataAndSetImage(new String[]{"2", "7", "1", "9"});
                getImgcode();
                break;
        }
    }
}
