package com.weedai.ptp.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.model.About;
import com.weedai.ptp.model.BaseModel;
import com.weedai.ptp.utils.DataUtil;
import com.weedai.ptp.volley.ResponseListener;

/**
 * 奖品折现
 */
public class AwardConvertActity extends BaseActivity {

    private final static String TAG = "AwardConvertActity";

    private TextView tvAward;
    private Button btnAwardConvert;

    private String id;
    private String name;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_award_convert);

        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");

        tvAward = (TextView) findViewById(R.id.tvAward);
        btnAwardConvert = (Button) findViewById(R.id.btnAwardConvert);

        tvAward.setText( DataUtil.urlDecode(name));
        btnAwardConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                awardConvert();
            }
        });
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_award_convert;
    }

    private void awardConvert() {

        ApiClient.awardConvert(TAG, id, new ResponseListener() {
            @Override
            public void onStarted() {
                progressDialog = ProgressDialog.show(AwardConvertActity.this, null, getString(R.string.message_submit));
            }

            @Override
            public void onResponse(Object response) {
                progressDialog.dismiss();

                BaseModel result = (BaseModel) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(AwardConvertActity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (result.message.equals("success")) {
                    btnAwardConvert.setEnabled(false);
                    Toast.makeText(AwardConvertActity.this, "奖品折现成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AwardConvertActity.this, "奖品折现失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
            }
        });
    }


}
