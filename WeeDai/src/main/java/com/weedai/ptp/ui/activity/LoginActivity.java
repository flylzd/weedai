package com.weedai.ptp.ui.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Config;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.model.BaseModel;
import com.weedai.ptp.model.Invest;
import com.weedai.ptp.model.Valicode;
import com.weedai.ptp.ui.fragment.HomeFragment;
import com.weedai.ptp.utils.UIHelper;
import com.weedai.ptp.view.SimpleValidateCodeView;
import com.weedai.ptp.view.ValidateCodeView;
import com.weedai.ptp.volley.ResponseListener;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    private CheckBox cbRememberUser;
//    private ImageView imgSelect;
//    private Spinner spinner;
//    private QuickAdapter<String> adapter;
//    private List<String> dataList = new ArrayList<String>();
//    private Set<String> hashSet = new HashSet<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        initView();

    }

    @Override
    public void onBackPressed() {
        HomeFragment.isLoginFromHome = false;
        MainActivity.isLoginFromMain = false;
        finish();
    }

    private void initView() {

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etValicode = (EditText) findViewById(R.id.etValicode);
        cbRememberUser = (CheckBox) findViewById(R.id.cbRememberUser);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        tvLoginRegister = (TextView) findViewById(R.id.tvLoginRegister);
        tvLoginForgetPassword = (TextView) findViewById(R.id.tvLoginForgetPassword);
//        imgSelect = (ImageView) findViewById(R.id.imgSelect);

        btnLogin.setOnClickListener(this);
        tvLoginRegister.setOnClickListener(this);
        tvLoginForgetPassword.setOnClickListener(this);
//        imgSelect.setOnClickListener(this);

        viewValicode = (SimpleValidateCodeView) findViewById(R.id.viewValicode);
//        viewValicode.getValidataAndSetImage(new String[]{"4","7","0","2"});
        viewValicode.setOnClickListener(this);

        getImgcode();  //获取验证码

        showRemeberUser();

//
//        adapter = new QuickAdapter<String>(LoginActivity.this, R.layout.listitem_user_remeber) {
//            @Override
//            protected void convert(final BaseAdapterHelper helper, final String item) {
//
//                helper.setText(R.id.tvUsername, item);
//                helper.setOnClickListener(R.id.imgDetele, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        if (adapter.getItem(helper.getPosition()).equals(etUsername.getText().toString())) {
//                            etUsername.getText().clear();
//                        }
//
//                        adapter.remove(item);
//                        dataList.remove(item);
//
//                        hashSet.clear();
//                        for (String item : dataList) {
//                            hashSet.add(item);
//                        }
//                        SharedPreferences preferences = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
//                        SharedPreferences.Editor editor = preferences.edit();
//                        editor.putStringSet(REMEBER_USER, hashSet);
//                        editor.commit();
//                    }
//                });
//
//                helper.getView().setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        System.out.println("setOnClickListener");
//                        etUsername.setText(adapter.getItem(helper.getPosition()));
//                        spinner.setSelection(helper.getPosition());
////                        spinner.
//                    }
//                });
//            }
//        };
//
//        spinner = (Spinner) findViewById(R.id.spinner);
//        spinner.setAdapter(adapter);
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
////                etUsername.setText(adapter.getItem(position));
//                System.out.println("setOnItemSelectedListener");
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        SharedPreferences preferences = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
//        hashSet = preferences.getStringSet(REMEBER_USER, new HashSet<String>());
//        for (String item : hashSet) {
//            System.out.println("init remeber user " + item);
//            dataList.add(item);
//        }
//        adapter.addAll(dataList);

    }


    private void login() {

        final String username = etUsername.getText().toString();
        final String password = etPassword.getText().toString();
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
            if (!valicode.equalsIgnoreCase(code)) {
                Toast.makeText(LoginActivity.this, getString(R.string.login_valicode_not_match), Toast.LENGTH_SHORT).show();
                getImgcode();
                etValicode.getText().clear();
                return;
            }
        }

//        username = URLEncoder.encode(username);
        ApiClient.login(TAG, username, password, valicode, new ResponseListener() {
            @Override
            public void onStarted() {
                progressDialog = ProgressDialog.show(LoginActivity.this, null, getString(R.string.login_waiting));
            }

            @Override
            public void onResponse(Object response) {
                progressDialog.dismiss();

                BaseModel result = (BaseModel) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    String message = result.message;
                    if (message.equals("login_fail")) {
                        message = "登录失败,密码不正确";
                        getImgcode();
                        etValicode.getText().clear();
                    }
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (cbRememberUser.isChecked()) {  //记住用户
                    saveRemeberUser(username, password);
                } else {
                    saveRemeberUser("", "");
                }

                if (Config.GESTURE_VERIFY_ERROR_CLEAR) {
                    Config.GESTURE_VERIFY_CLEAR = true;
                    SharedPreferences preferences = getSharedPreferences(Config.PREFERENCE_NAME_LOCK, MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(Config.REMEBER_LOCK_VALUE, "");
                    editor.putBoolean(Config.REMEBER_LOCK_LOGIN, false);
                    editor.commit();
//                    finish();
//                    return;
                }

//                if (HomeFragment.isLoginFromHome) {
//                    Config.isLogin = true;
//                    finish();
//                    return;
//                }

                if (MainActivity.isLoginFromMain) {
//                UIHelper.showMain(LoginActivity.this);
                    MainActivity.lastSelect = 1;
//                    Config.isLogin = true;
//                    finish();
//                    return;
                }

                Config.isLogin = true;
                finish();
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


    private void showRemeberUser() {

//        SharedPreferences preferences = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
//        hashSet = preferences.getStringSet(REMEBER_USER, new HashSet<String>());
//        dataList.clear();
//        for (String item : hashSet) {
//            System.out.println("showRemeberUser remeber user " + item);
//            dataList.add(item);
//        }
////        adapter.replaceAll(dataList);
//        spinner.performClick();

        SharedPreferences preferences = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String username = preferences.getString(Config.REMEBER_USERNAME, "");
        String password = preferences.getString(Config.REMEBER_PASSWORD, "");
        etUsername.setText(username);
        etPassword.setText(password);

    }

    private void saveRemeberUser(String username, String password) {

//        dataList.add(0, username);
//        hashSet.clear();
//        for (String item : dataList) {
//            System.out.println("saveRemeberUser remeber user " + item);
//            hashSet.add(item);
//        }
//        SharedPreferences preferences = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putStringSet(REMEBER_USER, hashSet);
//        editor.commit();

        SharedPreferences preferences = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Config.REMEBER_USERNAME, username);
        editor.putString(Config.REMEBER_PASSWORD, password);
        editor.commit();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnLogin:
//                UIHelper.showMain(LoginActivity.this);
                login();
                break;
            case R.id.imgSelect:
//                showRemeberUser();
                break;
            case R.id.tvLoginRegister:
                UIHelper.showRegister(LoginActivity.this);
                finish();
                break;
            case R.id.tvLoginForgetPassword:
                String url = "https://www.weedai.com/index.php?user&q=action/getpwd"; // web address
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
                break;
            case R.id.viewValicode:
//                viewValicode.getValidataAndSetImage(new String[]{"2", "7", "1", "9"});
                getImgcode();
                break;
        }
    }
}
