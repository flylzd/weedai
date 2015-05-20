package com.weedai.ptp.ui.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.lemon.aklib.adapter.BaseAdapterHelper;
import com.lemon.aklib.adapter.QuickAdapter;
import com.lemon.aklib.widget.EndOfListView;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.model.StandInsideLetter;
import com.weedai.ptp.model.StandInsideLetterList;
import com.weedai.ptp.utils.DataUtil;
import com.weedai.ptp.volley.ResponseListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MyStandInsideLetterActivity extends BaseActivity implements EndOfListView.OnEndOfListListener {

    private final static String TAG = "MyStandInsideLetterActivity";

    private EndOfListView listView;
    private QuickAdapter<StandInsideLetterList> adapter;
    private List<StandInsideLetterList> dataList = new ArrayList<StandInsideLetterList>();

    private final static int DEFAULT_PAGE = 1;
    private int page = DEFAULT_PAGE;

    private boolean isFirstLoadingomplete = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_stand_inside_letter);

        initView();
        loadData();
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_my_stand_inside_letter;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    public void onEndOfList(Object lastItem) {

        showIndeterminateProgress(true);
        if (isFirstLoadingomplete) {
            int size = dataList.size();
            if (size > 6) {
                page++;
                getStandInsideLetter();
            }
        }
    }

    private void showIndeterminateProgress(boolean visibility) {
        adapter.showIndeterminateProgress(visibility);
    }

    private void initView() {

        adapter = new QuickAdapter<StandInsideLetterList>(MyStandInsideLetterActivity.this, R.layout.listitem_stand_inside_letter) {

            @Override
            protected void convert(BaseAdapterHelper helper, StandInsideLetterList item) {

                String addTime = item.addtime + "000";
                SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 hh:mm");
                String time = sdf.format(Long.parseLong(addTime));

                helper.setText(R.id.tvTitle, Html.fromHtml(DataUtil.urlDecode(item.name)));
                helper.setText(R.id.tvTime, time);

                int status = item.status;
                if (status == 0) {  //没读
                    helper.setImageResource(R.id.imgStandInside, R.drawable.icon_stand_inside_not_read);
                } else {
                    helper.setImageResource(R.id.imgStandInside, R.drawable.icon_stand_inside_read);
                }
            }
        };

        listView = (EndOfListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnEndOfListListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                View viewDialog = getLayoutInflater().inflate(R.layout.dialog_stand_inside_letter, null);
                TextView tvLetter = (TextView) viewDialog.findViewById(R.id.tvLetter);

                tvLetter.setText(Html.fromHtml(DataUtil.urlDecode(adapter.getItem(position).content)));
                AlertDialog.Builder builder = new AlertDialog.Builder(MyStandInsideLetterActivity.this);
//                TextView textView = new TextView(MyStandInsideLetterActivity.this);
//                textView.setText(Html.fromHtml(DataUtil.urlDecode(adapter.getItem(position).content)));
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//                textView.setGravity(Gravity.CENTER_VERTICAL);
//                textView.setPadding(10, 10, 10, 10);
//                textView.setLayoutParams(params);
                builder.setView(viewDialog);
                builder.setPositiveButton("确定", null);
                builder.create();
                builder.show();
            }
        });
    }
    private void showDialog() {

    }

    private void loadData() {
        getStandInsideLetter();
    }

    private void getStandInsideLetter() {

        ApiClient.getStandInsideLetter(TAG, page, new ResponseListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onResponse(Object response) {

                showIndeterminateProgress(false);

                StandInsideLetter result = (StandInsideLetter) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(MyStandInsideLetterActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }

                List<StandInsideLetterList> list = ((StandInsideLetter) response).data.list;
                dataList.addAll(list);
                adapter.replaceAll(dataList);

                isFirstLoadingomplete = true;
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                showIndeterminateProgress(false);
            }
        });
    }


}
