package com.weedai.ptp.ui.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.constant.Urls;
import com.weedai.ptp.model.Bank;
import com.weedai.ptp.model.BaseModel;
import com.weedai.ptp.model.Valicode;
import com.weedai.ptp.utils.AppUtil;
import com.weedai.ptp.utils.DataUtil;
import com.weedai.ptp.utils.UIHelper;
import com.weedai.ptp.view.SimpleValidateCodeView;
import com.weedai.ptp.volley.ResponseListener;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyRechargeActivity extends Activity {

    private final static String TAG = "MyRechargeActivity";


    //线下充值
    private TextView tvRealName;
    private TextView tvAccount;
    private EditText etAmount;
    private EditText etValicode;
    private EditText etWaterAccount;
    private Button btnOk;

    private SimpleValidateCodeView viewValicode;
    private String valicode;

    //在线充值
//    private Button btnOnline;
//    private TextView tvRealNameOnline;
//    private EditText etValicodeOnline;
//    private SimpleValidateCodeView viewValicodeOnline;
//    private EditText etAmountOnline;

    //在线充值(新)
    private GridView gridViewBank;
    private BankAdapter adapter;

    private EditText etAmountOnline;
    private EditText etRealNameOnline;
    private EditText etIDCardOnline;
    private EditText etBankCodeOnline;
    private ImageView imgBankIcon;
    private EditText etValicodeOnline;
    private Button btnValicodeOnline;
    private Button btnOnline;
    private AlertDialog bankDialog;
    private String bankCode;

    private ProgressDialog progressDialog;

    private ImageView imgBack;
    private Button btnOfflineTopUp;
    private Button btnOnlineTopUp;

    private View layoutOffline;
    private View layoutOnline;

    private WebView webView;


    private int time = 60;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            time--;
            btnValicodeOnline.setEnabled(false);
            btnValicodeOnline.setText("稍后重试(" + time + ")");
            if (time < 0) {
                btnValicodeOnline.setEnabled(true);
                btnValicodeOnline.setText("发送手机验证码");
                time = 60;
                return;
            }
            handler.postDelayed(runnable, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my_recharge);

        initView();
        loadData();
    }

//    @Override
//    protected int getActionBarTitle() {
//        return R.string.title_my_recharge;
//    }
//
//    @Override
//    protected boolean hasBackButton() {
//        return true;
//    }


    private void initView() {

        imgBack = (ImageView) findViewById(R.id.imgBack);
        btnOfflineTopUp = (Button) findViewById(R.id.btnOfflineTopUp);
        btnOnlineTopUp = (Button) findViewById(R.id.btnOnlineTopUp);
        layoutOffline = findViewById(R.id.layoutOffline);
        layoutOnline = findViewById(R.id.layoutOnline);

        layoutOffline.setVisibility(View.GONE);
        layoutOnline.setVisibility(View.VISIBLE);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnOnlineTopUp.setSelected(true);
        btnOfflineTopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                layoutOffline.setVisibility(View.VISIBLE);
                layoutOnline.setVisibility(View.GONE);

                btnOfflineTopUp.setSelected(true);
                btnOnlineTopUp.setSelected(false);

                getImgcode();
            }
        });
        btnOnlineTopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutOffline.setVisibility(View.GONE);
                layoutOnline.setVisibility(View.VISIBLE);

                btnOfflineTopUp.setSelected(false);
                btnOnlineTopUp.setSelected(true);

