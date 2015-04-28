package com.weedai.ptp.ui.activity;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFragmentSwitcher();
        initRadioGroup();
    }

    @Override
    protected void onResume() {
        super.onResume();

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


}
