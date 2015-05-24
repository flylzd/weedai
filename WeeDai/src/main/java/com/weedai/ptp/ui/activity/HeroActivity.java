package com.weedai.ptp.ui.activity;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.model.Hero;
import com.weedai.ptp.model.HeroData;
import com.weedai.ptp.model.RotationImage;
import com.weedai.ptp.utils.DataUtil;
import com.weedai.ptp.utils.ListViewUtil;
import com.weedai.ptp.volley.ResponseListener;

import java.util.List;

public class HeroActivity extends BaseActivity {

    private final static String TAG = "HeroActivity";

    private ListView listView;
    private QuickAdapter<HeroData> adapter;
    private List<HeroData> dataList;

    private int dact = 0; //1. 上星期
    // 0. 本星期

    private TextView tvHeroTitle;
    private TextView tvLastWeek;
    private TextView tvThisWeek;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero);

        initView();
        loadData();
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_hero;
    }

    private void initView() {

        adapter = new QuickAdapter<HeroData>(HeroActivity.this, R.layout.listitem_hero) {
            @Override
            protected void convert(BaseAdapterHelper helper, HeroData item) {

                ImageView imageView = helper.getView(R.id.imgHeroRanking);
                TextView tvHeroRanking = helper.getView(R.id.tvHeroRanking);
                if (helper.getPosition() == 0) {
                    tvHeroRanking.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageResource(R.drawable.icon_hero_1);

                    helper.setText(R.id.tvHeroUsername, DataUtil.urlDecode(item.user));
                    helper.setText(R.id.tvHeroIntegral, DataUtil.urlDecode(item.fens));

                    helper.setTextColor(R.id.tvHeroUsername, getResources().getColor(R.color.main_text_orange));
                    helper.setTextColor(R.id.tvHeroIntegral, getResources().getColor(R.color.main_text_orange));

                } else if (helper.getPosition() == 1) {
                    tvHeroRanking.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageResource(R.drawable.icon_hero_2);

                    helper.setText(R.id.tvHeroUsername, DataUtil.urlDecode(item.user));
                    helper.setText(R.id.tvHeroIntegral, DataUtil.urlDecode(item.fens));

                    helper.setTextColor(R.id.tvHeroUsername, getResources().getColor(R.color.main_text_orange));
                    helper.setTextColor(R.id.tvHeroIntegral, getResources().getColor(R.color.main_text_orange));

                } else if (helper.getPosition() == 2) {
                    tvHeroRanking.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageResource(R.drawable.icon_hero_3);

                    helper.setText(R.id.tvHeroUsername, DataUtil.urlDecode(item.user));
                    helper.setText(R.id.tvHeroIntegral, DataUtil.urlDecode(item.fens));

                    helper.setTextColor(R.id.tvHeroUsername, getResources().getColor(R.color.main_text_orange));
                    helper.setTextColor(R.id.tvHeroIntegral, getResources().getColor(R.color.main_text_orange));

                } else {
                    imageView.setVisibility(View.GONE);
                    tvHeroRanking.setVisibility(View.VISIBLE);

                    tvHeroRanking.setText(String.valueOf(helper.getPosition() + 1));
                    helper.setText(R.id.tvHeroUsername, DataUtil.urlDecode(item.user));
                    helper.setText(R.id.tvHeroIntegral, DataUtil.urlDecode(item.fens));

                    helper.setTextColor(R.id.tvHeroUsername, getResources().getColor(R.color.main_text_black));
                    helper.setTextColor(R.id.tvHeroIntegral, getResources().getColor(R.color.main_text_black));
                }
            }
        };

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        ListViewUtil.setListViewHeightBasedOnChildren(listView);

        tvHeroTitle = (TextView) findViewById(R.id.tvHeroTitle);
        tvLastWeek = (TextView) findViewById(R.id.tvLastWeek);
        tvThisWeek = (TextView) findViewById(R.id.tvThisWeek);
        tvLastWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dact = 1;
                getHeroList(dact);

                tvHeroTitle.setText("上周投资英雄榜");
            }
        });
        tvThisWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dact = 0;
                getHeroList(dact);
                tvHeroTitle.setText("本周投资英雄榜");
            }
        });



    }

    private void loadData() {

        getHeroList(dact);
    }

    private void getHeroList(final int dact) {

        ApiClient.getHeroList(TAG, dact, new RefreshResponseListener() {
            @Override
            public void onResponse(Object response) {
                super.onResponse(response);

                Hero result = (Hero) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(HeroActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }
                dataList = result.data;
                adapter.replaceAll(dataList);
                ListViewUtil.setListViewHeightBasedOnChildren(listView);

                if (dact == 0 ){
                    tvLastWeek.setVisibility(View.VISIBLE);
                    tvThisWeek.setVisibility(View.GONE);
                } else {
                    tvLastWeek.setVisibility(View.GONE);
                    tvThisWeek.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private abstract class RefreshResponseListener implements ResponseListener {

        @Override
        public void onStarted() {
            progressDialog = ProgressDialog.show(HeroActivity.this, null, getString(R.string.message_waiting));
        }

        @Override
        public void onResponse(Object response) {
            progressDialog.dismiss();
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            progressDialog.dismiss();
        }
    }
}
