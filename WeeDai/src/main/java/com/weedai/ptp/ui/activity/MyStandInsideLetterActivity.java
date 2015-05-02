package com.weedai.ptp.ui.activity;

import android.os.Bundle;

import com.lemon.aklib.adapter.QuickAdapter;
import com.lemon.aklib.widget.EndOfListView;
import com.weedai.ptp.R;
import com.weedai.ptp.model.MoneyList;

import java.util.ArrayList;
import java.util.List;

public class MyStandInsideLetterActivity extends BaseActivity implements EndOfListView.OnEndOfListListener {

    private final static String TAG = "MyStandInsideLetterActivity";

    private EndOfListView listView;
    private QuickAdapter<MoneyList> adapter;
    private List<MoneyList> dataList = new ArrayList<MoneyList>();

    private final static int DEFAULT_PAGE = 1;
    private int page = DEFAULT_PAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_stand_inside_letter);

        initView();
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

    }

    private void showIndeterminateProgress(boolean visibility) {
//        adapter.showIndeterminateProgress(visibility);
    }

    private void initView() {

    }


}
