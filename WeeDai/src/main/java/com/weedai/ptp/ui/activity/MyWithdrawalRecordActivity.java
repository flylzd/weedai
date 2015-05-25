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


/**
 * 提现记录
 */
public class MyWithdrawalRecordActivity extends BaseActivity implements EndOfListView.OnEndOfListListener {

    private final static String TAG = "MoneyRecordActivity";

    private EndOfListView listView;
    private QuickAdapter<WithdrawalRecordList> adapter;
    private List<WithdrawalRecordList> dataList = new ArrayList<WithdrawalRecordList>();

    private final static int DEFAULT_PAGE = 0;
    private int page = DEFAULT_PAGE;

    private boolean isBottomLoadingComplete = false;

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

//        if (isFirstLoadingomplete){
//            if (dataList.size() < 10) {
//                showIndeterminateProgress(false);
//                return;
//            }
//            showIndeterminateProgress(true);
//            page++;
//            getWithdrawalRecord();
//        } else {
//            getWithdrawalRecord();
//        }

        if (isBottomLoadingComplete) {
            showIndeterminateProgress(false);
            return;
        }
        page++;
        getWithdrawalRecord();
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

                String account = item.account;
                int length = account.length();
                String bankName = Constant.bankMap.get(item.bank);
                bankName = bankName + "(尾号" + account.substring(length - 4, length) + ")";
                helper.setText(R.id.tvRecordCardNumber, bankName);
                helper.setText(R.id.tvRecordTotal, item.total);
                helper.setText(R.id.tvRecordCredited, item.credited);

                String state = "";
                int status = item.status;
                switch (status) {
                    case 0:
                        state = "审核中";
                        break;
                    case 1:
                        state = "提现成功";
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

//                isFirstLoadingomplete = true;

                int currentPage = result.data.page;
                int totalPage = result.data.total_page;
                if (currentPage == totalPage || totalPage == 0) {
                    isBottomLoadingComplete = true;
                }
            }
        });
    }


    private abstract class RefreshResponseListener implements ResponseListener {

        @Override
        public void onStarted() {
            showIndeterminateProgress(true);
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