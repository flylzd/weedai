package com.weedai.ptp.ui.activity;


import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.weedai.ptp.R;

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

        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
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

        View back = view.findViewById(R.id.imgBack);
        if (back == null) {
            throw new IllegalArgumentException(
                    "can not find R.id.imgBack in customView");
        }
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (hasBackButton()) {
            back.setVisibility(View.VISIBLE);
        } else {
            back.setVisibility(View.GONE);
        }

        View qq = view.findViewById(R.id.imgQQ);
        if (hasQQ()){
            qq.setVisibility(View.VISIBLE);
        } else {
            qq.setVisibility(View.GONE);
        }
        qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qqClick();
            }
        });

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

    protected boolean hasBackButton() {
        return false;
    }

    protected boolean hasQQ() {
        return false;
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

    protected void backClick() {
//        finish();
    }

    protected void qqClick() {

    }

}
