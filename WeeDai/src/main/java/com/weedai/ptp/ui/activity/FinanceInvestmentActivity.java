package com.weedai.ptp.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.weedai.ptp.R;
import com.weedai.ptp.model.InvestList;
import com.weedai.ptp.utils.DataUtil;
import com.weedai.ptp.view.SimpleWaveView;

/**
 * 投资理财
 */
public class FinanceInvestmentActivity extends BaseActivity {


    private SimpleWaveView simpleWaveView;
    private TextView tvScale;
    private ImageView imgAward;
    private TextView tvTitle;
    private TextView tvAmount;
    private TextView tvApr;
    private TextView tvAward;
    private TextView tvLimitTime;
    private TextView tvProgress;
    private TextView tvRemainingmount;
    private EditText etInvestmentmount;
    private Button btnTopUp;
    private Button btnOk;

    private InvestList data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_investment);

        if (getIntent().hasExtra("data")) {
            data = (InvestList) getIntent().getSerializableExtra("data");
        }

        initView();
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_finance_investment;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    private void initView() {

        simpleWaveView = (SimpleWaveView) findViewById(R.id.simpleWaveView);
        tvScale = (TextView) findViewById(R.id.tvScale);
        imgAward = (ImageView) findViewById(R.id.imgAward);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvAmount = (TextView) findViewById(R.id.tvAmount);
        tvApr = (TextView) findViewById(R.id.tvApr);
        tvAward = (TextView) findViewById(R.id.tvAward);
        tvLimitTime = (TextView) findViewById(R.id.tvLimitTime);
        tvProgress = (TextView) findViewById(R.id.tvProgress);
        tvRemainingmount = (TextView) findViewById(R.id.tvRemainingmount);

        etInvestmentmount = (EditText) findViewById(R.id.etInvestmentmount);
        btnTopUp = (Button) findViewById(R.id.btnTopUp);
        btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        setInfo();
    }

    private void setInfo() {

        if (data != null) {
            tvTitle.setText(DataUtil.urlDecode(data.name));
            String award = data.award;
            if (award.equals("0")) {
                tvAward.setText("暂无");
                imgAward.setVisibility(View.GONE);
            } else {
                tvAward.setText(award + "%");
                imgAward.setVisibility(View.VISIBLE);

            }
            tvApr.setText(data.apr + "%");
            tvAmount.setText(data.account + "元");
            tvLimitTime.setText(data.time_limit + "个月");

            final float scale = data.scale;
            float percentage = scale / 100;
            simpleWaveView.setColor(getResources().getColor(R.color.main_text_orange));
            simpleWaveView.setPercentage(percentage);
            tvScale.setText(scale + "%");
        }
    }
}
