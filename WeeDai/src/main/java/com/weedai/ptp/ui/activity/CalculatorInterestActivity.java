package com.weedai.ptp.ui.activity;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.lemon.aklib.adapter.BaseAdapterHelper;
import com.lemon.aklib.adapter.QuickAdapter;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.model.Article;
import com.weedai.ptp.model.ArticleList;
import com.weedai.ptp.model.Calculator;
import com.weedai.ptp.model.CalculatorList;
import com.weedai.ptp.volley.ResponseListener;

import java.util.ArrayList;
import java.util.List;

public class CalculatorInterestActivity extends BaseActivity {

    private final static String TAG = "CalculatorInterestActivity";

    private EditText etAmount;
    private EditText etApr;
    private EditText etLimit;
    private TextView tvType;
    private Button btnCalculator;

    private TextView tvAprYear;
    private TextView tvAprMonthly;
    private TextView tvRepaymentMonthly;
    private TextView tvRepaymentAccount;

    private ListView listView;
    private QuickAdapter<CalculatorList> adapter;
    private List<CalculatorList> dataList = new ArrayList<CalculatorList>();

    private int type = 3;   //0等额本息
    //3到期还本,按月付息
    private AlertDialog alertDialog;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator_interest);

        initView();
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_calculator_interest;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    private void initView() {

        etAmount = (EditText) findViewById(R.id.etAmount);
        etApr = (EditText) findViewById(R.id.etApr);
        etLimit = (EditText) findViewById(R.id.etLimit);
        tvType = (TextView) findViewById(R.id.tvType);
        btnCalculator = (Button) findViewById(R.id.btnCalculator);

        tvAprYear = (TextView) findViewById(R.id.tvAprYear);
        tvAprMonthly = (TextView) findViewById(R.id.tvAprMonthly);
        tvRepaymentMonthly = (TextView) findViewById(R.id.tvRepaymentMonthly);
        tvRepaymentAccount = (TextView) findViewById(R.id.tvRepaymentAccount);

        tvType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(CalculatorInterestActivity.this);
                builder.setTitle("还款方式");
                View view = getLayoutInflater().inflate(R.layout.dialog_calculator_interest, null);
                final TextView tvType3 = (TextView) view.findViewById(R.id.tvType3);
                final TextView tvType0 = (TextView) view.findViewById(R.id.tvType0);
                tvType0.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        type = 0;
                        tvType.setText(tvType0.getText().toString());
                        alertDialog.dismiss();
                    }
                });
                tvType3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        type = 3;
                        tvType.setText(tvType3.getText().toString());
                        alertDialog.dismiss();
                    }
                });
                builder.setView(view);
                builder.create();
                alertDialog = builder.show();
            }
        });

        btnCalculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String amount = etAmount.getText().toString();
                String apr = etApr.getText().toString();
                String limit = etLimit.getText().toString();

                if (TextUtils.isEmpty(amount) || TextUtils.isEmpty(apr) || TextUtils.isEmpty(limit)) {
                    Toast.makeText(CalculatorInterestActivity.this, "数据不能为空!", Toast.LENGTH_SHORT).show();
                    return;
                }

                calculator(amount, apr, limit, type);
            }
        });

//        adapter = new QuickAdapter<CalculatorList>(CalculatorInterestActivity.this, R.layout.listitem_calculator_interest) {
//            @Override
//            protected void convert(BaseAdapterHelper helper, CalculatorList item) {
//
//                helper.setText(R.id.tvRepaymentMonthly, item.monthpay);
//                helper.setText(R.id.tvRepaymentAccount, item.allpay);
//
//            }
//        };
//
//        listView = (ListView) findViewById(R.id.listView);
//        listView.setAdapter(adapter);

    }

    private void calculator(String account, final String lilv, final String times, int type) {

        ApiClient.calculatorInterest(TAG, account, lilv, times, type, new ResponseListener() {
            @Override
            public void onStarted() {
                progressDialog = ProgressDialog.show(CalculatorInterestActivity.this, null, "正在提交数据");
            }

            @Override
            public void onResponse(Object response) {
                progressDialog.dismiss();

                Calculator result = (Calculator) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(CalculatorInterestActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }

                tvAprYear.setText(lilv);
                tvAprMonthly.setText(String.valueOf(Float.parseFloat(lilv) / Float.parseFloat(times)));

                tvRepaymentMonthly.setText(result.data.monthpay);
                tvRepaymentAccount.setText(result.data.allpay);

//                List<CalculatorList> list = result.data.list;
//                if (list != null && list.size() != 0) {
//                    dataList = list;
//                    adapter.replaceAll(dataList);
//                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
            }
        });
    }
}
