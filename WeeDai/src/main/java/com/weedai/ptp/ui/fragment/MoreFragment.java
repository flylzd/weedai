package com.weedai.ptp.ui.fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Config;
import com.weedai.ptp.model.AppVersion;
import com.weedai.ptp.utils.UIHelper;
import com.weedai.ptp.volley.ResponseListener;

public class MoreFragment extends Fragment implements View.OnClickListener {

    private final static String TAG = "MoreFragment";

    private RelativeLayout layoutAbout;
    private RelativeLayout layoutWebsite;
    private RelativeLayout layoutPhone;
    private RelativeLayout layoutVersion;
    private View layoutAccountSetting;
    private TextView tvVersionCode;

    private View layoutCalculatorInterest;
    private View layoutCalculatorNetCredit;

    private ProgressDialog progressDialog;

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
        layoutVersion = (RelativeLayout) view.findViewById(R.id.layoutVersion);
        layoutAccountSetting = view.findViewById(R.id.layoutAccountSetting);
        tvVersionCode = (TextView) view.findViewById(R.id.tvVersionCode);
        layoutVersion.setOnClickListener(this);
        layoutCalculatorInterest.setOnClickListener(this);
        layoutCalculatorNetCredit.setOnClickListener(this);
        layoutAbout.setOnClickListener(this);
        layoutWebsite.setOnClickListener(this);
        layoutPhone.setOnClickListener(this);
        layoutAccountSetting.setOnClickListener(this);


        try {
            // 获取packagemanager的实例
            PackageManager packageManager = getActivity().getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(getActivity().getPackageName(), 0);
            tvVersionCode.setText(packInfo.versionName);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layoutVersion:
//                UIHelper.showCalculatorInterest(getActivity());
                getAppVersion();
                break;
            case R.id.layoutCalculatorInterest:
                UIHelper.showCalculatorInterest(getActivity());
                break;
            case R.id.layoutCalculatorNetCredit:
                UIHelper.showCalculatorNetCredit(getActivity());
                break;
            case R.id.layoutAbout:
                UIHelper.showAbout(getActivity());
                break;
            case R.id.layoutAccountSetting:
                if (!Config.isLogin){
                    UIHelper.showLogin(getActivity());
                    return;
                }
                UIHelper.showAccount(getActivity());
                break;
            case R.id.layoutWebsite: {
                String url = "http://www.weedai.com"; // web address
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
            break;
            case R.id.layoutPhone: {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:4000881609"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            break;
        }
    }


    private void getAppVersion() {

        ApiClient.getAppVersion(TAG, new ResponseListener() {
            @Override
            public void onStarted() {
                progressDialog = ProgressDialog.show(getActivity(), null, "正在检查新版本...");
            }

            @Override
            public void onResponse(Object response) {

                progressDialog.dismiss();
                final AppVersion appVersion = (AppVersion) response;

                try {
                    // 获取packagemanager的实例
                    PackageManager packageManager = getActivity().getPackageManager();
                    // getPackageName()是你当前类的包名，0代表是获取版本信息
                    PackageInfo packInfo = packageManager.getPackageInfo(getActivity().getPackageName(), 0);
                    String version = String.valueOf(packInfo.versionCode);

                    System.out.println("native version " + version);
                    System.out.println("remote version " + appVersion.version);

                    if (version.compareTo(appVersion.version) < 0) { //更新

                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("更新");
                        builder.setMessage("有新的版本可以更新");
                        builder.setNegativeButton("以后再说", null);
                        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String url = appVersion.installUrl; // web address
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(url));
                                startActivity(intent);
                            }
                        });
                        builder.create();
                        builder.show();
                    } else {
                        Toast.makeText(getActivity(), "此版本是最新版本", Toast.LENGTH_SHORT).show();
                    }

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
            }
        });
    }
}
