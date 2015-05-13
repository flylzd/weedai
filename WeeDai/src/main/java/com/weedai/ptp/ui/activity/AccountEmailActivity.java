package com.weedai.ptp.ui.activity;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Constant;
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

        getImgcode();  //获取验证码
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
