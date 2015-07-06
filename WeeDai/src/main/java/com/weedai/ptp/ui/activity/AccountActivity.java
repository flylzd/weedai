package com.weedai.ptp.ui.activity;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Config;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.constant.Urls;
import com.weedai.ptp.model.BaseModel;
import com.weedai.ptp.model.User;
import com.weedai.ptp.model.UserData;
import com.weedai.ptp.utils.DataUtil;
import com.weedai.ptp.utils.UIHelper;
import com.weedai.ptp.volley.ResponseListener;

public class AccountActivity extends BaseActivity implements View.OnClickListener {

    private final static String TAG = "AccountActivity";

    private ImageView imgAvatar;
    private TextView tvRealName;
    private TextView tvSex;

    private RelativeLayout layoutAccount;
    private RelativeLayout layoutAccountDateBirth;
    private RelativeLayout layoutAccountPhone;
    private RelativeLayout layoutAccountEmail;
    private RelativeLayout layoutAccountModifyLoginPassword;
    private RelativeLayout layoutAccountPasswordProtection;
    private RelativeLayout layoutPasswordGestures;
    private Button btnExit;

    private TextView tvAccountPasswordDateBirth;
    private TextView tvAccountPhone;
    private TextView tvAccountEmail;

    private static UserData data;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        initView();
//        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadData();
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

        layoutAccount = (RelativeLayout) findViewById(R.id.layoutAccount);
        layoutAccountDateBirth = (RelativeLayout) findViewById(R.id.layoutAccountDateBirth);
        layoutAccountPhone = (RelativeLayout) findViewById(R.id.layoutAccountPhone);
        layoutAccountEmail = (RelativeLayout) findViewById(R.id.layoutAccountEmail);
        layoutAccountModifyLoginPassword = (RelativeLayout) findViewById(R.id.layoutAccountModifyLoginPassword);
        layoutAccountPasswordProtection = (RelativeLayout) findViewById(R.id.layoutAccountPasswordProtection);
        layoutPasswordGestures = (RelativeLayout) findViewById(R.id.layoutPasswordGestures);
        btnExit = (Button) findViewById(R.id.btnExit);
        layoutAccountDateBirth.setOnClickListener(this);
        layoutAccount.setOnClickListener(this);
        layoutAccountPhone.setOnClickListener(this);
        layoutAccountEmail.setOnClickListener(this);
        layoutAccountModifyLoginPassword.setOnClickListener(this);
        layoutAccountPasswordProtection.setOnClickListener(this);
        layoutPasswordGestures.setOnClickListener(this);
        btnExit.setOnClickListener(this);

        imgAvatar = (ImageView) findViewById(R.id.imgAvatar);
        tvRealName = (TextView) findViewById(R.id.tvRealName);
        tvSex = (TextView) findViewById(R.id.tvSex);

        tvAccountPasswordDateBirth = (TextView) findViewById(R.id.tvAccountPasswordDateBirth);
        tvAccountPhone = (TextView) findViewById(R.id.tvAccountPhone);
        tvAccountEmail = (TextView) findViewById(R.id.tvAccountEmail);
    }

    private void loadData() {
        getUser();
    }

    private void getUser() {
        ApiClient.getUser(TAG, new RefreshResponseListener() {
            @Override
            public void onResponse(Object response) {
                super.onResponse(response);

                User result = (User) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(AccountActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }
                User.userInfo = result.data;
                data = User.userInfo;
                setUserInfo();

//                getAvatars();  //获取头像
            }
        });
    }

    private void getAvatars() {
        ApiClient.getAvatars(TAG, new RefreshResponseListener() {
            @Override
            public void onResponse(Object response) {
                super.onResponse(response);

                User result = (User) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(AccountActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }

                System.out.println("头像 == " + DataUtil.urlDecode(result.data.touxiang));
//                User.userInfo.touxiang = DataUtil.urlDecode(result.data.touxiang);
                String url = DataUtil.urlDecode(result.data.touxiang);
                if (!TextUtils.isEmpty(url)) {
                    url = Urls.SERVER_URL + url;
                    ImageLoader.getInstance().displayImage(url, imgAvatar);
                }
            }
        });
    }

    private void setUserInfo() {
        String url = User.userInfo.touxiang;
        if (!TextUtils.isEmpty(url)) {
            url = Urls.SERVER_URL + url;
            ImageLoader.getInstance().displayImage(url, imgAvatar);
        }

        tvRealName.setText("真实姓名: " + DataUtil.urlDecode(User.userInfo.realname));
        String sex = User.userInfo.sex == 1 ? "男" : "女";
        tvSex.setText("性别:  " + sex);
//        tvAccountPasswordDateBirth
        tvAccountPhone.setText(User.userInfo.phone);
        tvAccountEmail.setText(User.userInfo.email);
    }

    private abstract class RefreshResponseListener implements ResponseListener {

        @Override
        public void onStarted() {
            progressDialog = ProgressDialog.show(AccountActivity.this, null, getString(R.string.message_waiting));
        }

        @Override
        public void onResponse(Object response) {
            progressDialog.dismiss();
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            progressDialog.dismiss();
        }
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
                UIHelper.showSecurityPhone(AccountActivity.this);
                break;
            case R.id.layoutAccountEmail:
                UIHelper.showSecurityEmail(AccountActivity.this);
                break;
            case R.id.layoutAccountModifyLoginPassword:
                UIHelper.showChangePassword(AccountActivity.this);
                break;
            case R.id.layoutAccountPasswordProtection:
                UIHelper.showChangePaymentPassword(AccountActivity.this);
                break;
            case R.id.layoutPasswordGestures:

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
