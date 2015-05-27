package com.weedai.ptp.ui.activity;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.model.Bank;
import com.weedai.ptp.model.BankData;
import com.weedai.ptp.model.Money;
import com.weedai.ptp.utils.DataUtil;
import com.weedai.ptp.utils.UIHelper;
import com.weedai.ptp.volley.ResponseListener;

import java.io.UnsupportedEncodingException;

public class MyBankCardActivity extends BaseActivity {

    private final static String TAG = "MyBankCardActivity";

    private ImageView imgBankIcon;
    private TextView tvRealName;
    private TextView tvBankName;
    private TextView tvRealEmail;
    private TextView tvBankBranch;
    private TextView tvBankCode;
    private Button btnModifyBankCard;

    private String bank;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bank_card);

        initView();
//        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_my_bank;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    private void initView() {

        imgBankIcon = (ImageView) findViewById(R.id.imgBankIcon);
        tvRealName = (TextView) findViewById(R.id.tvRealName);
        tvRealEmail = (TextView) findViewById(R.id.tvRealEmail);
        tvBankBranch = (TextView) findViewById(R.id.tvBankBranch);
        tvBankCode = (TextView) findViewById(R.id.tvBankCode);
        tvBankName = (TextView) findViewById(R.id.tvBankName);

        btnModifyBankCard = (Button) findViewById(R.id.btnModifyBankCard);
        btnModifyBankCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.showMyBankCardChange(MyBankCardActivity.this,bank );
            }
        });
    }

    private void setInfo(BankData data) {

        if (data != null) {

            String realname = data.realname;
            String banksname =  data.banksname;
            String branch = data.branch;
            String account = data.account;



            tvRealName.setText(DataUtil.urlDecode(data.realname));
            tvRealEmail.setText(DataUtil.urlDecode(data.email));
            tvBankBranch.setText(DataUtil.urlDecode(DataUtil.urlDecode(data.branch)));
//            tvBankBranch.setText(DataUtil.urlDecode(data.branch));
            tvBankCode.setText(DataUtil.urlDecode(data.account));
            tvBankName.setText(DataUtil.urlDecode(data.banksname));

            if (!TextUtils.isEmpty(banksname)) {
                int resId = Constant.bankImgMap.get(DataUtil.urlDecode(data.banksname));
                imgBankIcon.setImageResource(resId);
                bank = data.bank;
            }
        }
    }

    private void loadData() {
        getBank();
    }

    private void getBank() {
        ApiClient.getBank(TAG, new ResponseListener() {
            @Override
            public void onStarted() {
                progressDialog = ProgressDialog.show(MyBankCardActivity.this, null, getString(R.string.message_waiting));
            }

            @Override
            public void onResponse(Object response) {
                progressDialog.dismiss();

                Bank result = (Bank) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(MyBankCardActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }

                setInfo(result.data);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
            }
        });
    }
}