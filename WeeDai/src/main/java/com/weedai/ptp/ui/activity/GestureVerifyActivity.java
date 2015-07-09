package com.weedai.ptp.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Config;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.model.BaseModel;
import com.weedai.ptp.model.Valicode;
import com.weedai.ptp.ui.fragment.HomeFragment;
import com.weedai.ptp.utils.UIHelper;
import com.weedai.ptp.volley.ResponseListener;
import com.weedai.ptp.widget.lockview.GestureContentView;
import com.weedai.ptp.widget.lockview.GestureDrawline;

/**
 * 手势绘制/校验界面
 */
public class GestureVerifyActivity extends Activity implements View.OnClickListener {

    private final static String TAG = "GestureVerifyActivity";

    /**
     * 手机号码
     */
    public static final String PARAM_PHONE_NUMBER = "PARAM_PHONE_NUMBER";
    /**
     * 意图
     */
    public static final String PARAM_INTENT_CODE = "PARAM_INTENT_CODE";

    private ImageView mImgUserLogo;
    private TextView mTextPhoneNumber;
    private TextView mTextTip;
    private FrameLayout mGestureContainer;
    private GestureContentView mGestureContentView;
    private TextView mTextForget;
    private TextView mTextOther;
    private String mParamPhoneNumber;
    private long mExitTime = 0;
    private int mParamIntentCode;

    private int countGesture = 0;

    private String valicode;

    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gesture_verify);
        initView();
//        ObtainExtraData();
//        setUpViews();
//        setUpListeners();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_other_account:
                Config.GESTURE_VERIFY_ERROR = true;      //手势密码出错
                UIHelper.showMain(GestureVerifyActivity.this);
                this.finish();
                break;
            default:
                break;
        }
    }


    private void initView() {
//        mTopLayout = (RelativeLayout) findViewById(R.id.top_layout);
//        mTextTitle = (TextView) findViewById(R.id.text_title);
//        mTextCancel = (TextView) findViewById(R.id.text_cancel);
        mImgUserLogo = (ImageView) findViewById(R.id.user_logo);
        mTextPhoneNumber = (TextView) findViewById(R.id.text_phone_number);
        mTextTip = (TextView) findViewById(R.id.text_tip);
        mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container);
        mTextForget = (TextView) findViewById(R.id.text_forget_gesture);
        mTextOther = (TextView) findViewById(R.id.text_other_account);

//        mTextCancel.setOnClickListener(this);
        mTextForget.setOnClickListener(this);
        mTextOther.setOnClickListener(this);

        SharedPreferences preferences = getSharedPreferences(Config.PREFERENCE_NAME_LOCK, MODE_PRIVATE);
        String lockPassword = preferences.getString(Config.REMEBER_LOCK_VALUE, "");

        // 初始化一个显示各个点的viewGroup
        mGestureContentView = new GestureContentView(this, true, lockPassword,
                new GestureDrawline.GestureCallBack() {

                    @Override
                    public void onGestureCodeInput(String inputCode) {

                    }

                    @Override
                    public void checkedSuccess() {
                        mGestureContentView.clearDrawlineState(0L);
//                        Toast.makeText(GestureVerifyActivity.this, "密码正确", Toast.LENGTH_SHORT).show();
//                        GestureVerifyActivity.this.finish();
                        getImgcode();
                    }

                    @Override
                    public void checkedFail() {
                        countGesture++;
                        if (countGesture == 5) {
                            Config.GESTURE_VERIFY_ERROR = true;      //手势密码出错

                            SharedPreferences preferences = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString(Config.REMEBER_PASSWORD, "");  //清楚记录密码
                            editor.commit();

                            UIHelper.showMain(GestureVerifyActivity.this);
                            GestureVerifyActivity.this.finish();
                        }
                        mGestureContentView.clearDrawlineState(1300L);
                        mTextTip.setVisibility(View.VISIBLE);
                        mTextTip.setText(Html
                                .fromHtml("<font color='#c70c1e'>密码错误,还有" + (5 - countGesture) + "次机会</font>"));
                        // 左右移动动画
                        Animation shakeAnimation = AnimationUtils.loadAnimation(GestureVerifyActivity.this, R.anim.shake);
                        mTextTip.startAnimation(shakeAnimation);
                    }
                });
        // 设置手势解锁显示到哪个布局里面
        mGestureContentView.setParentView(mGestureContainer);
    }

    private void login() {

        SharedPreferences preferences = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String username = preferences.getString(Config.REMEBER_USERNAME, "");
        String password = preferences.getString(Config.REMEBER_PASSWORD, "");

        ApiClient.login(TAG, username, password, valicode, new ResponseListener() {
            @Override
            public void onStarted() {
                progressDialog = ProgressDialog.show(GestureVerifyActivity.this, null, getString(R.string.login_waiting));
            }

            @Override
            public void onResponse(Object response) {
                progressDialog.dismiss();

                BaseModel result = (BaseModel) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    String message = result.message;
                    if (message.equals("login_fail")) {
                        message = "登录失败,密码不正确";
//                        getImgcode();
//                        etValicode.getText().clear();
                    }
                    Toast.makeText(GestureVerifyActivity.this, message, Toast.LENGTH_SHORT).show();
                    return;
                }

//                if (HomeFragment.isLoginFromHome) {
//                    Config.isLogin = true;
//                    finish();
//                    return;
//                }
//
//                if (MainActivity.isLoginFromMain) {
////                UIHelper.showMain(LoginActivity.this);
//                    MainActivity.lastSelect = 1;
//                    Config.isLogin = true;
//                    finish();
//                    return;
//                }

                Config.isLogin = true;
                UIHelper.showMain(GestureVerifyActivity.this);
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
                progressDialog = ProgressDialog.show(GestureVerifyActivity.this, null, getString(R.string.login_waiting));
            }

            @Override
            public void onResponse(Object response) {

                Valicode result = (Valicode) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(GestureVerifyActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }
                valicode = result.data.code;
                System.out.println("valicode : " + valicode);

                login();
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
    }
}
