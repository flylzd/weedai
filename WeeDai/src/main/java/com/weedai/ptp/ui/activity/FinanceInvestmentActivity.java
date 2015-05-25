package com.weedai.ptp.ui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.model.ArticleDetail;
import com.weedai.ptp.model.BaseModel;
import com.weedai.ptp.model.InvestList;
import com.weedai.ptp.model.MyWeallth;
import com.weedai.ptp.model.Valicode;
import com.weedai.ptp.utils.DataUtil;
import com.weedai.ptp.view.SimpleValidateCodeView;
import com.weedai.ptp.view.SimpleWaveView;
import com.weedai.ptp.volley.ResponseListener;

/**
 * 投资理财
 */
public class FinanceInvestmentActivity extends BaseActivity {

    private final static String TAG = "FinanceInvestmentActivity";

    private SimpleWaveView simpleWaveView;
    private TextView tvScale;
    private ImageView imgAward;
    private TextView tvTitle;
    private TextView tvAmount;
    private TextView tvApr;
    private TextView tvAward;
    private TextView tvLimitTime;
    private TextView tvProgress;
    private TextView tvRemainingAmount;
    private EditText etInvestmentAmount;
    private Button btnTopUp;
    private Button btnOk;

    private InvestList data;

    private ProgressDialog progressDialog;

    private AlertDialog alertDialog;
    private EditText etPayPassword;
    private SimpleValidateCodeView simpleValidateCodeView;
    private String valicode;

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
        tvRemainingAmount = (TextView) findViewById(R.id.tvRemainingAmount);

        etInvestmentAmount = (EditText) findViewById(R.id.etInvestmentAmount);
        btnTopUp = (Button) findViewById(R.id.btnTopUp);
        btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String money = etInvestmentAmount.getText().toString();
                if (TextUtils.isEmpty(money)) {
                    Toast.makeText(FinanceInvestmentActivity.this, "请输入投资金额", Toast.LENGTH_SHORT).show();
                    return;
                }
                float bidMoney = Float.parseFloat(money);
                if (bidMoney < 50) {
                    Toast.makeText(FinanceInvestmentActivity.this, "亲，投资金额不能低于50元", Toast.LENGTH_SHORT).show();
                    return;
                }
                String remainAmount = tvRemainingAmount.getText().toString();
                remainAmount = remainAmount.substring(0, remainAmount.indexOf("元"));
                float userMoney = Float.parseFloat(remainAmount);
                if (bidMoney > userMoney) {
                    Toast.makeText(FinanceInvestmentActivity.this, "亲，投资金额不能超过剩余金额", Toast.LENGTH_SHORT).show();
                    return;
                }

