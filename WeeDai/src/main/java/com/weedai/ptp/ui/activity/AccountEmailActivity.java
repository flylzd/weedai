package com.weedai.ptp.ui.activity;


import android.app.ProgressDialog;
import android.os.Bundle;
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
import com.weedai.ptp.model.Valicode;
import com.weedai.ptp.view.SimpleValidateCodeView;
import com.weedai.ptp.volley.ResponseListener;

public class AccountEmailActivity extends BaseActivity {

    private final static String TAG = "AccountEmailActivity";

    private EditText etEmail;
    private EditText etVerificationCode;
    private SimpleValidateCodeView viewValicode;
    private Button btnOk;

    private String valicode;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_email);

        initView();

//        getImgcode();  //获取验证码
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_account_email;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    private void initView() {

        etEmail = (EditText) findViewById(R.id.etEmail);
        etVerificationCode = (EditText) findViewById(R.id.etVerificationCode);
        viewValicode = (SimpleValidateCodeView) findViewById(R.id.viewValicode);
        btnOk = (Button) findViewById(R.id.btnOk);

        viewValicode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImgcode();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = etEmail.getText().toString();
                String code = etVerificationCode.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(AccountEmailActivity.this, "邮箱不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

//                if (TextUtils.isEmpty(code)) {
//                    Toast.makeText(AccountEmailActivity.this, getString(R.string.login_valicode_empty), Toast.LENGTH_SHORT).show();
//                    return;
//                } else {
//                    if (!valicode.equals(code)) {
//                        Toast.makeText(AccountEmailActivity.this, getString(R.string.login_valicode_not_match), Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                }
                bindingEmail(email, code);

            }
        });

    }

    private void bindingEmail(String email, String valicodes) {
        ApiClient.bindingEmail(TAG, email, new ResponseListener() {
            @Override
            public void onStarted() {
                progressDialog = ProgressDialog.show(AccountEmailActivity.this, null, "正在验证邮箱");
            }

            @Override
            public void onResponse(Object response) {
                progressDialog.dismiss();

                SecurityPhone result = (SecurityPhone) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(AccountEmailActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }
                SecurityPhoneData data = result.data;
                String message = result.message;
                if (message.equals("sendsuccess")) {
                    Toast.makeText(AccountEmailActivity.this, "邮箱验证成功", Toast.LENGTH_SHORT).show();
                    User.userInfo.phone_status = 1;
                    finish();
                } else if (message.equals("emailexist ")) {
                    Toast.makeText(AccountEmailActivity.this, "邮箱已经存在", Toast.LENGTH_SHORT).show();
                } else if (message.equals("aftertwo")) {
                    Toast.makeText(AccountEmailActivity.this, "请两分钟后再发", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AccountEmailActivity.this, "邮箱验证失败", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(AccountEmailActivity.this, result.message, Toast.LENGTH_SHORT).show();
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
