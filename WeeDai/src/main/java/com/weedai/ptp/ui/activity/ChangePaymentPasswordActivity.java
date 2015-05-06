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
import com.weedai.ptp.model.Valicode;
import com.weedai.ptp.utils.UIHelper;
import com.weedai.ptp.view.SimpleValidateCodeView;
import com.weedai.ptp.volley.ResponseListener;

/**
 * 提现密码修改
 */
public class ChangePaymentPasswordActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "ChangePaymentPasswordActivity";

    private EditText etOldPassword;
    private EditText etNewPassword;
    private EditText etConfirmPassword;
    private EditText etVerificationCode;
    private Button btnOk;

    private SimpleValidateCodeView viewValicode;

    private String valicode;


    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_payment_password);

        initView();

        getImgcode();  //获取验证码
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_change_payment_password;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.viewValicode:
//                viewValicode.getValidataAndSetImage(new String[]{"2", "7", "1", "9"});
                getImgcode();
                break;
        }
    }

    private void initView() {

        etOldPassword = (EditText) findViewById(R.id.etOldPassword);
        etNewPassword = (EditText) findViewById(R.id.etNewPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        etVerificationCode = (EditText) findViewById(R.id.etVerificationCode);
        btnOk = (Button) findViewById(R.id.btnOk);

        viewValicode = (SimpleValidateCodeView) findViewById(R.id.viewValicode);
        viewValicode.setOnClickListener(this);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String oldPassword = etOldPassword.getText().toString();
                String newPassword = etNewPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();
                String code = etVerificationCode.getText().toString();

                if (TextUtils.isEmpty(oldPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(ChangePaymentPasswordActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(ChangePaymentPasswordActivity.this, "新密码和确认密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(ChangePaymentPasswordActivity.this, getString(R.string.login_valicode_empty), Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (!valicode.equals(code)) {
                        Toast.makeText(ChangePaymentPasswordActivity.this, getString(R.string.login_valicode_not_match), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                changeaymentPassword(oldPassword, newPassword, valicode);
            }
        });
    }

    private void changeaymentPassword(String oldPassword, String newPassword, String valicode) {
        ApiClient.changeaymentPassword(TAG, oldPassword, newPassword, valicode, new ResponseListener() {
            @Override
            public void onStarted() {
                progressDialog = ProgressDialog.show(ChangePaymentPasswordActivity.this, null, "正在修改密码...");
            }

            @Override
            public void onResponse(Object response) {
                progressDialog.dismiss();
                SecurityPhone result = (SecurityPhone) response;
//                if (result.code != Constant.CodeResult.SUCCESS) {
//                    Toast.makeText(ChangePaymentPasswordActivity.this, result.message, Toast.LENGTH_SHORT).show();
//                    return;
//                }

                if (result.message.equals("valicode_notsuit")) {
                    Toast.makeText(ChangePaymentPasswordActivity.this, "验证码不正确", Toast.LENGTH_SHORT).show();
                   return;
                }

                if (result.message.equals("pasw_notsuit")) {
                    Toast.makeText(ChangePaymentPasswordActivity.this, "原密码不正确", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (result.message.equals("payedit_success")) {
                    Toast.makeText(ChangePaymentPasswordActivity.this, "提现密码修改成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ChangePaymentPasswordActivity.this, "提现密码修改失败", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ChangePaymentPasswordActivity.this, result.message, Toast.LENGTH_SHORT).show();
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