                showDialog();
            }
        });

        setInfo();

        getMyWealth();
    }

    private void showDialog() {

        getImgcode();

        View view = getLayoutInflater().inflate(R.layout.dialog_bid, null);
        etPayPassword = (EditText) view.findViewById(R.id.etPayPassword);
        simpleValidateCodeView = (SimpleValidateCodeView) view.findViewById(R.id.viewValicode);
        final EditText etValicode = (EditText) view.findViewById(R.id.etValicode);
        simpleValidateCodeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImgcode();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(FinanceInvestmentActivity.this);
        builder.setTitle("支付确认");
        builder.setView(view);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog = builder.create();
        alertDialog.show();

        alertDialog.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = etValicode.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(FinanceInvestmentActivity.this, getString(R.string.login_valicode_empty), Toast.LENGTH_SHORT).show();
                } else {
                    if (!valicode.equalsIgnoreCase(code)) {
                        Toast.makeText(FinanceInvestmentActivity.this, getString(R.string.login_valicode_not_match), Toast.LENGTH_SHORT).show();
                        getImgcode();
                        etValicode.getText().clear();
                    } else {
                        String money = etInvestmentAmount.getText().toString();
                        String paypassword = etPayPassword.getText().toString();
//                        String valicode = etValicode.getText().toString();

                        tender(data.id, money, paypassword, valicode);
                    }
                }
            }
        });
    }

    private void setInfo() {

        if (data != null) {
            tvTitle.setText(DataUtil.urlDecode(data.name));
            if (TextUtils.isEmpty(data.funds)) {
                tvAward.setText("暂无");
                imgAward.setVisibility(View.GONE);
            } else {
                tvAward.setText(data.funds + "%");
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


    private void tender(String id, String money, String paypassword, String valicode) {

        ApiClient.tender(TAG, id, money, paypassword, valicode, new ResponseListener() {
            @Override
            public void onStarted() {
                progressDialog = ProgressDialog.show(FinanceInvestmentActivity.this, null, getString(R.string.message_submit));
            }

            @Override
            public void onResponse(Object response) {
                progressDialog.dismiss();

                BaseModel result = (BaseModel) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(FinanceInvestmentActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }
                String message = result.message;
                if (message.equals("valicode_fail")) {
                    Toast.makeText(FinanceInvestmentActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                } else if (message.equals("account_lock")) {
                    Toast.makeText(FinanceInvestmentActivity.this, "账户被锁定", Toast.LENGTH_SHORT).show();
                } else if (message.equals("barray_fail")) {
                    Toast.makeText(FinanceInvestmentActivity.this, "获取数据异常", Toast.LENGTH_SHORT).show();
                } else if (message.equals("biao_man")) {
                    Toast.makeText(FinanceInvestmentActivity.this, "标已满，请勿再投", Toast.LENGTH_SHORT).show();
                } else if (message.equals("biao_noshenhe")) {
                    Toast.makeText(FinanceInvestmentActivity.this, "该标没审核", Toast.LENGTH_SHORT).show();
                } else if (message.equals("biao_past")) {
                    Toast.makeText(FinanceInvestmentActivity.this, "此标已过期", Toast.LENGTH_SHORT).show();
                } else if (message.equals("more_money")) {
                    Toast.makeText(FinanceInvestmentActivity.this, "投标超出标的总金额", Toast.LENGTH_SHORT).show();
                } else if (message.equals("paysercet_fail")) {
                    Toast.makeText(FinanceInvestmentActivity.this, "支付密码不正确", Toast.LENGTH_SHORT).show();
                } else if (message.equals("yue_notenough")) {
                    Toast.makeText(FinanceInvestmentActivity.this, "余额不足", Toast.LENGTH_SHORT).show();
                } else if (message.equals("tender_yichang")) {
                    Toast.makeText(FinanceInvestmentActivity.this, "投标异常", Toast.LENGTH_SHORT).show();
                } else if (message.equals("need_lowest")) {
                    Toast.makeText(FinanceInvestmentActivity.this, "投标金额不能低于最小限额", Toast.LENGTH_SHORT).show();
                } else if (message.equals("tender_succ")) {
                    Toast.makeText(FinanceInvestmentActivity.this, "投标成功", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();

                    getMyWealth();
                    etInvestmentAmount.getText().clear();
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
            }
        });
    }


    private void getMyWealth() {

        ApiClient.getMyWealth(TAG, new ResponseListener() {
            @Override
            public void onStarted() {
                progressDialog = ProgressDialog.show(FinanceInvestmentActivity.this, null, getString(R.string.message_waiting));
            }

            @Override
            public void onResponse(Object response) {
                progressDialog.dismiss();

                MyWeallth result = (MyWeallth) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(FinanceInvestmentActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }
                String useMoney = result.data.use_money;
                if (!TextUtils.isEmpty(useMoney)){
                    tvRemainingAmount.setText(result.data.use_money + " 元");
                }else {
                    tvRemainingAmount.setText(0 + " 元");
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
            }
        });
    }

    private void getImgcode() {

        ApiClient.getImgcode(TAG, new ResponseListener() {
            @Override
            public void onStarted() {
            }

            @Override
            public void onResponse(Object response) {

                Valicode result = (Valicode) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(FinanceInvestmentActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }
                valicode = result.data.code;
                System.out.println("valicode : " + valicode);
                String code = result.data.code;
                int length = code.length();
                String[] codes = new String[length];
                for (int i = 0; i < length; i++) {
                    codes[i] = String.valueOf(code.charAt(i));
                }
                simpleValidateCodeView.getValidataAndSetImage(codes);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
    }
}
