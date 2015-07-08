package com.weedai.ptp.ui.activity;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.weedai.ptp.R;
import com.weedai.ptp.constant.Config;
import com.weedai.ptp.utils.UIHelper;

public class GesturePasswordActivity extends BaseActivity implements View.OnClickListener {


    private View layoutPasswordGestures;
    private View layoutPasswordGesturesClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_gestures);

        initView();
//        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences(Config.PREFERENCE_NAME_LOCK, MODE_PRIVATE);
        boolean isLock = preferences.getBoolean(Config.REMEBER_LOCK_LOGIN, false);
        if (isLock) {
            layoutPasswordGestures.setVisibility(View.GONE);
            layoutPasswordGesturesClear.setVisibility(View.VISIBLE);
        } else {
            layoutPasswordGestures.setVisibility(View.VISIBLE);
            layoutPasswordGesturesClear.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.layoutPasswordGestures:
                UIHelper.showLock9View(GesturePasswordActivity.this);
                break;
            case R.id.layoutPasswordGesturesClear:
//                UIHelper.showLock9View(PasswordGesturesActivity.this);
                SharedPreferences preferences = getSharedPreferences(Config.PREFERENCE_NAME_LOCK, MODE_PRIVATE);
                preferences.edit().clear();
                preferences.edit().commit();
                layoutPasswordGestures.setVisibility(View.VISIBLE);
                layoutPasswordGesturesClear.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_password_gestures;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    private void initView() {
        layoutPasswordGestures = findViewById(R.id.layoutPasswordGestures);
        layoutPasswordGesturesClear = findViewById(R.id.layoutPasswordGesturesClear);
        layoutPasswordGestures.setOnClickListener(this);
        layoutPasswordGesturesClear.setOnClickListener(this);
    }

}
