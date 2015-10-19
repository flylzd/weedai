package com.weedai.ptp.ui.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.constant.Urls;
import com.weedai.ptp.model.User;
import com.weedai.ptp.model.UserData;
import com.weedai.ptp.utils.DataUtil;
import com.weedai.ptp.utils.UIHelper;
import com.weedai.ptp.volley.ResponseListener;

public class MyFragment extends Fragment implements View.OnClickListener {

    private final static String TAG = "MyFragment";

    private View layoutAccount;
    private View layoutMyWealth;
    private View layoutMyMicroCurrency;
    private TextView tvMyWithdrawalRecord;
    private TextView tvMyFinancialManagement;
    private TextView tvMyBankCard;
    private TextView tvMyStandInsideLetter;
    private TextView tvMyLuckyDraw;
    private TextView tvMyLuckyDrawRecord;
    private View layoutSecurityLevel;
    private TextView tvSecurityLevel;

    private ImageView imgAvatar;
    private TextView tvUsername;
    private TextView tvEmail;
    private ImageView imgUser;
    private ImageView imgPhone;
    private ImageView imgEmail;
    private ImageView imgVip;
    private LinearLayout layoutCredit;

    private Button btnMoneyRecord;
    private Button btnReturnSearch;

    private View layoutRecharge;
    private View layoutWithdrawal;

    private TextView tvAvailableBalance;
    private TextView tvAvailableMicroCurrency;

    private ProgressDialog progressDialog;

    private static UserData data;

    private String securityLevel;
    private int level = 0;

    public static MyFragment newInstance() {
        MyFragment fragment = new MyFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
//        if (data == null) {
//            loadData();
//        } else {
//            setUserInfo();
//        }

    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void init(View view) {

        imgAvatar = (ImageView) view.findViewById(R.id.imgAvatar);
        tvUsername = (TextView) view.findViewById(R.id.tvUsername);
        tvEmail = (TextView) view.findViewById(R.id.tvEmail);
        imgUser = (ImageView) view.findViewById(R.id.imgUser);
        imgPhone = (ImageView) view.findViewById(R.id.imgPhone);
        imgEmail = (ImageView) view.findViewById(R.id.imgEmail);
        imgVip = (ImageView) view.findViewById(R.id.imgVip);
        tvAvailableBalance = (TextView) view.findViewById(R.id.tvAvailableBalance);
        tvAvailableMicroCurrency = (TextView) view.findViewById(R.id.tvAvailableMicroCurrency);
        layoutCredit = (LinearLayout) view.findViewById(R.id.layoutCredit);

        btnMoneyRecord = (Button) view.findViewById(R.id.btnMoneyRecord);
        btnReturnSearch = (Button) view.findViewById(R.id.btnReturnSearch);
        btnMoneyRecord.setOnClickListener(this);
        btnReturnSearch.setOnClickListener(this);

        layoutAccount = view.findViewById(R.id.layoutAccount);
        layoutMyWealth = view.findViewById(R.id.layoutMyWealth);
        tvMyWithdrawalRecord = (TextView) view.findViewById(R.id.tvMyWithdrawalRecord);
        layoutMyMicroCurrency = view.findViewById(R.id.layoutMyMicroCurrency);
        tvMyFinancialManagement = (TextView) view.findViewById(R.id.tvMyFinancialManagement);
        tvMyBankCard = (TextView) view.findViewById(R.id.tvMyBankCard);
        tvMyStandInsideLetter = (TextView) view.findViewById(R.id.tvMyStandInsideLetter);
        tvMyLuckyDraw = (TextView) view.findViewById(R.id.tvMyLuckyDraw);
        tvMyLuckyDrawRecord = (TextView) view.findViewById(R.id.tvMyLuckyDrawRecord);
        tvSecurityLevel = (TextView) view.findViewById(R.id.tvSecurityLevel);
        layoutSecurityLevel = view.findViewById(R.id.layoutSecurityLevel);

        layoutAccount.setOnClickListener(this);
        layoutMyWealth.setOnClickListener(this);
        tvMyWithdrawalRecord.setOnClickListener(this);
        layoutMyMicroCurrency.setOnClickListener(this);
        tvMyFinancialManagement.setOnClickListener(this);
        tvMyBankCard.setOnClickListener(this);
        tvMyStandInsideLetter.setOnClickListener(this);
        tvMyLuckyDraw.setOnClickListener(this);
        tvMyLuckyDrawRecord.setOnClickListener(this);
        layoutSecurityLevel.setOnClickListener(this);

        layoutRecharge = view.findViewById(R.id.layoutRecharge);
        layoutWithdrawal = view.findViewById(R.id.layoutWithdrawal);
        layoutRecharge.setOnClickListener(this);
        layoutWithdrawal.setOnClickListener(this);
    }

    private void loadData() {
        getUser();
    }

    private void getUser() {
        ApiClient.getUser(TAG, new RefreshResponseListener() {
            @Override
            public void onResponse(Object response) {
                super.onResponse(response);

                User result = (User) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(getActivity(), result.message, Toast.LENGTH_SHORT).show();
                    return;
                }
                User.userInfo = result.data;
                data = User.userInfo;
                setUserInfo();

                getAvatars();  //获取头像
            }
        });
    }

