package com.weedai.ptp.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.lemon.aklib.adapter.BaseAdapterHelper;
import com.lemon.aklib.adapter.QuickAdapter;
import com.lemon.aklib.widget.EndOfListView;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.model.BaseModel;
import com.weedai.ptp.model.StandInsideLetter;
import com.weedai.ptp.model.StandInsideLetterList;
import com.weedai.ptp.utils.DataUtil;
import com.weedai.ptp.utils.UIHelper;
import com.weedai.ptp.volley.ResponseListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private CheckBox cbLetterAll;
    private TextView tvDelete;
    private TextView tvLetterRead;

    private List<String> ids = new ArrayList<String>();

    private List<String> idChecks = new ArrayList<String>();

    private Map<Integer, StandInsideLetterList> selectMap = new HashMap<Integer, StandInsideLetterList>();

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
            protected void convert(BaseAdapterHelper helper, final StandInsideLetterList item) {

                final String addTime = item.addtime + "000";
                SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 hh:mm");
                String time = sdf.format(Long.parseLong(addTime));

                helper.setText(R.id.tvTitle, Html.fromHtml(DataUtil.urlDecode(item.name)));
                helper.setText(R.id.tvTime, time);

                final int position = helper.getPosition();
                CheckBox cbLetter = helper.getView(R.id.cbLetter);
                if (selectMap.get(position) != null) {
                    cbLetter.setChecked(true);
                } else {
                    cbLetter.setChecked(false);
                }
                cbLetter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (selectMap.get(position) != null) {
                            selectMap.remove(position);
                        } else {
                            selectMap.put(position, item);
                        }
                    }
                });

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
        registerForContextMenu(listView);//为ListView添加上下文


        cbLetterAll = (CheckBox) findViewById(R.id.cbLetterAll);
        tvDelete = (TextView) findViewById(R.id.tvDelete);
        tvLetterRead = (TextView) findViewById(R.id.tvLetterRead);

        cbLetterAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cbLetterAll.isChecked()) {
                    System.out.println("cbLetterAll.isChecked() " + cbLetterAll.isChecked());

                    int size = dataList.size();
                    selectMap.clear();
                    for (int i = 0; i < size; i++) {
                        selectMap.put(i, dataList.get(i));
                    }
                } else {
                    System.out.println("cbLetterAll.isChecked() is not  " + cbLetterAll.isChecked());
                    selectMap.clear();
                }
                adapter.notifyDataSetChanged();
            }
        });

        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectMap.size() == 0) {
                    Toast.makeText(MyStandInsideLetterActivity.this, "请选择信件", Toast.LENGTH_SHORT).show();
                    return;
                }

                ids.clear();
                for (Map.Entry<Integer, StandInsideLetterList> entry : selectMap.entrySet()) {
                    System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
                    ids.add(((StandInsideLetterList) entry.getValue()).id);
                }
                System.out.println("ids " + ids.toString());
                toDelete(ids);


//                adapter.notifyDataSetChanged();
//                List<StandInsideLetterList> listTmp = new ArrayList<StandInsideLetterList>();
//                for (StandInsideLetterList item : dataList) {
//                    listTmp.add(item);
//                }
//                System.out.println("dataList ' size == " + dataList.size());
//                System.out.println("listTmp ' size == " + listTmp.size());
//                for (int i = 0; i < selectPositions.size(); i++) {
//                    System.out.println("position " + selectPositions.get(i));
//                    selectMap.remove(selectPositions.get(i));
////                        dataList.remove(position);
//                    System.out.println("remove is " + listTmp.remove(selectPositions.get(i)));
////                        adapter.remove(position);
//                }
//                dataList = listTmp;

                System.out.println("dataList ' size == " + dataList.size());
            }
        });
        tvLetterRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (selectMap.size() == 0) {
                    Toast.makeText(MyStandInsideLetterActivity.this, "请选择信件", Toast.LENGTH_SHORT).show();
                    return;
                }

                ids.clear();
                for (Map.Entry<Integer, StandInsideLetterList> entry : selectMap.entrySet()) {
                    System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
                    ids.add(((StandInsideLetterList) entry.getValue()).id);
                }
                System.out.println("ids " + ids.toString());
                toRead(ids);
            }
        });
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
                    if (selectMap.size() == 0) {  //单选
                        System.out.println("mess_yidu_suc");
                        adapter.getItem(selectPosition).status = 1;
                        adapter.notifyDataSetChanged();
                    }
//                    else {  //多选
//                        for (Map.Entry<Integer, StandInsideLetterList> entry : selectMap.entrySet()) {
//                            adapter.getItem(entry.getKey()).status = 1;
//                        }
//                        adapter.notifyDataSetChanged();
//                    }
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
            }
        });
    }


    private void toRead(List<String> id) {
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
                    //多选
                    for (Map.Entry<Integer, StandInsideLetterList> entry : selectMap.entrySet()) {
                        adapter.getItem(entry.getKey()).status = 1;
                    }
                    selectMap.clear();
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
                    if (selectMap.size() == 0) {  //单选
                        System.out.println("mess_del_suc");
                        adapter.remove(selectPosition);
                    }
//                    else {
//                        List<Integer> selectPositions = new ArrayList<Integer>();
//                        for (Map.Entry<Integer, StandInsideLetterList> entry : selectMap.entrySet()) {
////                            selectPositions.add(entry.getKey());
//                            adapter.remove(entry.getKey());
//                        }
//                    }
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
            }
        });
    }

    private void toDelete(List<String> id) {
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

                    List<Integer> selectPositions = new ArrayList<Integer>();
                    for (Map.Entry<Integer, StandInsideLetterList> entry : selectMap.entrySet()) {
                        System.out.println("entry.getKey() " + entry.getKey());
//                        selectPositions.add(entry.getKey());
                        System.out.println("remove is " + dataList.remove(entry.getValue()));
                    }
                    selectMap.clear();
                    List<StandInsideLetterList> listTmp = new ArrayList<StandInsideLetterList>();
                    listTmp.addAll(dataList);
                    dataList.clear();
                    dataList.addAll(listTmp);
                    adapter.replaceAll(dataList);
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
            }
        }
    }

}
