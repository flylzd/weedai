package com.weedai.ptp.ui.activity;


import android.app.Activity;
import android.os.Bundle;

import com.weedai.ptp.R;

public class Lock9ViewActivity extends BaseActivity {

//    private Lock9View lock9View;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock9view);

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

    private void initView() {

//        lock9View = (Lock9View) findViewById(R.id.lock9View);
//        lock9View.setCallBack(new Lock9View.CallBack() {
//            @Override
//            public void onFinish(String s) {
//
//
//
//
//            }
//        });


    }

}