    private void getAvatars() {
        ApiClient.getAvatars(TAG, new RefreshResponseListener() {
            @Override
            public void onResponse(Object response) {
                super.onResponse(response);

                User result = (User) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(getActivity(), result.message, Toast.LENGTH_SHORT).show();
                    return;
                }

                User.userInfo.touxiang = DataUtil.urlDecode(data.touxiang);
                String url = DataUtil.urlDecode(data.touxiang);
                if (!TextUtils.isEmpty(url)) {
                    url = Urls.SERVER_URL + url;
                    ImageLoader.getInstance().displayImage(url, imgAvatar);
                }
            }
        });
    }

    private void setUserInfo() {
        if (data != null) {
            tvUsername.setText(DataUtil.urlDecode(data.username));
            tvEmail.setText(DataUtil.urlDecode(data.email));

            level = 0;
            if (data.real_status == 1) {
                imgUser.setImageResource(R.drawable.icon_user_on);
                level++;
            } else {
                imgUser.setImageResource(R.drawable.icon_user);
            }
            if (data.phone_status == 1) {
                imgPhone.setImageResource(R.drawable.icon_phone_on);
                level++;
            } else {
                imgPhone.setImageResource(R.drawable.icon_phone);
            }
            if (data.email_status == 1) {
                imgEmail.setImageResource(R.drawable.icon_email_on);
                level++;
            } else {
                imgEmail.setImageResource(R.drawable.icon_email);
            }
            if (data.vip_status == 1) {
                imgVip.setImageResource(R.drawable.icon_vip_on);
                level++;
            } else {
                imgVip.setImageResource(R.drawable.icon_vip);
            }

            System.out.println("level == " + level);
            if (level == 0) {
                securityLevel = "低";
            } else if (level == 1) {
                securityLevel = "低";
            } else if (level == 2) {
                securityLevel = "中";
            } else if (level >= 3) {
                securityLevel = "高";
            }
            tvSecurityLevel.setText(securityLevel);

            System.out.println("data.avatar_status " + data.real_status);
            System.out.println("data.vip_status " + data.vip_status);
            System.out.println("data.phone_status " + data.phone_status);
            System.out.println("data.email_status " + data.email_status);

            String userMoney;
            if (TextUtils.isEmpty(data.use_money)) {
                userMoney = String.format(getActivity().getString(R.string.user_available_balance_empty));
            } else {
                userMoney = String.format(getActivity().getString(R.string.user_available_balance), data.use_money);
            }
            String wb;
            if (TextUtils.isEmpty(data.wb)) {
                wb = String.format(getActivity().getString(R.string.user_available_micro_currency), 0);
            } else {
                wb = String.format(getActivity().getString(R.string.user_available_micro_currency), data.wb);
            }
            tvAvailableBalance.setText(userMoney);
            tvAvailableMicroCurrency.setText(wb);

//            String url = data.touxiang;
//            if (!TextUtils.isEmpty(url)) {
//                url = Urls.SERVER_URL + url;
//                ImageLoader.getInstance().displayImage(url, imgAvatar);
//            }

            int credit = data.credit;
            System.out.println("credit : " + credit);
            int crownNum = credit / 1000; // 皇冠的数量
            int starsNum = (credit - crownNum * 1000) / 200;  //星星的数量

            layoutCredit.removeAllViews();
            for (int i = 0; i < crownNum; i++) {
                ImageView imageView = new ImageView(getActivity());
                imageView.setImageResource(R.drawable.icon_crown);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(30, 30);
                params.setMargins(4, 0, 0, 0);
//                imageView.setLayoutParams(params);
                layoutCredit.addView(imageView, params);
            }

            for (int i = 0; i < starsNum; i++) {
                ImageView imageView = new ImageView(getActivity());
                imageView.setImageResource(R.drawable.icon_star);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(30, 30);
                params.setMargins(4, 0, 0, 0);
                imageView.setLayoutParams(params);
                layoutCredit.addView(imageView);
            }

        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.layoutAccount:
                UIHelper.showAccountAvatars(getActivity());
                break;
            case R.id.btnMoneyRecord:
                UIHelper.showMyMoneyRecord(getActivity());
                break;
            case R.id.btnReturnSearch:
                UIHelper.showMyReceivableSearch(getActivity());
                break;
            case R.id.layoutMyWealth:
                UIHelper.showMyWealth(getActivity(), data.use_money, data.wb);
                break;
            case R.id.tvMyWithdrawalRecord:
                UIHelper.showMyWithdrawalRecord(getActivity());
                break;
            case R.id.layoutMyMicroCurrency:
                UIHelper.showMyMicroCurrencyHistory(getActivity(), data.wb);
                break;
            case R.id.tvMyFinancialManagement:
                UIHelper.showMyFinancialManagemen(getActivity());
                break;
            case R.id.tvMyBankCard:
                UIHelper.showMyBankCard(getActivity());
                break;
            case R.id.tvMyStandInsideLetter:
                UIHelper.showMyStandInsideLetter(getActivity());
                break;
            case R.id.tvMyLuckyDraw:
                UIHelper.showLuckyDraw(getActivity());
//                Toast.makeText(getActivity(), "新活动即将开始，敬请期待", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tvMyLuckyDrawRecord:
                UIHelper.showLuckyDrawRecord(getActivity());
//                Toast.makeText(getActivity(), "新活动即将开始，敬请期待", Toast.LENGTH_SHORT).show();
                break;
            case R.id.layoutSecurityLevel:
                UIHelper.showSecurityLevel(getActivity());
                break;
            case R.id.layoutRecharge:
                UIHelper.showMyRecharge(getActivity());
//                Toast.makeText(MyWealthActivity.this, "此功能暂不开放", Toast.LENGTH_SHORT).show();
                break;
            case R.id.layoutWithdrawal:
                UIHelper.showMyWithdrawal(getActivity());
                break;
        }
    }

    private abstract class RefreshResponseListener implements ResponseListener {

        @Override
        public void onStarted() {
            progressDialog = ProgressDialog.show(getActivity(), null, getString(R.string.message_waiting));
        }

        @Override
        public void onResponse(Object response) {
            progressDialog.dismiss();
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            progressDialog.dismiss();
        }
    }
}
