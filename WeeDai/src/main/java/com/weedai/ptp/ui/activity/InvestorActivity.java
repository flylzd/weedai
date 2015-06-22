package com.weedai.ptp.ui.activity;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.lemon.aklib.widget.EndOfListView;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.model.Investor;
import com.weedai.ptp.model.InvestorData;
import com.weedai.ptp.utils.DataUtil;
import com.weedai.ptp.volley.ResponseListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class InvestorActivity extends BaseActivity {

    private final static String TAG = "InvestorActivity";

    private EndOfListView listView;
    private QuickAdapter<InvestorData> adapter;
    private List<InvestorData> dataList = new ArrayList<InvestorData>();

    private String bid;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investor);

        if (getIntent().hasExtra("bid")) {
            bid = getIntent().getStringExtra("bid");
        }

        initView();
        loadData();
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_investor;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }


    private void showIndeterminateProgress(boolean visibility) {
        adapter.showIndeterminateProgress(visibility);
    }

    private void initView() {

        adapter = new QuickAdapter<InvestorData>(InvestorActivity.this, R.layout.listitem_investor) {
            @Override
            protected void convert(BaseAdapterHelper helper, InvestorData item) {

                helper.setText(R.id.tvInvestorName, DataUtil.urlDecode(item.username));
                helper.setText(R.id.tvInvestorInvestmentAmount, DataUtil.urlDecode(item.money));
                helper.setText(R.id.tvInvestorForceAmount, DataUtil.urlDecode(item.tender_account));

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String addtime = sdf.format(Long.parseLong(item.addtime + "000"));
                helper.setText(R.id.tvInvestorDate, addtime);

                String state;
                if (item.money.equals(item.tender_account)) {
                    state = "全部通过";
                } else {
                    state = "通过";
                }
                helper.setText(R.id.tvInvestorState, state);
            }
        };

        listView = (EndOfListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }

    private void loadData() {

        getInvester(bid);
    }

    private void getInvester(String bid) {

        ApiClient.getInvester(TAG, bid, new RefreshResponseListener() {
            @Override
            public void onResponse(Object response) {
                super.onResponse(response);
            }
        });
    }

    private abstract class RefreshResponseListener implements ResponseListener {

        @Override
        public void onStarted() {
            progressDialog = ProgressDialog.show(InvestorActivity.this, null, getString(R.string.message_waiting));
        }

        @Override
        public void onResponse(Object response) {
            progressDialog.dismiss();

            Investor result = (Investor) response;
            if (result.code != Constant.CodeResult.SUCCESS) {
                Toast.makeText(InvestorActivity.this, result.message, Toast.LENGTH_SHORT).show();
                return;
            }

            dataList = result.data;
            adapter.replaceAll(dataList);

        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            progressDialog.dismiss();
        }
    }
}
