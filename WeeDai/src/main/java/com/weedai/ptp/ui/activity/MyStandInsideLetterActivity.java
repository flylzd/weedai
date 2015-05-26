package com.weedai.ptp.ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.lemon.aklib.adapter.BaseAdapterHelper;
import com.lemon.aklib.adapter.QuickAdapter;
import com.lemon.aklib.widget.EndOfListView;
import com.nostra13.universalimageloader.cache.memory.impl.FIFOLimitedMemoryCache;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.model.Bank;
import com.weedai.ptp.model.BaseModel;
import com.weedai.ptp.model.StandInsideLetter;
import com.weedai.ptp.model.StandInsideLetterList;
import com.weedai.ptp.utils.DataUtil;
import com.weedai.ptp.utils.UIHelper;
import com.weedai.ptp.volley.ResponseListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MyStandInsideLetterActivity extends BaseActivity implements EndOfListView.OnEndOfListListener {

    private final static String TAG = "MyStandInsideLetterActivity";

    private EndOfListView listView;
    private QuickAdapter<StandInsideLetterList> adapter;
    private List<StandInsideLetterList> dataList = new ArrayList<StandInsideLetterList>();

    private final static int DEFAULT_PAGE = 0;
    private int page = DEFAULT_PAGE;

    private boolean isBottomLoadingComplete = false;

    private ProgressDialog progressDialog;

    private int selectPosition;

    public static final int DETAIL_DELETE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_stand_inside_letter);

        initView();
//        loadData();
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

        if (isBottomLoadingComplete) {
            showIndeterminateProgress(false);
            return;
        }
        page++;
        getStandInsideLetter();
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
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                selectPosition = position;

//                View viewDialog = getLayoutInflater().inflate(R.layout.dialog_stand_inside_letter, null);
//                TextView tvLetter = (TextView) viewDialog.findViewById(R.id.tvLetter);
//
//                tvLetter.setText(Html.fromHtml(DataUtil.urlDecode(adapter.getItem(position).content)));
//                AlertDialog.Builder builder = new AlertDialog.Builder(MyStandInsideLetterActivity.this);
//                builder.setView(viewDialog);
//                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        toRead(adapter.getItem(position).id);
//                    }
//                });
//                builder.create();
//                builder.show();

                adapter.getItem(selectPosition).status = 1;
                adapter.notifyDataSetChanged();
                UIHelper.showMyStandInsideLetterDetail(MyStandInsideLetterActivity.this, adapter.getItem(selectPosition), DETAIL_DELETE);
            }
        });
        registerForContextMenu(listView);//为ListView添加上下文菜单
    }

    private void loadData() {
        getStandInsideLetter();
    }

    private void getStandInsideLetter() {

        ApiClient.getStandInsideLetter(TAG, page, new ResponseListener() {
            @Override
            public void onStarted() {
                showIndeterminateProgress(true);
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

                int currentPage = result.data.page;
                int totalPage = result.data.total_page;
                if (currentPage == totalPage || totalPage == 0) {
                    isBottomLoadingComplete = true;
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                showIndeterminateProgress(false);
            }
        });
    }


    private void toRead(String id) {
        ApiClient.standInsideLetterToRead(TAG, id, new ResponseListener() {
            @Override
            public void onStarted() {
                progressDialog = ProgressDialog.show(MyStandInsideLetterActivity.this, null, getString(R.string.message_waiting));
            }

            @Override
            public void onResponse(Object response) {
                progressDialog.dismiss();
                BaseModel result = (BaseModel) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(MyStandInsideLetterActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }

                String message = result.message;
                if ("mess_yidu_suc".equals(message)) {
                    System.out.println("mess_yidu_suc");
                    adapter.getItem(selectPosition).status = 1;
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
            }
        });
    }

    private void toDelete(String id) {
        ApiClient.standInsideLetterToDelete(TAG, id, new ResponseListener() {
            @Override
            public void onStarted() {
                progressDialog = ProgressDialog.show(MyStandInsideLetterActivity.this, null, getString(R.string.message_submit));
            }

            @Override
            public void onResponse(Object response) {
                progressDialog.dismiss();
                BaseModel result = (BaseModel) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(MyStandInsideLetterActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }

                String message = result.message;
                if ("mess_del_suc".equals(message)) {
                    System.out.println("mess_del_suc");
                    adapter.remove(selectPosition);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("操作");
        menu.add(0, 1, 0, "删除该站内信");
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo itemInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case 1:
                selectPosition = itemInfo.position;
                toDelete(adapter.getItem(itemInfo.position).id);
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    // 回调方法，从第二个页面回来的时候会执行这个方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("onActivityResult resultCode " + resultCode);
        System.out.println("onActivityResult requestCode " + requestCode);
        if (resultCode == RESULT_OK) {
            System.out.println("onActivityResult ");
            if (requestCode == DETAIL_DELETE) {
                System.out.println("删除");

                adapter.remove(selectPosition);
                adapter.notifyDataSetChanged();
            }
        }
    }

}
