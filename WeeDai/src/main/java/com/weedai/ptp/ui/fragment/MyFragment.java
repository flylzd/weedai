package com.weedai.ptp.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.weedai.ptp.R;
import com.weedai.ptp.utils.UIHelper;

public class MyFragment extends Fragment implements View.OnClickListener {

    private View layoutMyWealth;
    private TextView tvMyFinancialManagement;
    private TextView tvMyBankCard;
    private TextView tvMyStandInsideLetter;


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
//        loadData();
    }

    private void init(View view) {

        layoutMyWealth = view.findViewById(R.id.layoutMyWealth);
        tvMyFinancialManagement = (TextView) view.findViewById(R.id.tvMyFinancialManagement);
        tvMyBankCard = (TextView) view.findViewById(R.id.tvMyBankCard);
        tvMyStandInsideLetter = (TextView) view.findViewById(R.id.tvMyStandInsideLetter);
        layoutMyWealth.setOnClickListener(this);
        tvMyFinancialManagement.setOnClickListener(this);
        tvMyBankCard.setOnClickListener(this);
        tvMyStandInsideLetter.setOnClickListener(this);
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
}
