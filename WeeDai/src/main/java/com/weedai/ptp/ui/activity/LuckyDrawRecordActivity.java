package com.weedai.ptp.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.lemon.aklib.adapter.BaseAdapterHelper;
import com.lemon.aklib.adapter.QuickAdapter;
import com.lemon.aklib.widget.EndOfListView;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.model.LuckyDraw;
import com.weedai.ptp.model.LuckyDrawList;
import com.weedai.ptp.model.Money;
import com.weedai.ptp.model.MoneyList;
import com.weedai.ptp.utils.DataUtil;
import com.weedai.ptp.volley.ResponseListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：lemon
 * 日期：2015-08-17
 */
public class LuckyDrawRecordActivity extends BaseActivity implements EndOfListView.OnEndOfListListener {

    private final static String TAG = "LuckyDrawRecordActivity";

    private TextView tvLuckyDrawTotal;

    private EndOfListView listView;
    private QuickAdapter<LuckyDrawList> adapter;
    private List<LuckyDrawList> dataList = new ArrayList<LuckyDrawList>();

    private final static int DEFAULT_PAGE = 0;
    private int page = DEFAULT_PAGE;

    private boolean isBottomLoadingComplete = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lucky_draw_record);

        initView();

    }


    @Override
    protected int getActionBarTitle() {
        return R.string.title_lucky_draw_record;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }


    @Override
    public void onEndOfList(Object lastItem) {
        if (isBottomLoadingComplete) {
            showIndeterminateProgress(false);
            return;
        }
        page++;
        getLuckyDrawRecord();
    }

    private void showIndeterminateProgress(boolean visibility) {
        adapter.showIndeterminateProgress(visibility);
    }

    private void initView() {

        tvLuckyDrawTotal = (TextView) findViewById(R.id.tvLuckyDrawTotal);

        adapter = new QuickAdapter<LuckyDrawList>(LuckyDrawRecordActivity.this, R.layout.listitem_lucky_draw) {
            @Override
            protected void convert(BaseAdapterHelper helper, LuckyDrawList item) {

                String fangStr;
                Integer fang = item.if_fang;
                if (fang != null && fang == 1) {
                    fangStr = "已发放";
                } else {
                    fangStr = "没发放";
                }
                helper.setText(R.id.tvLUckyDrawState, fangStr);
                helper.setText(R.id.tvLUckyDrawPrize, DataUtil.urlDecode(item.prize));

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String time = sdf.format(Long.parseLong(item.addtime + "000"));
                helper.setText(R.id.tvLUckyDrawTime, time);
            }
        };

        listView = (EndOfListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnEndOfListListener(this);
    }

    private void getLuckyDrawRecord() {
        ApiClient.getLuckyDrawRecord(TAG, page, new RefreshResponseListener() {
            @Override
            public void onResponse(Object response) {
                super.onResponse(response);

                LuckyDraw result = (LuckyDraw) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(LuckyDrawRecordActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }
                dataList.addAll(result.data.list);
                adapter.replaceAll(dataList);

                String luckDrawTotal = String.format(getResources().getString(R.string.user_my_lucky_draw_record), result.data.luprice_num);
                tvLuckyDrawTotal.setText(luckDrawTotal);

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
