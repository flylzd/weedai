package com.weedai.ptp.ui.activity;


import android.app.AlertDialog;
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

import java.util.HashMap;
import java.util.Map;

public class LuckyDrawActivity extends BaseActivity {

    private final static String TAG = "LuckyDrawActivity";

    private ImageView imgAward;
    private TextView tvAward;

    private int prizeid = 0;  //奖励
    private int number = 0;  //次数

    private ProgressDialog progressDialog;
    private Map<Integer, String> awardMap = new HashMap<Integer, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lucky_draw_new);

//        initView();
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
                    } else  {
                        Toast.makeText(LuckyDrawActivity.this, "没有抽奖机会，赶紧投标", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    UIHelper.showLogin(LuckyDrawActivity.this);
                }
            }
        });

        String award = String.format(getString(R.string.award_number_hint), number);
        tvAward.setText(Html.fromHtml(award));

        awardMap.put(1, "ipadmini");
        awardMap.put(2, "魅族mx4");
        awardMap.put(3, "魅族魅蓝");
        awardMap.put(4, "神奇干红");
        awardMap.put(5, "智能开关");
        awardMap.put(6, "50 微币");
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
//                tvAward.setText(Html.fromHtml(award));
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
                AwardData data = result.data;
//                number = data.cishu;
                number--;
                String award = String.format(getString(R.string.award_number_hint), number);
                tvAward.setText(Html.fromHtml(award));

                int prizeid = data.prizeid;
                String message = awardMap.get(prizeid);

                AlertDialog.Builder builder = new AlertDialog.Builder(LuckyDrawActivity.this);
                builder.setTitle("提示");
                builder.setMessage("恭喜你, 你抽到 " + message);
                builder.setPositiveButton("确定", null);
                builder.show();
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
            }
        });
    }
}