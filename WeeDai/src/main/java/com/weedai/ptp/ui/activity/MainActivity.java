package com.weedai.ptp.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.lemon.aklib.widget.fragmentswitcher.FragmentStateArrayPagerAdapter;
import com.lemon.aklib.widget.fragmentswitcher.FragmentSwitcher;
import com.weedai.ptp.R;
import com.weedai.ptp.constant.Config;
import com.weedai.ptp.ui.fragment.BbsFragment;
import com.weedai.ptp.ui.fragment.HomeFragment;
import com.weedai.ptp.ui.fragment.MoreFragment;
import com.weedai.ptp.ui.fragment.MyFragment;
import com.weedai.ptp.utils.UIHelper;


public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private FragmentSwitcher fragmentSwitcher;
    private FragmentStateArrayPagerAdapter fragmentAdapter;
    private RadioGroup radioGroup;

    public static int lastSelect = 0;
    public static boolean isLoginFromMain;

    private static final String SHAREDPREFERENCES_NAME = "first_pref";
    private boolean isFirstIn = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initGuided();
        if (!isFirstIn){
            initFragmentSwitcher();
            initRadioGroup();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        System.out.println("MainActivity onResume");

        switch (lastSelect) {
            case 0:
                fragmentSwitcher.setCurrentItem(0);
                setActionBarTitle(R.string.main_tab_home);
                lastSelect = 0;
                ((RadioButton) radioGroup.getChildAt(lastSelect)).setChecked(true);
                break;
            case 1:
                fragmentSwitcher.setCurrentItem(1);
                setActionBarTitle(R.string.main_tab_my);
                lastSelect = 1;
                ((RadioButton) radioGroup.getChildAt(lastSelect)).setChecked(true);
                break;
            case 2:
                fragmentSwitcher.setCurrentItem(2);
                setActionBarTitle(R.string.main_tab_bbs);
                lastSelect = 2;
                ((RadioButton) radioGroup.getChildAt(lastSelect)).setChecked(true);
                break;
            case 3:
                fragmentSwitcher.setCurrentItem(3);
                setActionBarTitle(R.string.main_tab_more);
                lastSelect = 3;
                ((RadioButton) radioGroup.getChildAt(lastSelect)).setChecked(true);
                break;
        }
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.main_tab_home;
    }

    private void initGuided() {

        // 读取SharedPreferences中需要的数据
        // 使用SharedPreferences来记录程序的使用次数
        SharedPreferences preferences = getSharedPreferences(
                SHAREDPREFERENCES_NAME, MODE_PRIVATE);

        // 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
        isFirstIn = preferences.getBoolean("isFirstIn", true);

        // 判断程序与第几次运行，如果是第一次运行则跳转到引导界面，否则跳转到主界面
        if (isFirstIn) {
            Intent intent = new Intent(MainActivity.this, GuidedActivity.class);
            this.startActivity(intent);
            finish();
        }
    }

    private void initFragmentSwitcher() {
        fragmentSwitcher = (FragmentSwitcher) findViewById(R.id.fragment_container);
        fragmentAdapter = new FragmentStateArrayPagerAdapter(getSupportFragmentManager());
        fragmentSwitcher.setAdapter(fragmentAdapter);

        HomeFragment homeFragment = HomeFragment.newInstance();
        MyFragment myFragment = MyFragment.newInstance();
        BbsFragment bbsFragment = BbsFragment.newInstance();
        MoreFragment moreFragment = MoreFragment.newInstance();

        fragmentAdapter.add(homeFragment);
        fragmentAdapter.add(myFragment);
        fragmentAdapter.add(bbsFragment);
        fragmentAdapter.add(moreFragment);
    }

    private void initRadioGroup() {

        radioGroup = (RadioGroup) findViewById(R.id.rgMainTab);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.rbMainTabHome:
                        fragmentSwitcher.setCurrentItem(0);
                        setActionBarTitle(R.string.main_tab_home);
                        lastSelect = 0;
                        break;
                    case R.id.rbMainTabMy:

                        if (Config.isLogin) {
                            fragmentSwitcher.setCurrentItem(1);
                            setActionBarTitle(R.string.main_tab_my);
                            lastSelect = 1;
                        } else {
                            MainActivity.isLoginFromMain = true;
                            UIHelper.showLogin(MainActivity.this);
                        }
                        break;
                    case R.id.rbMainTabBbs:
                        fragmentSwitcher.setCurrentItem(2);
                        setActionBarTitle(R.string.main_tab_bbs);
                        lastSelect = 2;
                        break;
                    case R.id.rbMainTabMore:
                        fragmentSwitcher.setCurrentItem(3);
                        setActionBarTitle(R.string.main_tab_more);
                        lastSelect = 3;
                        break;
                }
            }
        });
    }


    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;
    private static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次后退键退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            Log.e(TAG, "exit application");
//            this.finish();
            System.exit(0);
        }
    }


}
