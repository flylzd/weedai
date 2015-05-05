package com.weedai.ptp.ui.activity;


import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.lemon.aklib.adapter.BaseAdapterHelper;
import com.lemon.aklib.adapter.QuickAdapter;
import com.lemon.aklib.widget.EndOfListView;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.model.Money;
import com.weedai.ptp.model.WithdrawalRecord;
import com.weedai.ptp.model.WithdrawalRecordList;
import com.weedai.ptp.volley.ResponseListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MyWithdrawalRecordActivity extends BaseActivity implements EndOfListView.OnEndOfListListener {

    private final static String TAG = "MoneyRecordActivity";

    private EndOfListView listView;
    private QuickAdapter<WithdrawalRecordList> adapter;
    private List<WithdrawalRecordList> dataList = new ArrayList<WithdrawalRecordList>();

    private final static int DEFAULT_PAGE = 1;
    private int page = DEFAULT_PAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawal_record);

        initView();
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_withdrawal_record;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    public void onEndOfList(Object lastItem) {
        showIndeterminateProgress(true);
        getWithdrawalRecord();
        page++;
    }

    private void showIndeterminateProgress(boolean visibility) {
        adapter.showIndeterminateProgress(visibility);
    }

    private void initView() {


        adapter = new QuickAdapter<WithdrawalRecordList>(MyWithdrawalRecordActivity.this, R.layout.listitem_withdrawal_record) {
            @Override
            protected void convert(BaseAdapterHelper helper, WithdrawalRecordList item) {


                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String time = sdf.format(Long.parseLong(item.addtime + "000"));
                helper.setText(R.id.tvRecordTime, time);

                helper.setText(R.id.tvRecordCardNumber, item.account);
                helper.setText(R.id.tvRecordTotal, item.total);
                helper.setText(R.id.tvRecordCredited, item.credited);

                String state = "";
                int status = item.status;
                switch (status) {
                    case 0:
                        state = "审核中";
                        break;
                    case 1:
                        state = "现成功";
                        break;
                    case 2:
                        state = "提现失败";
                        break;
                    case 3:
                        state = "用户取消";
                        break;
                }
                helper.setText(R.id.tvRecordStatus, state);

            }
        };

        listView = (EndOfListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnEndOfListListener(this);
    }


    public void getWithdrawalRecord() {
        ApiClient.getWithdrawalRecord(TAG, page, new RefreshResponseListener() {
            @Override
            public void onResponse(Object response) {
                super.onResponse(response);

                WithdrawalRecord result = (WithdrawalRecord) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(MyWithdrawalRecordActivity.this, result.message, Toast.LENGTH_SHORT).show();
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