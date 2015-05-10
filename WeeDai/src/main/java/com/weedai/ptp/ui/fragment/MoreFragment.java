package com.weedai.ptp.ui.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.weedai.ptp.R;
import com.weedai.ptp.utils.UIHelper;

public class MoreFragment extends Fragment implements View.OnClickListener {


    private RelativeLayout layoutAbout;
    private RelativeLayout layoutWebsite;
    private RelativeLayout layoutPhone;

    private View layoutCalculatorInterest;
    private View layoutCalculatorNetCredit;


    public static MoreFragment newInstance() {
        MoreFragment fragment = new MoreFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_more, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);
//        loadData();
    }

    private void init(View view) {

        layoutCalculatorInterest = view.findViewById(R.id.layoutCalculatorInterest);
        layoutCalculatorNetCredit = view.findViewById(R.id.layoutCalculatorNetCredit);
        layoutAbout = (RelativeLayout) view.findViewById(R.id.layoutAbout);
        layoutWebsite = (RelativeLayout) view.findViewById(R.id.layoutWebsite);
        layoutPhone = (RelativeLayout) view.findViewById(R.id.layoutPhone);
        layoutCalculatorInterest.setOnClickListener(this);
        layoutCalculatorNetCredit.setOnClickListener(this);
        layoutAbout.setOnClickListener(this);
        layoutWebsite.setOnClickListener(this);
        layoutPhone.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layoutCalculatorInterest:
                UIHelper.showCalculatorInterest(getActivity());
                break;
            case R.id.layoutCalculatorNetCredit:
                UIHelper.showCalculatorNetCredit(getActivity());
                break;
            case R.id.layoutAbout:
                UIHelper.showAbout(getActivity());
                break;
            case R.id.layoutWebsite: {
                String url = "http://www.weedai.com"; // web address
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
            break;
            case R.id.layoutPhone: {
                Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:4000881609"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            break;
        }
    }
}
