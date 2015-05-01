package com.weedai.ptp.ui.activity;


import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.lemon.aklib.adapter.BaseAdapterHelper;
import com.lemon.aklib.adapter.QuickAdapter;
import com.lemon.aklib.widget.EndOfListView;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.model.Micro;
import com.weedai.ptp.model.MicroList;
import com.weedai.ptp.volley.ResponseListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyMicroCurrencyHistoryActivity extends BaseActivity implements EndOfListView.OnEndOfListListener {

    private final static String TAG = "MyMicroCurrencyHistoryActivity";

    private EndOfListView listView;
    private QuickAdapter<MicroList> adapter;
    private List<MicroList> dataList = new ArrayList<MicroList>();

    private TextView tvAvailableMicroCurrency;
    private TextView tvTotalMicroCurrency;

    private final static int DEFAULT_PAGE = 1;
    private int page = DEFAULT_PAGE;

    private String wb = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_micro_currency_history);

        if (getIntent().hasExtra("wb")) {
            wb = getIntent().getStringExtra("wb");
        }

        initView();
    }


    @Override
    protected int getActionBarTitle() {
        return R.string.title_micro_history;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    public void onEndOfList(Object lastItem) {
        showIndeterminateProgress(true);
        getMicroHistory();
        page++;
    }

    private void showIndeterminateProgress(boolean visibility) {
        adapter.showIndeterminateProgress(visibility);
    }

    private void initView() {

        tvAvailableMicroCurrency = (TextView) findViewById(R.id.tvAvailableMicroCurrency);
        tvTotalMicroCurrency = (TextView) findViewById(R.id.tvTotalMicroCurrency);
        tvAvailableMicroCurrency.setText(wb);

        adapter = new QuickAdapter<MicroList>(MyMicroCurrencyHistoryActivity.this, R.layout.listitem_my_micro_currency) {
            @Override
            protected void convert(BaseAdapterHelper helper, MicroList item) {

                String typeName = "";
                int resId = 0;
                int type = item.type;
                switch (type) {
                    case 0:
                        typeName = "站岗";
                        resId = R.drawable.icon_micro_zhangang;
                        break;
                    case 1:
                        typeName = "签到";
                        resId = R.drawable.icon_micro_sign;
                        break;
                    case 2:
                        typeName = "分享";
                        resId = R.drawable.icon_micro_zhangang;
                        break;
                    case 3:
                        typeName = "投资";
                        resId = R.drawable.icon_micro_touzi;
                        break;
                    case 4:
                        typeName = "抽奖";
                        resId = R.drawable.icon_micro_chouzhang;
                        break;
                    case 7:
                        typeName = "罚息";
                        resId = R.drawable.icon_micro_zhangang;
                        break;
                    case 8:
                        typeName = "续投";
                        resId = R.drawable.icon_micro_xutou;
                        break;
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                String time = sdf.format(item.addTime);

                System.out.println("time " + time);
                System.out.println("today Time  " + sdf.format(new Date()));

                helper.setImageResource(R.id.imgType, resId);
                helper.setText(R.id.tvMicroType, typeName);
                helper.setText(R.id.tvMicroTime, time);
                helper.setText(R.id.tvMicroNum, item.fen);
            }
        };
        listView = (EndOfListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnEndOfListListener(this);
    }

    private void getMicroHistory() {
        ApiClient.getMicroHistory(TAG, page, new RefreshResponseListener() {
            @Override
            public void onResponse(Object response) {
                super.onResponse(response);

                Micro result = (Micro) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(MyMicroCurrencyHistoryActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }
                dataList.addAll(result.data.list);
                adapter.replaceAll(dataList);

                tvTotalMicroCurrency.setText(result.data.account_num);
            }
        });
    }


    private abstract class RefreshResponseListener implements ResponseListener {

        @Override
        public void onStarted() {
        }

        @Override
        public void onResponse(Object response) {
            showIndeterminateProgress(false);
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            showIndeterminateProgress(false);
        }
    }


}
