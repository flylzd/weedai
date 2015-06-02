package com.weedai.ptp.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;


import com.android.volley.error.VolleyError;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Config;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.model.About;
import com.weedai.ptp.model.Company;
import com.weedai.ptp.model.CompanyData;
import com.weedai.ptp.utils.DataUtil;
import com.weedai.ptp.volley.ResponseListener;

import java.util.ArrayList;
import java.util.List;

public class PartnerActivity extends BaseActivity {

    private final static String TAG = "PartnerActivity";

    private WebView webView;

    private ProgressDialog progressDialog;

    private ListView listView;
    private QuickAdapter<CompanyData> adapter;
    private List<CompanyData> dataList = new ArrayList<CompanyData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner);

        initView();
        loadData();
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_partner;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    private void initView() {

        adapter = new QuickAdapter<CompanyData>(PartnerActivity.this, R.layout.listitem_partner) {
            @Override
            protected void convert(BaseAdapterHelper helper, CompanyData item) {

                helper.setText(R.id.tvCompanyName, DataUtil.urlDecode(item.name));

                ImageView imageView = helper.getView(R.id.imgCompany);
                String url = item.images;
                System.out.println("url " + url);
                if (!TextUtils.isEmpty(url)) {
                    ImageLoader.getInstance().displayImage(url, imageView);
                } else {
                    url = Config.DEFAULT_IMG_DOWNLOAD;
                    ImageLoader.getInstance().displayImage(url, imageView);
                }
            }
        };

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String url = DataUtil.urlDecode(adapter.getItem(position).url); // web address
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
    }

    private void loadData() {
        getPartner();
    }

    private void getPartner() {

        ApiClient.getPartner(TAG, new ResponseListener() {
            @Override
            public void onStarted() {
                progressDialog = ProgressDialog.show(PartnerActivity.this, null, getString(R.string.message_waiting));
            }

            @Override
            public void onResponse(Object response) {
                progressDialog.dismiss();

                Company result = (Company) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(PartnerActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (result.message.equals("friends_list_succ")) {

                    adapter.replaceAll(result.data);

                } else {
                    Toast.makeText(PartnerActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
            }
        });

    }
}


