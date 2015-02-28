package com.weedai.p2p.ui.activity;


import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.weedai.p2p.R;

public class BaseActivity extends ActionBarActivity {

    protected LayoutInflater mInflater;
    protected ActionBar mActionBar;
    private TextView mTvActionBarTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mInflater = getLayoutInflater();
        mActionBar = getSupportActionBar();

        initActionBar();
    }

    protected void initActionBar() {

        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowCustomEnabled(true);
//        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View view = mInflater.inflate(R.layout.actionbar_custom, null);

        mTvActionBarTitle = (TextView) view.findViewById(R.id.tvActionbarTitle);
        if (mTvActionBarTitle == null) {
            throw new IllegalArgumentException(
                    "can not find R.id.tv_actionbar_title in customView");
        }
        int titleRes = getActionBarTitle();
        if (titleRes != 0) {
            mTvActionBarTitle.setText(titleRes);
        }

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT);
        mActionBar.setCustomView(view, params);
    }


    protected int getActionBarTitle() {
        return R.string.app_name;
    }

    public void setActionBarTitle(int resId) {
        if (resId != 0) {
            setActionBarTitle(getString(resId));
        }
    }

    public void setActionBarTitle(String title) {
        if (mTvActionBarTitle != null) {
            mTvActionBarTitle.setText(title);
        }
        mActionBar.setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
//            finish();
            backClick();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected  void backClick() {
//        finish();
    }

}