//                getImgcodeOnline();
            }
        });

        tvRealName = (TextView) findViewById(R.id.tvRealName);
        tvAccount = (TextView) findViewById(R.id.tvAccount);
        etAmount = (EditText) findViewById(R.id.etAmount);
        etValicode = (EditText) findViewById(R.id.etValicode);
        etWaterAccount = (EditText) findViewById(R.id.etWaterAccount);
        viewValicode = (SimpleValidateCodeView) findViewById(R.id.viewValicode);
        viewValicode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImgcode();
            }
        });
        btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String money = etAmount.getText().toString();
                String code = etValicode.getText().toString();
                String remark = etWaterAccount.getText().toString();

                if (TextUtils.isEmpty(money)) {
                    Toast.makeText(MyRechargeActivity.this, "请填写充值金额", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(MyRechargeActivity.this, getString(R.string.login_valicode_empty), Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (!valicode.equalsIgnoreCase(code)) {
                        Toast.makeText(MyRechargeActivity.this, getString(R.string.login_valicode_not_match), Toast.LENGTH_SHORT).show();
                        getImgcode();
                        etValicode.getText().clear();
                        return;
                    }
                }

                recharge(money, code, remark);
            }
        });

        etAmountOnline = (EditText) findViewById(R.id.etAmountOnline);
        etRealNameOnline = (EditText) findViewById(R.id.etRealNameOnline);
        etIDCardOnline = (EditText) findViewById(R.id.etIDCardOnline);
        etBankCodeOnline = (EditText) findViewById(R.id.etBankCodeOnline);
        imgBankIcon = (ImageView) findViewById(R.id.imgBankIcon);
        etValicodeOnline = (EditText) findViewById(R.id.etValicodeOnline);
        btnValicodeOnline = (Button) findViewById(R.id.btnValicodeOnline);
        btnOnline = (Button) findViewById(R.id.btnOnline);

        bankCode = bankNameList.get(0);
        adapter = new BankAdapter(this, bankNameList);

        imgBankIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MyRechargeActivity.this);
                builder.setTitle("请选择银行");

                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        bankCode = bankNameList.get(which);
                        Integer bankIcon = bankIconMap.get(bankCode);
                        imgBankIcon.setImageDrawable(getResources().getDrawable(bankIcon));
                    }
                });
                bankDialog = builder.create();
                bankDialog.show();
            }
        });
        btnValicodeOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String moeny = etAmountOnline.getText().toString();
                String realName = etRealNameOnline.getText().toString();
                String cardID = etIDCardOnline.getText().toString();
                String bankNo = etBankCodeOnline.getText().toString();
//                String phoneCode =   etValicodeOnline.getText().toString();
                if (TextUtils.isEmpty(moeny)
                        || TextUtils.isEmpty(realName)
                        || TextUtils.isEmpty(cardID)
                        || TextUtils.isEmpty(bankCode)) {
                    Toast.makeText(MyRechargeActivity.this, "请填写完整所有数据", Toast.LENGTH_SHORT).show();
                    return;
                }

                getPhoneCodeOnline(moeny, bankCode, realName, cardID, bankNo);

                btnOnline.setEnabled(true);
                handler.postDelayed(runnable, 1000);
            }
        });
        btnOnline.setEnabled(false);
        btnOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String moeny = etAmountOnline.getText().toString();
                String realName = etRealNameOnline.getText().toString();
                String cardID = etIDCardOnline.getText().toString();
                String bankNo = etBankCodeOnline.getText().toString();
                String phoneCode = etValicodeOnline.getText().toString();
                if (TextUtils.isEmpty(moeny)
                        || TextUtils.isEmpty(realName)
                        || TextUtils.isEmpty(cardID)
                        || TextUtils.isEmpty(bankCode)
                        || TextUtils.isEmpty(phoneCode)) {
                    Toast.makeText(MyRechargeActivity.this, "请填写完整所有数据", Toast.LENGTH_SHORT).show();
                    return;
                }

                rechargeOnlineNew(moeny, bankCode, realName, cardID, bankNo, phoneCode);

            }
        });


//        gridViewBank = (GridView) findViewById(R.id.gridViewBank);
//        gridViewBank.setAdapter(adapter);
//        gridViewBank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                System.out.println("bankName == " + bankNameList.get(position));
//            }
//        });

