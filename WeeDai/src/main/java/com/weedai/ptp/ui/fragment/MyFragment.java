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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.model.ArticleDetail;
import com.weedai.ptp.model.User;
import com.weedai.ptp.model.UserData;
import com.weedai.ptp.utils.UIHelper;
import com.weedai.ptp.volley.ResponseListener;

import org.w3c.dom.Text;

public class MyFragment extends Fragment implements View.OnClickListener {

    private final static String TAG = "MyFragment";

    private View layoutMyWealth;
    private TextView tvMyFinancialManagement;
    private TextView tvMyBankCard;
    private TextView tvMyStandInsideLetter;

    private ImageView imgAvatar;
    private TextView tvUsername;
    private TextView tvEmail;
    private ImageView imgUser;
    private ImageView imgPhone;
    private ImageView imgEmail;
    private ImageView imgVip;

    private Button btnMoneyRecord;
    private Button btnReturnSearch;

    private TextView tvAvailableBalance;
    private TextView tvAvailableMicroCurrency;

    private ProgressDialog progressDialog;

    private static UserData data;

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
        if (data == null) {
            loadData();
        } else {
            setUserInfo();
        }

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

        btnMoneyRecord = (Button) view.findViewById(R.id.btnMoneyRecord);
        btnReturnSearch = (Button) view.findViewById(R.id.btnReturnSearch);
        btnMoneyRecord.setOnClickListener(this);
        btnReturnSearch.setOnClickListener(this);

        layoutMyWealth = view.findViewById(R.id.layoutMyWealth);
        tvMyFinancialManagement = (TextView) view.findViewById(R.id.tvMyFinancialManagement);
        tvMyBankCard = (TextView) view.findViewById(R.id.tvMyBankCard);
        tvMyStandInsideLetter = (TextView) view.findViewById(R.id.tvMyStandInsideLetter);
        layoutMyWealth.setOnClickListener(this);
        tvMyFinancialManagement.setOnClickListener(this);
        tvMyBankCard.setOnClickListener(this);
        tvMyStandInsideLetter.setOnClickListener(this);
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
                data = result.data;
                setUserInfo();
            }
        });
    }

    private void setUserInfo() {
        if (data != null) {
            tvUsername.setText(data.username);
            tvEmail.setText(data.email);

            if (data.avatar_status == 1) {
                imgUser.setImageResource(R.drawable.icon_user_on);
            } else {
                imgUser.setImageResource(R.drawable.icon_user);
            }

            if (data.vip_status == 1) {
                imgVip.setImageResource(R.drawable.icon_vip_on);
            } else {
                imgVip.setImageResource(R.drawable.icon_vip);
            }

            if (data.phone_status) {
                imgPhone.setImageResource(R.drawable.icon_phone_on);
            } else {
                imgPhone.setImageResource(R.drawable.icon_phone);
            }

            if (data.email_status) {
                imgEmail.setImageResource(R.drawable.icon_email_on);
            } else {
                imgEmail.setImageResource(R.drawable.icon_email);
            }

            System.out.println("data.avatar_status " + data.avatar_status);
            System.out.println("data.vip_status " + data.vip_status);
            System.out.println("data.phone_status " + data.phone_status);
            System.out.println("data.email_status " + data.email_status);

            String userMoney;
            if (TextUtils.isEmpty(data.use_money)) {
                userMoney = String.format(getActivity().getString(R.string.user_available_balance_empty));
            } else {
                userMoney = String.format(getActivity().getString(R.string.user_available_balance), data.use_money);
            }
            String wb = String.format(getActivity().getString(R.string.user_available_micro_currency), data.wb);
            tvAvailableBalance.setText(userMoney);
            tvAvailableMicroCurrency.setText(wb);

            String url = data.touxiang;
            if (!TextUtils.isEmpty(url)) {
//                ImageLoader.getInstance().displayImage();
            }

        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnMoneyRecord:
                UIHelper.showMyMoneyRecord(getActivity());
                break;
            case R.id.btnReturnSearch:
                UIHelper.showMyReceivableSearch(getActivity());
                break;
            case R.id.layoutMyWealth:
                UIHelper.showMyWealth(getActivity());
                break;
            case R.id.tvMyFinancialManagement:
//                UIHelper.showMyWealth(getActivity());
                break;
            case R.id.tvMyBankCard:
                UIHelper.showMyBankCard(getActivity());
                break;
            case R.id.tvMyStandInsideLetter:
//                UIHelper.showMyWealth(getActivity());
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
