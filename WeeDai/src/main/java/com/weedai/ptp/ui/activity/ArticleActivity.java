package com.weedai.ptp.ui.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.weedai.ptp.constant.Config;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.constant.Urls;
import com.weedai.ptp.model.Article;
import com.weedai.ptp.model.ArticleData;
import com.weedai.ptp.model.ArticleList;
import com.weedai.ptp.model.RotationImage;
import com.weedai.ptp.model.RotationImageList;
import com.weedai.ptp.utils.DataUtil;
import com.weedai.ptp.utils.UIHelper;
import com.weedai.ptp.view.AutoScrollViewPager;
import com.weedai.ptp.volley.ResponseListener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class ArticleActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, EndOfListView.OnEndOfListListener {

    private final static String TAG = "ArticleActivity";

    private AutoScrollViewPager viewPager;
    //    private ViewPagerAdapter viewPagerAdapter;
    private LinearLayout layoutIndicator;

    //自定义轮播图的资源
//    private String[] imageUrls;
    private List<String> imageUrls = new ArrayList<String>();
    //放轮播图片的ImageView 的list
    private List<ImageView> imageViewsList = new ArrayList<ImageView>();
    //放圆点的View的list
    private List<View> dotViewsList = new ArrayList<View>();

    private RadioGroup radioGroup;

    private PMSwipeRefreshLayout pullRefreshLayout;
    private EndOfListView listView;
    private QuickAdapter<ArticleList> adapter;
    private List<ArticleList> informationList = new ArrayList<ArticleList>();
    private List<ArticleList> noticeList = new ArrayList<ArticleList>();

    private int articleType = Constant.ArticleType.INFORMATION;

    private final static int DEFAULT_PAGE = 0;
    private int infoPage = DEFAULT_PAGE;
    private int noticePage = DEFAULT_PAGE;

    private boolean isBottomLoadingCompleteInfo = false;
    private boolean isBottomLoadingCompleteNotice = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

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

    @Override
    public void onResume() {
        super.onResume();
//        viewPager.startAutoScroll();
        if (imageUrls.size() != 0) {
            System.out.println("onResume imageViewsList.size() == " + imageViewsList.size());
            showImageViewPager();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        viewPager.stopAutoScroll();
    }

    @Override
    public void onRefresh() {
        if (articleType == Constant.ArticleType.INFORMATION) {
            infoPage = DEFAULT_PAGE;
            getArticleInfo(infoPage);
        } else {
            noticePage = DEFAULT_PAGE;
            getArticleNotice(noticePage);
        }
    }

    @Override
    public void onEndOfList(Object lastItem) {

        if (articleType == Constant.ArticleType.INFORMATION) {
            if (isBottomLoadingCompleteInfo) {
                showIndeterminateProgress(false);
                return;
            }
            infoPage++;
            getArticleInfo(infoPage);
        } else {
            if (isBottomLoadingCompleteNotice) {
                showIndeterminateProgress(false);
                return;
            }
            noticePage++;
            getArticleNotice(noticePage);
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

                        synchronized (ArticleActivity.this){
                            articleType = Constant.ArticleType.INFORMATION;
                            if (!informationList.isEmpty()) {
                                adapter.replaceAll(informationList);
                            } else {
                                if (infoPage == 0) {
                                    infoPage++;
                                }
                                adapter.clear();
                                getArticleInfo(infoPage);
                            }
                        }
                        break;
                    case R.id.rbArticleNotice:
                        synchronized (ArticleActivity.this){
                            articleType = Constant.ArticleType.NOTICE;
                            if (!noticeList.isEmpty()) {
                                adapter.replaceAll(noticeList);
                            } else {
                                if (noticePage == 0) {
                                    noticePage++;
                                }
                                adapter.clear();
                                getArticleNotice(noticePage);
                            }
                        }
                        break;
                }
            }
        });

        pullRefreshLayout = (PMSwipeRefreshLayout) findViewById(R.id.pullRefreshLayout);
        pullRefreshLayout.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
//        pullRefreshLayout.setOnRefreshListener(this);
        pullRefreshLayout.setRefreshing(true);
        pullRefreshLayout.setEnabled(false);

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
                System.out.println("url " + url);
                if (!TextUtils.isEmpty(url)) {
                    url = Config.DEFAULT_IMG_URL + url;
                    ImageLoader.getInstance().displayImage(url, imageView);
                } else {
                    url = Config.DEFAULT_IMG_DOWNLOAD;
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

        viewPager = (AutoScrollViewPager) findViewById(R.id.viewPager);
        layoutIndicator = (LinearLayout) findViewById(R.id.layoutIndicator);

    }

    private void showImageViewPager() {
        int size = imageUrls.size();
        if (imageUrls == null || size == 0) {
            return;
        }

        layoutIndicator.removeAllViews();
        dotViewsList.clear();
        imageViewsList.clear();
        for (int i = 0; i < size; i++) {
            ImageView view = new ImageView(ArticleActivity.this);
            view.setTag(imageUrls.get(i));
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            imageViewsList.add(view);

            ImageView dotView = new ImageView(ArticleActivity.this);
            LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(20, 20);
            linearParams.setMargins(7, 10, 7, 10);
            dotView.setLayoutParams(linearParams);
            if (i == 0) {
                dotView.setBackgroundResource(R.drawable.dot_focused);
            } else {
                dotView.setBackgroundResource(R.drawable.dot_normal);
            }
            dotViewsList.add(dotView);
            layoutIndicator.addView(dotView);
        }
        viewPager.setFocusable(true);
        viewPager.setAdapter(new ViewPagerAdapter(imageViewsList));
        viewPager.setOnPageChangeListener(new ViewPageChangeListener());
        viewPager.startAutoScroll();
        viewPager.setInterval(2 * 1000);
    }

    private void loadData() {

        scrollPic();
//        getArticleList(infoPage);
    }

