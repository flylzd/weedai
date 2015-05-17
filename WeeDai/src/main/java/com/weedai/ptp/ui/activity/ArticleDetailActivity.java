package com.weedai.ptp.ui.activity;


import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.model.ArticleDetail;
import com.weedai.ptp.utils.DataUtil;
import com.weedai.ptp.volley.ResponseListener;

public class ArticleDetailActivity extends BaseActivity {

    private final static String TAG = "ArticleDetailActivity";

    private WebView webView;

    private ProgressDialog progressDialog;

    private String aid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        if (getIntent().hasExtra("aid")) {
            aid = getIntent().getStringExtra("aid");
        }

        initView();
        loadData();
    }

    @Override
    protected int getActionBarTitle() {
        return R.string.title_information;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    private void initView() {

        webView = (WebView) findViewById(R.id.webView);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
    }

    private void loadData() {
        getArticleDetail();
    }

    private void getArticleDetail() {

        ApiClient.getArticleDetail(TAG, aid, new RefreshResponseListener() {
            @Override
            public void onResponse(Object response) {
                super.onResponse(response);

                ArticleDetail result = (ArticleDetail) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(ArticleDetailActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }

                String htmlString = DataUtil.urlDecode(result.data.content);
                System.out.println("name " + DataUtil.urlDecode(result.data.name));
                System.out.println("content " + DataUtil.urlDecode(result.data.content));
                // 载入这个html页面
//                webView.loadData(htmlString, "text/html", "utf-8");

                webView.loadData(getHtmlData(htmlString), "text/html; charset=utf-8", "utf-8");

//                webView.loadData(htmlString, "text/html; charset=utf-8", "utf-8");

//                webView.loadDataWithBaseURL(null, htmlString, "text/html", "utf-8", null);
            }
        });
    }

    private String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto;}</style>" +
                "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }

    private abstract class RefreshResponseListener implements ResponseListener {

        @Override
        public void onStarted() {
            progressDialog = ProgressDialog.show(ArticleDetailActivity.this, null, getString(R.string.message_waiting));


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
