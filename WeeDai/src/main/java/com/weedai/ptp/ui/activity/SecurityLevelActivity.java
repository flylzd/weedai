package com.weedai.ptp.ui.activity;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.model.User;
import com.weedai.ptp.model.UserData;
import com.weedai.ptp.utils.UIHelper;
import com.weedai.ptp.volley.ResponseListener;

public class SecurityLevelActivity extends BaseActivity implements View.OnClickListener {

    private final static String TAG = "SecurityLevelActivity";

    private View layoutSecurityName;
    private View layoutSecurityPhone;
    private View layoutSecurityPassword;

    private ImageView imgSecurityName;
    private ImageView imgSecurityPhone;
    private ImageView imgSecurityPassword;

    private TextView tvSecurityLevelTitle;
    private TextView tvSecurityName;
    private TextView tvSecurityPhone;
    private TextView tvSecurityPassword;

    private UserData data;
    private int level = 0;
    private String securityLevel;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_level);

        initView();
//        loadData();

    }


    @Override
    protected void onResume() {
        super.onResume();

        setInfo();
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_security_level;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    private void initView() {

        layoutSecurityName = findViewById(R.id.layoutSecurityName);
        layoutSecurityPhone = findViewById(R.id.layoutSecurityPhone);
        layoutSecurityPassword = findViewById(R.id.layoutSecurityPassword);
        layoutSecurityName.setOnClickListener(this);
        layoutSecurityPhone.setOnClickListener(this);
        layoutSecurityPassword.setOnClickListener(this);

        imgSecurityName = (ImageView) findViewById(R.id.imgSecurityName);
        imgSecurityPhone = (ImageView) findViewById(R.id.imgSecurityPhone);
        imgSecurityPassword = (ImageView) findViewById(R.id.imgSecurityPassword);

        tvSecurityLevelTitle = (TextView) findViewById(R.id.tvSecurityLevelTitle);
        tvSecurityName = (TextView) findViewById(R.id.tvSecurityName);
        tvSecurityPhone = (TextView) findViewById(R.id.tvSecurityPhone);
        tvSecurityPassword = (TextView) findViewById(R.id.tvSecurityPassword);

    }

    private void setInfo() {

        data = User.userInfo;
        if (data != null) {
            level = 0;
            if (data.real_status == 1) {
                imgSecurityName.setImageResource(R.drawable.icon_security_set);
                level++;
            } else {
                imgSecurityName.setImageResource(R.drawable.icon_security_not_set);
            }

            if (data.phone_status == 1) {
                imgSecurityPhone.setImageResource(R.drawable.icon_security_set);
                level++;
                tvSecurityPhone.setText(data.phone);
            } else {
                imgSecurityPhone.setImageResource(R.drawable.icon_security_not_set);
                tvSecurityPhone.setText(data.phone);
            }

            if (!TextUtils.isEmpty(data.paypassword)) {
                imgSecurityPassword.setImageResource(R.drawable.icon_security_set);
                level++;
                tvSecurityPassword.setText("已经设置，点击修改");
            } else {
                imgSecurityPassword.setImageResource(R.drawable.icon_security_not_set);
            }
            if (level == 0) {
                securityLevel = "低";
            } else if (level == 1) {
                securityLevel = "低";
            } else if (level == 2) {
                securityLevel = "中";
            } else if (level == 3) {
                securityLevel = "高";
            }
            String securityLevelTitle = String.format(getString(R.string.user_security_level), securityLevel);
            tvSecurityLevelTitle.setText(Html.fromHtml(securityLevelTitle));
        }

    }

    private void loadData() {
        ApiClient.getSecurityLevel(TAG, new ResponseListener() {
            @Override
            public void onStarted() {
                progressDialog = ProgressDialog.show(SecurityLevelActivity.this, null, getString(R.string.message_waiting));
            }

            @Override
            public void onResponse(Object response) {
                progressDialog.dismiss();

                User result = (User) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(SecurityLevelActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }


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
            case R.id.layoutSecurityName:
                break;
            case R.id.layoutSecurityPhone:
                UIHelper.showSecurityPhone(SecurityLevelActivity.this);
                break;
            case R.id.layoutSecurityPassword:
                break;
        }
    }
}
