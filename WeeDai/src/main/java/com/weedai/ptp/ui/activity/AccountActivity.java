package com.weedai.ptp.ui.activity;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Config;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.model.BaseModel;
import com.weedai.ptp.utils.UIHelper;
import com.weedai.ptp.volley.ResponseListener;

public class AccountActivity extends BaseActivity implements View.OnClickListener {

    private final static String TAG = "AccountActivity";

    private TextView tvRealName;
    private TextView tvSex;

    private RelativeLayout layoutAccount;
    private RelativeLayout layoutAccountDateBirth;
    private RelativeLayout layoutAccountPhone;
    private RelativeLayout layoutAccountEmail;
    private RelativeLayout layoutAccountModifyLoginPassword;
    private RelativeLayout layoutAccountPasswordProtection;
    private RelativeLayout layoutAccountPasswordManagementGestures;
    private Button btnExit;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        initView();
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_account;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    private void initView() {

        tvRealName = (TextView) findViewById(R.id.tvRealName);
        tvSex = (TextView) findViewById(R.id.tvSex);

        layoutAccount = (RelativeLayout) findViewById(R.id.layoutAccount);
        layoutAccountDateBirth = (RelativeLayout) findViewById(R.id.layoutAccountDateBirth);
        layoutAccountPhone = (RelativeLayout) findViewById(R.id.layoutAccountPhone);
        layoutAccountEmail = (RelativeLayout) findViewById(R.id.layoutAccountEmail);
        layoutAccountModifyLoginPassword = (RelativeLayout) findViewById(R.id.layoutAccountModifyLoginPassword);
        layoutAccountPasswordProtection = (RelativeLayout) findViewById(R.id.layoutAccountPasswordProtection);
        layoutAccountPasswordManagementGestures = (RelativeLayout) findViewById(R.id.layoutAccountPasswordManagementGestures);
        btnExit = (Button) findViewById(R.id.btnExit);
        layoutAccountDateBirth.setOnClickListener(this);
        layoutAccount.setOnClickListener(this);
        layoutAccountPhone.setOnClickListener(this);
        layoutAccountEmail.setOnClickListener(this);
        layoutAccountModifyLoginPassword.setOnClickListener(this);
        layoutAccountPasswordProtection.setOnClickListener(this);
        layoutAccountPasswordManagementGestures.setOnClickListener(this);
        btnExit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layoutAccount:
                UIHelper.showAccountAvatars(AccountActivity.this);
                break;
            case R.id.layoutAccountDateBirth:
                break;
            case R.id.layoutAccountPhone:
                break;
            case R.id.layoutAccountEmail:
                break;
            case R.id.layoutAccountModifyLoginPassword:
                UIHelper.showChangePassword(AccountActivity.this);
                break;
            case R.id.layoutAccountPasswordProtection:
                break;
            case R.id.layoutAccountPasswordManagementGestures:
                break;
            case R.id.btnExit:
                AlertDialog.Builder builder = new AlertDialog.Builder(AccountActivity.this);
                builder.setTitle("提示");
                builder.setMessage("是否安全退出");
                builder.setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                });
                builder.show();
                break;
        }
    }

    private void logout() {
        ApiClient.logout(TAG, new ResponseListener() {
            @Override
            public void onStarted() {
                progressDialog = ProgressDialog.show(AccountActivity.this, null, getString(R.string.message_waiting));
            }

            @Override
            public void onResponse(Object response) {
                progressDialog.dismiss();

                BaseModel result = (BaseModel) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(AccountActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (result.message.equals("logout_success")) {
                    Config.isLogin = false;
                    finish();
                } else {
                    Toast.makeText(AccountActivity.this, "安全退出失败", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
            }
        });
    }
}
