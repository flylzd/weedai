package com.weedai.ptp.ui.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.model.ArticleDetail;
import com.weedai.ptp.model.User;
import com.weedai.ptp.model.UserData;
import com.weedai.ptp.utils.UIHelper;
import com.weedai.ptp.volley.ResponseListener;

public class MyFragment extends Fragment implements View.OnClickListener {

    private final static String TAG = "MyFragment";

    private View layoutMyWealth;
    private TextView tvMyFinancialManagement;
    private TextView tvMyBankCard;
    private TextView tvMyStandInsideLetter;

    private TextView tvUsername;
    private TextView tvEmail;

    private ProgressDialog progressDialog;

    private UserData data;

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
        loadData();
    }

    private void init(View view) {

        tvUsername = (TextView) view.findViewById(R.id.tvUsername);
        tvEmail = (TextView) view.findViewById(R.id.tvEmail);

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
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
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
