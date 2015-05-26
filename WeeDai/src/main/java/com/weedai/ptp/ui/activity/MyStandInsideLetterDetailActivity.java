package com.weedai.ptp.ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.model.BaseModel;
import com.weedai.ptp.model.StandInsideLetter;
import com.weedai.ptp.model.StandInsideLetterList;
import com.weedai.ptp.utils.DataUtil;
import com.weedai.ptp.volley.ResponseListener;

import java.text.SimpleDateFormat;

public class MyStandInsideLetterDetailActivity extends BaseActivity {

    private final static String TAG = "MyStandInsideLetterDetailActivity";

    private TextView tvName;
    private TextView tvContent;
    private TextView tvTime;

    private Button tvDelete;

    private ProgressDialog progressDialog;

    private String id;
    private StandInsideLetterList item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_stand_inside_letter_detail);

        if (getIntent().hasExtra("data")) {
            item = (StandInsideLetterList) getIntent().getSerializableExtra("data");
            id = item.id;
        }

        initView();
        loadData();
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_stand_inside_letter;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    private void initView() {

        tvName = (TextView) findViewById(R.id.tvName);
        tvContent = (TextView) findViewById(R.id.tvContent);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvDelete = (Button) findViewById(R.id.tvDelete);
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MyStandInsideLetterDetailActivity.this);
                builder.setTitle("提示");
                builder.setMessage("确定删除此信件");
                builder.setNegativeButton("取消",null);
                builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toDelete(id);
                    }
                });
                builder.create();
                builder.show();
            }
        });
        setInfo();
    }

    private void setInfo() {
        if (item != null) {
            tvName.setText(Html.fromHtml(DataUtil.urlDecode(item.name)));
            tvContent.setText(Html.fromHtml(DataUtil.urlDecode(item.content)));

            String addTime = item.addtime + "000";
            SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 hh:mm");
            String time = sdf.format(Long.parseLong(addTime));
            tvTime.setText(time);
        }
    }

    private void loadData() {
        toRead(id);
    }

    private void toRead(String id) {

        ApiClient.standInsideLetterToRead(TAG, id, new ResponseListener() {
            @Override
            public void onStarted() {
                progressDialog = ProgressDialog.show(MyStandInsideLetterDetailActivity.this, null, getString(R.string.message_waiting));
            }

            @Override
            public void onResponse(Object response) {
                progressDialog.dismiss();

                BaseModel result = (BaseModel) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(MyStandInsideLetterDetailActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
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
                progressDialog = ProgressDialog.show(MyStandInsideLetterDetailActivity.this, null, getString(R.string.message_submit));
            }

            @Override
            public void onResponse(Object response) {
                progressDialog.dismiss();
                BaseModel result = (BaseModel) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(MyStandInsideLetterDetailActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }

                String message = result.message;
                if ("mess_del_suc".equals(message)) {
                    System.out.println("mess_del_suc");
//                    adapter.remove(selectPosition);
//                    adapter.notifyDataSetChanged();
//                    getStandInsideLetter();
                    setResult(RESULT_OK);
                    finish();
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
            }
        });
    }
}