//        tvRealNameOnline = (TextView) findViewById(R.id.tvRealNameOnline);
//        etValicodeOnline = (EditText) findViewById(R.id.etValicodeOnline);
//        viewValicodeOnline = (SimpleValidateCodeView) findViewById(R.id.viewValicodeOnline);
//        viewValicodeOnline.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getImgcodeOnline();
//            }
//        });
//        etAmountOnline = (EditText) findViewById(R.id.etAmountOnline);
//        btnOnline = (Button) findViewById(R.id.btnOnline);
//        btnOnline.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                String money = etAmountOnline.getText().toString();
//                String code = etValicodeOnline.getText().toString();
//
//                if (TextUtils.isEmpty(money)) {
//                    Toast.makeText(MyRechargeActivity.this, "请填写充值金额", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (TextUtils.isEmpty(code)) {
//                    Toast.makeText(MyRechargeActivity.this, getString(R.string.login_valicode_empty), Toast.LENGTH_SHORT).show();
//                    return;
//                } else {
//                    if (!valicode.equalsIgnoreCase(code)) {
//                        Toast.makeText(MyRechargeActivity.this, getString(R.string.login_valicode_not_match), Toast.LENGTH_SHORT).show();
//                        getImgcodeOnline();
//                        etValicodeOnline.getText().clear();
//                        return;
//                    }
//                }
////                rechargeOnline(money, code);
////                ApiClient.getSignatureMap();
//
//                if (money.equals("0")) {
//                    Toast.makeText(MyRechargeActivity.this, "充值金额不能为零", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//
//                long time = System.currentTimeMillis();
//                String timestamp = String.valueOf(time);
//                String signature = AppUtil.getSignature(timestamp);
//                String url = Urls.ACTION_INDEX + "?" + "actions=yepoorecharge" + "&money=" + money + "&valicode=" + code + "&signature=" + signature + "&timestamp=" + timestamp;
//
//                System.out.println("online url " + url);
//
//                UIHelper.showRechargeOnline(MyRechargeActivity.this, url);
//
//            }
//        });

    }

    private void loadData() {
        getBank();
//        getImgcodeOnline();
    }

    private void recharge(String money, String valicode, String remark) {

        ApiClient.recharge(TAG, money, valicode, remark, new ResponseListener() {
            @Override
            public void onStarted() {
                progressDialog = ProgressDialog.show(MyRechargeActivity.this, null, getString(R.string.message_submit));
            }

            @Override
            public void onResponse(Object response) {
                progressDialog.dismiss();

                BaseModel result = (BaseModel) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(MyRechargeActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (result.message.equals("sendsuccess")) {
                    Toast.makeText(MyRechargeActivity.this, "线下充值成功", Toast.LENGTH_SHORT).show();
                    MyRechargeActivity.this.finish();
                } else if (result.message.equals("moneyerror")) {
                    Toast.makeText(MyRechargeActivity.this, "金额错误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
            }
        });
    }

    private void rechargeOnline(String money, String valicode) {

        ApiClient.rechargeOnline(TAG, money, valicode, new ResponseListener() {
            @Override
            public void onStarted() {
                progressDialog = ProgressDialog.show(MyRechargeActivity.this, null, getString(R.string.message_submit));
            }

            @Override
            public void onResponse(Object response) {
                progressDialog.dismiss();

                BaseModel result = (BaseModel) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(MyRechargeActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (result.message.equals("sendsuccess")) {
                    Toast.makeText(MyRechargeActivity.this, "在线充值成功", Toast.LENGTH_SHORT).show();
                    MyRechargeActivity.this.finish();
                } else if (result.message.equals("moneyerror")) {
                    Toast.makeText(MyRechargeActivity.this, "金额错误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
            }
        });
    }

    private void getBank() {
        ApiClient.getBank(TAG, new ResponseListener() {
            @Override
            public void onStarted() {
                progressDialog = ProgressDialog.show(MyRechargeActivity.this, null, getString(R.string.message_waiting));
            }

            @Override
            public void onResponse(Object response) {
                progressDialog.dismiss();

                Bank result = (Bank) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(MyRechargeActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (result.data != null) {
                    tvRealName.setText(DataUtil.urlDecode(result.data.realname));
                    tvAccount.setText(result.data.account);

                    etRealNameOnline.setText(DataUtil.urlDecode(result.data.realname));
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
                    Toast.makeText(MyRechargeActivity.this, result.message, Toast.LENGTH_SHORT).show();
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
                viewValicode.getValidataAndSetImage(codes);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
    }


    private static List<String> bankNameList = new ArrayList<String>();
    private static Map<String, Integer> bankIconMap = new HashMap<String, Integer>();

    static {
        bankNameList.add("ICBC_D_B2C");  //工商银行
        bankNameList.add("ABC_D_B2C");  //农业银行
        bankNameList.add("CCB_D_B2C");  //建设银行
//        bankNameList.add("CMBCD_D_B2C");  //民生银行

//        bankNameList.add("BOCSH_D_B2C");  //中国银行
        bankNameList.add("CIB_D_B2C");  //兴业银行
//        bankNameList.add("CEB_D_B2C");  //光大银行
        bankNameList.add("CNCB_D_B2C");  //中信银行

        bankNameList.add("PINGAN_D_B2C");  //平安银行
//        bankNameList.add("POSTGC_D_B2C");  //中国邮政
        bankNameList.add("COMM_D_B2C");  //交通银行

        bankIconMap.put("ICBC_D_B2C", R.drawable.bg_bank_gongshang);
        bankIconMap.put("ABC_D_B2C", R.drawable.bg_bank_nongye);
        bankIconMap.put("CCB_D_B2C", R.drawable.bg_bank_jianshe);
//        bankIconMap.put("CMBCD_D_B2C", R.drawable.bg_bank_mingsheng);

//        bankIconMap.put("BOCSH_D_B2C", R.drawable.bg_bank_zhongguo);
        bankIconMap.put("CIB_D_B2C", R.drawable.bg_bank_xingye);
//        bankIconMap.put("CEB_D_B2C", R.drawable.bg_bank_guangda);
        bankIconMap.put("CNCB_D_B2C", R.drawable.bg_bank_zhongxin);

        bankIconMap.put("PINGAN_D_B2C", R.drawable.bg_bank_pingan);
//        bankIconMap.put("POSTGC_D_B2C", R.drawable.bg_bank_youzheng);
        bankIconMap.put("COMM_D_B2C", R.drawable.bg_bank_jiaotong);

    }

    private void getPhoneCodeOnline(String money, String bankCode, String realName, String cardNo, String bankNo) {
        ApiClient.getRechargePhoneCode(TAG, money, bankCode, realName, cardNo, bankNo, new ResponseListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onResponse(Object response) {

                BaseModel result = (BaseModel) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(MyRechargeActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }

                System.out.println("getPhoneCodeOnline message " + result.message);
                if (result.message.equals("get_fail")) {
                    Toast.makeText(MyRechargeActivity.this, "获取付款验证码失败，请核对填写的信息", Toast.LENGTH_SHORT).show();
                } else if (result.message.equals("no_phone")){
                    Toast.makeText(MyRechargeActivity.this, "请进行手机认证", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
    }

    private void rechargeOnlineNew(String money, String bankCode, String realName, String cardNo, String bankNo, String phoneCode) {

        ApiClient.rechargeOnlineNew(TAG, money, bankCode, realName, cardNo, bankNo, phoneCode, new ResponseListener() {
            @Override
            public void onStarted() {
                progressDialog = ProgressDialog.show(MyRechargeActivity.this, null, getString(R.string.message_submit));

            }

            @Override
            public void onResponse(Object response) {
                progressDialog.dismiss();

                BaseModel result = (BaseModel) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(MyRechargeActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }
                System.out.println("getPhoneCodeOnline message " + result.message);
                String message = result.message;
                if (message.equals("pay_fail")) {
                    Toast.makeText(MyRechargeActivity.this, "支付失败，请重新支付", Toast.LENGTH_SHORT).show();
                } else if (message.equals("pay_success")){
                    Toast.makeText(MyRechargeActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
            }
        });

    }


    private void getImgcodeOnline() {

        ApiClient.getImgcode(TAG, new ResponseListener() {
            @Override
            public void onStarted() {
            }

            @Override
            public void onResponse(Object response) {

                Valicode result = (Valicode) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(MyRechargeActivity.this, result.message, Toast.LENGTH_SHORT).show();
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
//                viewValicode.getValidataAndSetImage(codes);
//                viewValicodeOnline.getValidataAndSetImage(codes);
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
    }

    class BankAdapter extends BaseAdapter {

        private Context context;
        private List<String> bankNameList;

        public BankAdapter(Context context, List<String> bankNameList) {
            this.context = context;
            this.bankNameList = bankNameList;
        }

        @Override
        public int getCount() {
            return bankNameList.size();
        }

        @Override
        public Object getItem(int position) {
            return bankNameList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = LayoutInflater.from(context).inflate(R.layout.griditem_bank, parent, false);
            ImageView imageView = (ImageView) view.findViewById(R.id.imgBankIcon);

            String bankName = bankNameList.get(position);
            Integer bankIcon = bankIconMap.get(bankName);
            imageView.setImageDrawable(context.getResources().getDrawable(bankIcon));

            return view;
        }
    }


}


