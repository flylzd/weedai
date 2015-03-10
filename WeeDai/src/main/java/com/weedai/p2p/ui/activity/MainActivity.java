package com.weedai.p2p.ui.activity;

import android.os.Bundle;
import android.widget.RadioGroup;

import com.lemon.aklib.widget.fragmentswitcher.FragmentStateArrayPagerAdapter;
import com.lemon.aklib.widget.fragmentswitcher.FragmentSwitcher;
import com.weedai.p2p.R;
import com.weedai.p2p.ui.fragment.BbsFragment;
import com.weedai.p2p.ui.fragment.HomeFragment;
import com.weedai.p2p.ui.fragment.MoreFragment;
import com.weedai.p2p.ui.fragment.MyFragment;


public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private FragmentSwitcher fragmentSwitcher;
    private FragmentStateArrayPagerAdapter fragmentAdapter;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFragmentSwitcher();
        initRadioGroup();
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
                        break;
                    case R.id.rbMainTabMy:
                        fragmentSwitcher.setCurrentItem(1);
                        setActionBarTitle(R.string.main_tab_my);
                        break;
                    case R.id.rbMainTabBbs:
                        fragmentSwitcher.setCurrentItem(2);
                        setActionBarTitle(R.string.main_tab_bbs);
                        break;
                    case R.id.rbMainTabMore:
                        fragmentSwitcher.setCurrentItem(3);
                        setActionBarTitle(R.string.main_tab_more);
                        break;
                }
            }
        });
    }

    protected int getActionBarTitle() {
        return R.string.main_tab_home;
    }

}
