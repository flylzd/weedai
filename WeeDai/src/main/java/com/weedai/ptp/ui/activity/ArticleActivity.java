package com.weedai.ptp.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.lemon.aklib.adapter.BaseAdapterHelper;
import com.lemon.aklib.adapter.QuickAdapter;
import com.lemon.aklib.widget.EndOfListView;
import com.lemon.aklib.widget.PMSwipeRefreshLayout;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.model.Article;
import com.weedai.ptp.model.ArticleData;
import com.weedai.ptp.model.ArticleList;
import com.weedai.ptp.utils.DataUtil;
import com.weedai.ptp.utils.UIHelper;
import com.weedai.ptp.volley.ResponseListener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class ArticleActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, EndOfListView.OnEndOfListListener {

    private final static String TAG = "ArticleActivity";

    private RadioGroup radioGroup;

    private PMSwipeRefreshLayout pullRefreshLayout;
    private EndOfListView listView;
    private QuickAdapter<ArticleList> adapter;
    private List<ArticleList> informationList = new ArrayList<ArticleList>();
    private List<ArticleList> noticeList = new ArrayList<ArticleList>();

    private int articleType = Constant.ArticleType.INFORMATION;

    private final static int DEFAULT_PAGE = 1;
    private int infoPage = DEFAULT_PAGE;
    private int noticePage = DEFAULT_PAGE;

    private boolean isFirstLoadingomplete = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        initView();
        loadData();
    }

    protected boolean hasBackButton() {
        return true;
    }

    @Override
    public void onRefresh() {
        if (articleType == Constant.ArticleType.INFORMATION) {
            infoPage = DEFAULT_PAGE;
            getArticleList(infoPage);
        } else {
            noticePage = DEFAULT_PAGE;
            getArticleList(noticePage);
        }
    }

    @Override
    public void onEndOfList(Object lastItem) {

        if (isFirstLoadingomplete) {
            showIndeterminateProgress(true);

            if (articleType == Constant.ArticleType.INFORMATION) {
                infoPage++;
                getArticleList(infoPage);
            } else {
                noticePage++;
                getArticleList(noticePage);
            }
        }
    }

    private void showIndeterminateProgress(boolean visibility) {
        adapter.showIndeterminateProgress(visibility);
    }

    private void initView() {

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbArticleInformation:

                        articleType = Constant.ArticleType.INFORMATION;
                        if (!informationList.isEmpty()) {
                            adapter.replaceAll(informationList);
                        } else {
                            isFirstLoadingomplete = false;
                            getArticleList(infoPage);
                        }
                        break;
                    case R.id.rbArticleNotice:
                        articleType = Constant.ArticleType.NOTICE;
                        if (!noticeList.isEmpty()) {
                            adapter.replaceAll(noticeList);
                        } else {
                            isFirstLoadingomplete = false;
                            getArticleList(noticePage);
                        }
                        break;
                }
            }
        });

        pullRefreshLayout = (PMSwipeRefreshLayout) findViewById(R.id.pullRefreshLayout);
        pullRefreshLayout.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        pullRefreshLayout.setOnRefreshListener(this);
        pullRefreshLayout.setRefreshing(true);
        listView = (EndOfListView) findViewById(R.id.listView);
        listView.setOnEndOfListListener(this);

        adapter = new QuickAdapter<ArticleList>(ArticleActivity.this, R.layout.listitem_article) {
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
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String aid;
                if (articleType == Constant.ArticleType.INFORMATION) {
                    aid = informationList.get(position).id;
                } else {
                    aid = noticeList.get(position).id;
                }
                UIHelper.showArticleDetail(ArticleActivity.this, aid);
            }
        });
    }

    private void loadData() {
        getArticleList(infoPage);
    }

    private void getArticleList(int page) {

        ApiClient.getArticleList(TAG, page, articleType, new RefreshResponseListener() {

            @Override
            public void onResponse(Object response) {
                super.onResponse(response);

                Article result = (Article) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(ArticleActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }
                List<ArticleList> articleList = result.data.list;
                if (articleList != null) {
                    switch (articleType) {
                        case Constant.ArticleType.INFORMATION:
                            if (infoPage == DEFAULT_PAGE) {
                                informationList = articleList;
                            } else {
                                informationList.addAll(articleList);
                            }
                            adapter.replaceAll(informationList);
                            break;
                        case Constant.ArticleType.NOTICE:
                            if (noticePage == DEFAULT_PAGE) {
                                noticeList = articleList;
                            } else {
                                noticeList.addAll(articleList);
                            }
                            adapter.replaceAll(noticeList);
                            break;
                    }
                }
                isFirstLoadingomplete = true;
            }
        });
    }


    private abstract class RefreshResponseListener implements ResponseListener {

        @Override
        public void onStarted() {

            if (infoPage != DEFAULT_PAGE || noticePage != DEFAULT_PAGE) {
                return;
            }
            if (!pullRefreshLayout.isRefreshing()) {
                pullRefreshLayout.setRefreshing(true);
            }
        }

        @Override
        public void onResponse(Object response) {

            if (infoPage != DEFAULT_PAGE || noticePage != DEFAULT_PAGE) {
                showIndeterminateProgress(false);
                return;
            }
            pullRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            pullRefreshLayout.setRefreshing(false);
        }
    }

}
