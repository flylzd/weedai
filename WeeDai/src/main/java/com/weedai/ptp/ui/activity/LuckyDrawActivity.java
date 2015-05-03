package com.weedai.ptp.ui.activity;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Config;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.model.Award;
import com.weedai.ptp.model.AwardData;
import com.weedai.ptp.utils.UIHelper;
import com.weedai.ptp.volley.ResponseListener;

public class LuckyDrawActivity extends BaseActivity {

    private final static String TAG = "LuckyDrawActivity";

    private ImageView imgAward;
    private TextView tvAward;

    private int prizeid = 0;  //奖励
    private int number = 0;  //次数

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lucky_draw);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_lucky_draw;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    private void initView() {

        imgAward = (ImageView) findViewById(R.id.imgAward);
        tvAward = (TextView) findViewById(R.id.tvAward);

        imgAward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Config.isLogin) {
                    if (number != 0) {
                        award();
                    }
                } else {
                    UIHelper.showLogin(LuckyDrawActivity.this);
                }
            }
        });

        String award = String.format(getString(R.string.award_number_hint), number);
        tvAward.setText(Html.fromHtml(award));
    }

    private void loadData() {

        if (Config.isLogin) {
            getAwardNumber();
        }
    }

    private void getAwardNumber() {

        ApiClient.getAwardNumber(TAG, new ResponseListener() {
            @Override
            public void onStarted() {
                progressDialog = ProgressDialog.show(LuckyDrawActivity.this, null, getString(R.string.message_waiting));
            }

            @Override
            public void onResponse(Object response) {
                progressDialog.dismiss();
                Award result = (Award) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(LuckyDrawActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }
                AwardData data = result.data;
                number = data.cishu;
                String award = String.format(getString(R.string.award_number_hint), number);
                tvAward.setText(Html.fromHtml(award));
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
            }
        });
    }

    private void award() {

        ApiClient.award(TAG, new ResponseListener() {
            @Override
            public void onStarted() {
                progressDialog = ProgressDialog.show(LuckyDrawActivity.this, null, getString(R.string.message_waiting));
            }

            @Override
            public void onResponse(Object response) {
                progressDialog.dismiss();
                Award result = (Award) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(LuckyDrawActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }
//                AwardData data = result.data;
//                number = data.cishu;
                number--;
                String award = String.format(getString(R.string.award_number_hint), number);
                tvAward.setText(Html.fromHtml(award));
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
            }
        });
    }
}