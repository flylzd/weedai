package com.weedai.ptp.ui.fragment;


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

        layoutAbout = (RelativeLayout) view.findViewById(R.id.layoutAbout);
        layoutAbout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layoutAbout:
                UIHelper.showAbout(getActivity());
                break;
        }
    }
}
