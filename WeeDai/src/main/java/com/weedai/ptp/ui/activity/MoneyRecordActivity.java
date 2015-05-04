package com.weedai.ptp.ui.activity;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ListView;
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
import com.weedai.ptp.model.Money;
import com.weedai.ptp.model.MoneyList;
import com.weedai.ptp.utils.DataUtil;
import com.weedai.ptp.volley.ResponseListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MoneyRecordActivity extends BaseActivity implements EndOfListView.OnEndOfListListener {

    private final static String TAG = "MoneyRecordActivity";

    private EndOfListView listView;
    private QuickAdapter<MoneyList> adapter;
    private List<MoneyList> dataList = new ArrayList<MoneyList>();

    private final static int DEFAULT_PAGE = 1;
    private int page = DEFAULT_PAGE;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_record);

        initView();
//        loadData();
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_money_record;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    public void onEndOfList(Object lastItem) {
        showIndeterminateProgress(true);
        getMoneyRecord();
        page++;
    }

    private void showIndeterminateProgress(boolean visibility) {
        adapter.showIndeterminateProgress(visibility);
    }

    private void initView() {

        adapter = new QuickAdapter<MoneyList>(MoneyRecordActivity.this, R.layout.listitem_money_record) {
            @Override
            protected void convert(BaseAdapterHelper helper, MoneyList item) {

                helper.setText(R.id.tvMoneyType, DataUtil.urlDecode(item.leixing));
                helper.setText(R.id.tvMoneyMoney, item.money);
                helper.setText(R.id.tvMoneyTotal, item.total);
                helper.setText(R.id.tvMoneyUseMoney, item.use_money);
                String username = item.to_username;
                if (TextUtils.isEmpty(username)) {
                    username = "admin";
                }
                helper.setText(R.id.tvMoneyUsername, DataUtil.urlDecode(username));

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String time = sdf.format(Long.parseLong(item.addtime + "000"));
                helper.setText(R.id.tvMoneyTime, time);
            }
        };

        listView = (EndOfListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnEndOfListListener(this);
    }

    private void loadData() {
        getMoneyRecord();
    }

    private void getMoneyRecord() {
        ApiClient.getMoneyRecord(TAG, page, new RefreshResponseListener() {
            @Override
            public void onResponse(Object response) {
                super.onResponse(response);

                Money result = (Money) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(MoneyRecordActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }
                dataList.addAll(result.data.list);
                adapter.replaceAll(dataList);
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
