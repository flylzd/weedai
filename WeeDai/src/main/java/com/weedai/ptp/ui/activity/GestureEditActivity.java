package com.weedai.ptp.ui.activity;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.weedai.ptp.R;
import com.weedai.ptp.constant.Config;
import com.weedai.ptp.widget.lockview.GestureContentView;
import com.weedai.ptp.widget.lockview.GestureDrawline;
import com.weedai.ptp.widget.lockview.LockIndicator;

public class GestureEditActivity extends BaseActivity implements View.OnClickListener {

//    private Lock9View lock9View;
    /**
     * 手机号码
     */
    public static final String PARAM_PHONE_NUMBER = "PARAM_PHONE_NUMBER";
    /**
     * 意图
     */
    public static final String PARAM_INTENT_CODE = "PARAM_INTENT_CODE";
    /**
     * 首次提示绘制手势密码，可以选择跳过
     */
    public static final String PARAM_IS_FIRST_ADVICE = "PARAM_IS_FIRST_ADVICE";
    private TextView mTextTitle;
    private TextView mTextCancel;
    private LockIndicator mLockIndicator;
    private TextView mTextTip;
    private FrameLayout mGestureContainer;
    private GestureContentView mGestureContentView;
    private TextView mTextReset;
    private String mParamSetUpcode = null;
    private String mParamPhoneNumber;
    private boolean mIsFirstInput = true;
    private String mFirstPassword = null;
    private String mConfirmPassword = null;
    private int mParamIntentCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_edit);

        initView();
//        loadData();
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_password_gestures_lock;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_cancel:
                this.finish();
                break;
            case R.id.text_reset:
                mIsFirstInput = true;
                updateCodeList("");
                mTextTip.setText(getString(R.string.set_gesture_pattern));
                break;
            default:
                break;
        }
    }

    private void initView() {

        mTextTitle = (TextView) findViewById(R.id.text_title);
        mTextCancel = (TextView) findViewById(R.id.text_cancel);
        mTextReset = (TextView) findViewById(R.id.text_reset);
        mTextReset.setClickable(false);
        mTextReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mIsFirstInput) {
                    updateCodeList("");
                    mTextTip.setText(getString(R.string.set_gesture_pattern));
                    mGestureContentView.clearDrawlineState(0L);
                    mIsFirstInput = true;
                }
            }
        });
        mLockIndicator = (LockIndicator) findViewById(R.id.lock_indicator);
        mTextTip = (TextView) findViewById(R.id.text_tip);
        mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container);
        // 初始化一个显示各个点的viewGroup
        mGestureContentView = new GestureContentView(this, false, "", new GestureDrawline.GestureCallBack() {
            @Override
            public void onGestureCodeInput(String inputCode) {
                if (!isInputPassValidate(inputCode)) {
                    mTextTip.setText(Html.fromHtml("<font color='#c70c1e'>最少链接4个点, 请重新输入</font>"));
                    mGestureContentView.clearDrawlineState(0L);
                    return;
                }
                if (mIsFirstInput) {
                    mFirstPassword = inputCode;
                    updateCodeList(inputCode);
                    mGestureContentView.clearDrawlineState(0L);
                    mTextReset.setClickable(true);
                    mTextReset.setText(getString(R.string.reset_gesture_code));
                } else {
                    if (inputCode.equals(mFirstPassword)) {
                        Toast.makeText(GestureEditActivity.this, "手势设置成功", Toast.LENGTH_SHORT).show();
                        mGestureContentView.clearDrawlineState(0L);
                        GestureEditActivity.this.finish();

                        SharedPreferences preferences = getSharedPreferences(Config.PREFERENCE_NAME_LOCK, MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(Config.REMEBER_LOCK_VALUE, inputCode);
                        editor.putBoolean(Config.REMEBER_LOCK_LOGIN, true);
                        editor.commit();

                    } else {
                        mTextTip.setText(Html.fromHtml("<font color='#c70c1e'>与上一次绘制不一致，请重新绘制</font>"));
                        // 左右移动动画
                        Animation shakeAnimation = AnimationUtils.loadAnimation(GestureEditActivity.this, R.anim.shake);
                        mTextTip.startAnimation(shakeAnimation);
                        // 保持绘制的线，1.5秒后清除
                        mGestureContentView.clearDrawlineState(1300L);
                    }
                }
                mIsFirstInput = false;
            }

            @Override
            public void checkedSuccess() {

            }

            @Override
            public void checkedFail() {

            }
        });
        // 设置手势解锁显示到哪个布局里面
        mGestureContentView.setParentView(mGestureContainer);
        updateCodeList("");
    }

    private void updateCodeList(String inputCode) {
        // 更新选择的图案
        mLockIndicator.setPath(inputCode);
    }

    private boolean isInputPassValidate(String inputPassword) {
        if (TextUtils.isEmpty(inputPassword) || inputPassword.length() < 4) {
            return false;
        }
        return true;
    }

}
