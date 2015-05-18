package com.weedai.ptp.ui.activity;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.weedai.ptp.R;
import com.weedai.ptp.constant.Constant;

public class MyBankCardChangeActivity extends BaseActivity {

    private ImageView imgBankIcon;
    private EditText etBankBranch;
    private EditText etBankCode;
    private Button btnModifyBankCard;

    private Spinner spinner;
    private ArrayAdapter<String> adapter;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bank_card_change);

        initView();
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_change_bank;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    private void initView() {

        imgBankIcon = (ImageView) findViewById(R.id.imgBankIcon);
        spinner = (Spinner) findViewById(R.id.spinner);
        etBankBranch = (EditText) findViewById(R.id.etBankBranch);
        etBankCode = (EditText) findViewById(R.id.etBankCode);


        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Constant.bankNameList);
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter 添加到spinner中
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnModifyBankCard = (Button) findViewById(R.id.btnModifyBankCard);
        btnModifyBankCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String branch = etBankBranch.getText().toString();
                String code = etBankCode.getText().toString();

                if (TextUtils.isEmpty(branch)) {
                    Toast.makeText(MyBankCardChangeActivity.this, "请填写支行名称", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(MyBankCardChangeActivity.this, "请填写银行卡号", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
}
