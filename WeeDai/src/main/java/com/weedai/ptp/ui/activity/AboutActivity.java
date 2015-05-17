package com.weedai.ptp.ui.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.weedai.ptp.R;
import com.weedai.ptp.utils.UIHelper;

public class AboutActivity extends BaseActivity {

    private Button btnPartner;
    private Button btnAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        initView();
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_about;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    private void initView() {

        btnPartner = (Button) findViewById(R.id.btnPartner);
        btnAbout = (Button) findViewById(R.id.btnAbout);

        btnPartner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.showPartner(AboutActivity.this);
            }
        });
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.showAboutCompany(AboutActivity.this);
            }
        });
    }
}
