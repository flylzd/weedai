package com.weedai.ptp.ui.activity;


import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.lemon.aklib.adapter.BaseAdapterHelper;
import com.lemon.aklib.adapter.QuickAdapter;
import com.lemon.aklib.widget.EndOfListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.model.Article;
import com.weedai.ptp.model.ArticleDetail;
import com.weedai.ptp.model.ArticleList;
import com.weedai.ptp.model.ArticleRelated;
import com.weedai.ptp.utils.DataUtil;
import com.weedai.ptp.utils.ListViewUtil;
import com.weedai.ptp.utils.UIHelper;
import com.weedai.ptp.volley.ResponseListener;

import java.util.ArrayList;
import java.util.List;

public class ArticleDetailActivity extends BaseActivity {

    private final static String TAG = "ArticleDetailActivity";

    private WebView webView;

    private ListView listView;
    private QuickAdapter<ArticleList> adapter;
    private List<ArticleList> newsList = new ArrayList<ArticleList>();

    private ProgressDialog progressDialog;

    private String aid;

    private String siteId;

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

        adapter = new QuickAdapter<ArticleList>(ArticleDetailActivity.this, R.layout.listitem_article) {
            @Override
            protected void convert(BaseAdapterHelper helper, ArticleList item) {

                helper.setText(R.id.tvArticleTitle, DataUtil.urlDecode(item.name));
                helper.setText(R.id.tvArticleSubTitle, DataUtil.urlDecode(item.summary));
                helper.setText(R.id.tvArticleDate, item.publish);
                helper.setText(R.id.tvComments, item.comment);

                ImageView imageView = helper.getView(R.id.imgArticle);
                String url = item.litpic;
                if (!TextUtils.isEmpty(url)) {
                    ImageLoader.getInstance().displayImage(url, imageView);
                }
            }
        };
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UIHelper.showArticleDetail(ArticleDetailActivity.this, newsList.get(position).id);
            }
        });

    }

    private void loadData() {
        getArticleDetail();
//        getRelatedArticleList();
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

                siteId = result.data.site_id;
                getRelatedArticleList();
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


    private void getRelatedArticleList() {
        ApiClient.getRelatedArticleList(TAG, siteId, new RefreshResponseListener() {
            @Override
            public void onResponse(Object response) {
                super.onResponse(response);

                ArticleRelated result = (ArticleRelated) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(ArticleDetailActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }
                List<ArticleList> articleList = result.data;
                newsList = articleList;
                adapter.replaceAll(newsList);

                ListViewUtil.setListViewHeightBasedOnChildren(listView);
            }
        });
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