//    private void getArticleList(int page) {
//
//        ApiClient.getArticleList(TAG, page, articleType, new RefreshResponseListener() {
//
//            @Override
//            public void onResponse(Object response) {
//                super.onResponse(response);
//
//                Article result = (Article) response;
//                if (result.code != Constant.CodeResult.SUCCESS) {
//                    Toast.makeText(ArticleActivity.this, result.message, Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                List<ArticleList> articleList = result.data.list;
//                if (articleList != null && articleList.size() != 0) {
//
//                    if (articleType == Constant.ArticleType.INFORMATION) {
//                        informationList.addAll(articleList);
//                        adapter.replaceAll(informationList);
//                    } else {
//                        noticeList.addAll(articleList);
//                        adapter.replaceAll(noticeList);
//                    }
//                }
//
//                int currentPage = result.data.page;
//                int totalPage = result.data.total_page;
//                if (currentPage == totalPage || totalPage == 0) {
//                    if (articleType == Constant.ArticleType.INFORMATION) {
//                        isBottomLoadingCompleteInfo = true;
//                    } else {
//                        isBottomLoadingCompleteNotice = true;
//                    }
//                }
//            }
//        });
//    }

    private void getArticleInfo(int page) {

        ApiClient.getArticleList(TAG, page, Constant.ArticleType.INFORMATION, new RefreshResponseListener() {

            @Override
            public void onResponse(Object response) {
                super.onResponse(response);

                Article result = (Article) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(ArticleActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }
                List<ArticleList> articleList = result.data.list;
                if (articleList != null && articleList.size() != 0) {
                        informationList.addAll(articleList);
                        adapter.replaceAll(informationList);
                }

                int currentPage = result.data.page;
                int totalPage = result.data.total_page;
                if (currentPage == totalPage || totalPage == 0) {
                        isBottomLoadingCompleteInfo = true;
                }
            }
        });
    }

    private void getArticleNotice(int page) {

        ApiClient.getArticleList(TAG, page, Constant.ArticleType.NOTICE, new RefreshResponseListener() {

            @Override
            public void onResponse(Object response) {
                super.onResponse(response);

                Article result = (Article) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(ArticleActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }
                List<ArticleList> articleList = result.data.list;
                if (articleList != null && articleList.size() != 0) {
                    noticeList.addAll(articleList);
                    adapter.replaceAll(noticeList);
                }

                int currentPage = result.data.page;
                int totalPage = result.data.total_page;
                if (currentPage == totalPage || totalPage == 0) {
                        isBottomLoadingCompleteNotice = true;
                }
            }
        });
    }

    private void scrollPic() {
        ApiClient.scrollPic(TAG, new ResponseListener() {
            @Override
            public void onStarted() {
            }

            @Override
            public void onResponse(Object response) {
                RotationImage result = (RotationImage) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(ArticleActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    return;
                }

                List<RotationImageList> list = result.data.list;
                for (RotationImageList item : list) {
                    imageUrls.add(item.pic);
                }
                showImageViewPager();
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
    }

    private class ViewPagerAdapter extends PagerAdapter {

        List<ImageView> views;

        public ViewPagerAdapter(List<ImageView> views) {
            this.views = views;
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        // PagerAdapter只缓存三张要显示的图片，如果滑动的图片超出了缓存的范围，就会调用这个方法，将图片销毁
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }

        // 当要显示的图片可以进行缓存的时候，会调用这个方法进行显示图片的初始化，我们将要显示的ImageView加入到ViewGroup中，然后作为返回值返回即可
        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            ImageView imageView = views.get(position);
            String url = Urls.SERVER_URL + "/" + imageView.getTag().toString();
            ImageLoader.getInstance().displayImage(url, imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    UIHelper.showLuckyDraw(getActivity());
                    UIHelper.showHero(ArticleActivity.this);
                }
            });

            container.addView(views.get(position));
            return views.get(position);
        }
    }

    private class ViewPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            viewPager.setCurrentItem(position);
            int size = dotViewsList.size();
            for (int i = 0; i < size; i++) {
                dotViewsList.get(i).setBackgroundResource(R.drawable.dot_normal);
            }
            dotViewsList.get(position).setBackgroundResource(R.drawable.dot_focused);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private abstract class RefreshResponseListener implements ResponseListener {

        @Override
        public void onStarted() {

            showIndeterminateProgress(true);
            if (!pullRefreshLayout.isRefreshing()) {
                pullRefreshLayout.setRefreshing(true);
            }
        }

        @Override
        public void onResponse(Object response) {
            pullRefreshLayout.setRefreshing(false);
            showIndeterminateProgress(false);
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            pullRefreshLayout.setRefreshing(false);
            showIndeterminateProgress(false);
        }
    }

}
