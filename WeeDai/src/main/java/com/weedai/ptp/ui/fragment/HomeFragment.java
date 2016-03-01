package com.weedai.ptp.ui.fragment;


import android.app.ProgressDialog;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Config;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.constant.Urls;
import com.weedai.ptp.model.Article;
import com.weedai.ptp.model.ArticleList;
import com.weedai.ptp.model.Days;
import com.weedai.ptp.model.Invest;
import com.weedai.ptp.model.RotationImage;
import com.weedai.ptp.model.RotationImageList;
import com.weedai.ptp.model.SignIn;
import com.weedai.ptp.utils.DataUtil;
import com.weedai.ptp.utils.UIHelper;
import com.weedai.ptp.view.AutoScrollViewPager;
import com.weedai.ptp.volley.ResponseListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private final static String TAG = "HomeFragment";

    public static boolean isLoginFromHome;

    private AutoScrollViewPager viewPager;
    private LinearLayout layoutIndicator;

    //自定义轮播图的资源
//    private String[] imageUrls;
    private List<String> imageUrls = new ArrayList<String>();
    //放轮播图片的ImageView 的list
    private List<ImageView> imageViewsList = new ArrayList<ImageView>();
    //放圆点的View的list
    private List<View> dotViewsList = new ArrayList<View>();

    private ProgressDialog progressDialog;

    private GridView gridView;
    private List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    private SimpleAdapter simpleAdapter;
    // 图片封装为一个数组
    private int[] icons = {R.drawable.e1, R.drawable.e2, R.drawable.e3, R.drawable.e4,
            R.drawable.e5, R.drawable.e6, R.drawable.e7, R.drawable.e8,
            R.drawable.e13, R.drawable.e14, R.drawable.e15, R.drawable.e16};

    private String zongchengjiao;
    private String yesterdatcj;
    private String today_trade;

    private TextView tv_zcj_yi;
    private TextView tv_zcj_yi_unit;
    private TextView tv_zcj_wang;
    private TextView tv_zcj_yuan;

    private TextView tv_jrcj_wang;
    private TextView tv_jrcj_wang_unit;
    private TextView tv_jrcj_yuan;
    private TextView tv_zrcj_wang;
    private TextView tv_zrcj_wang_unit;
    private TextView tv_zrcj_yuan;

    private ImageView ivCaiWu;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        System.out.println("HomeFragment onViewCreated");
        init(view);
//        loadData();
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("HomeFragment onResume");
        if (imageUrls.size() != 0) {
            System.out.println("onResume imageViewsList.size() == " + imageViewsList.size());
            showImageViewPager();
        } else {
            scrollPic();
        }


        if (TextUtils.isEmpty(zongchengjiao)) {
            getDaysData();
        } else {
            showDaysData();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        viewPager.stopAutoScroll();
    }

    private void init(View view) {
        viewPager = (AutoScrollViewPager) view.findViewById(R.id.viewPager);
        layoutIndicator = (LinearLayout) view.findViewById(R.id.layoutIndicator);

        tv_zcj_yi = (TextView) view.findViewById(R.id.tv_zcj_yi);
        tv_zcj_yi_unit = (TextView) view.findViewById(R.id.tv_zcj_yi_unit);
        tv_zcj_wang = (TextView) view.findViewById(R.id.tv_zcj_wang);
        tv_zcj_yuan = (TextView) view.findViewById(R.id.tv_zcj_yuan);

        tv_jrcj_wang = (TextView) view.findViewById(R.id.tv_jrcj_wang);
        tv_jrcj_yuan = (TextView) view.findViewById(R.id.tv_jrcj_yuan);

        tv_zrcj_wang = (TextView) view.findViewById(R.id.tv_zrcj_wang);
        tv_zrcj_yuan = (TextView) view.findViewById(R.id.tv_zrcj_yuan);

        tv_jrcj_wang_unit = (TextView) view.findViewById(R.id.tv_jrcj_wang_unit);
        tv_zrcj_wang_unit = (TextView) view.findViewById(R.id.tv_zrcj_wang_unit);

        ivCaiWu = (ImageView) view.findViewById(R.id.ivCaiWu);
        ivCaiWu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "此功能暂未开放", Toast.LENGTH_SHORT).show();
            }
        });

        gridView = (GridView) view.findViewById(R.id.gridView);
        //新建适配器
        String[] from = {"image"};
        int[] to = {R.id.imgHome};
        simpleAdapter = new SimpleAdapter(getActivity(), getData(), R.layout.griditem_home, from, to);
        gridView.setAdapter(simpleAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        UIHelper.showLiCaiRecommend(getActivity());
                        break;
                    case 1:
                        UIHelper.showOptimizingFinancial(getActivity());
                        break;
                    case 2:
                        UIHelper.showTransfer(getActivity());
                        break;
                    case 3:
                        if (Config.isLogin) {
                            UIHelper.showPhoneRecharge(getActivity());
                        } else {
                            Toast.makeText(getActivity(), "还未登陆，不能进行话费充值", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 4:
                        UIHelper.showLuckyDraw(getActivity());
                        break;
                    case 5:
                        Toast.makeText(getActivity(), "此功能暂未开放", Toast.LENGTH_SHORT).show();
                        break;
                    case 6:
                        UIHelper.showCardeal(getActivity());
                        break;
                    case 7:
                        UIHelper.showArticle(getActivity());
                        break;
                    case 8:
                        UIHelper.showAbout(getActivity());
                        break;
                    case 9:
                        UIHelper.showCalculatorInterest(getActivity());
                        break;
                    case 10:
                        UIHelper.showArticle(getActivity());
                        break;
                    case 11:
                        if (Config.isLogin) {
                            if (Config.isSignIn) {  //成功签到
                                Toast.makeText(getActivity(), "今日已签", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            signIn();  //签到

                        } else {  // 未登录
                            HomeFragment.isLoginFromHome = true;
                            UIHelper.showLogin(getActivity());
                        }
                        break;
                }
            }
        });
    }

    public List<Map<String, Object>> getData() {
        //cion和iconName的长度是相同的，这里任选其一都可以
        dataList.clear();
        for (int i = 0; i < icons.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", icons[i]);
//            map.put("text", iconName[i]);
            dataList.add(map);
        }
        return dataList;
    }

    private void loadData() {
        scrollPic();
        getDaysData();
    }

    private void signIn() {
        ApiClient.signIn(TAG, new ResponseListener() {
            @Override
            public void onStarted() {
                progressDialog = ProgressDialog.show(getActivity(), null, "正在签到...");
            }

            @Override
            public void onResponse(Object response) {
                progressDialog.dismiss();

                SignIn result = (SignIn) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(getActivity(), result.message, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (result.message.equals("success")) {
                    Config.isSignIn = true;
//                    tvSign.setText("今日已签");
                    Toast.makeText(getActivity(), "今日签到获得奖励", Toast.LENGTH_SHORT).show();
                } else if (result.message.equals("signuped")) {
                    Config.isSignIn = true;
//                    tvSign.setText("今日已签");
                    Toast.makeText(getActivity(), "今日已签", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
            }
        });
    }

    private void getDaysData() {
        ApiClient.getDaysData(TAG, new ResponseListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onResponse(Object response) {
                Days result = (Days) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(getActivity(), result.message, Toast.LENGTH_SHORT).show();
                    return;
                }

                zongchengjiao = result.data.zongchengjiao;
                yesterdatcj = result.data.yesterdatcj;
                today_trade = result.data.today_trade;

                showDaysData();
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
    }

    private void showDaysData() {

        int zcjLength = zongchengjiao.length();
//        String[] zcjs = new String[3];
        if (zcjLength >= 9) {
//            zcjs = new String[3]
            int indexOne = (zcjLength - 9) + 1;
            tv_zcj_yi.setText(zongchengjiao.substring(0, indexOne));
            tv_zcj_wang.setText(zongchengjiao.substring(indexOne, zcjLength - 4));
            tv_zcj_yuan.setText(zongchengjiao.substring(zcjLength - 4, zcjLength));

        } else {
            tv_zcj_yi.setVisibility(View.GONE);
            tv_zcj_yi_unit.setVisibility(View.GONE);
            tv_zcj_wang.setText(zongchengjiao.substring(0, zcjLength - 4));
            tv_zcj_yuan.setText(zongchengjiao.substring(zcjLength - 4, zcjLength));
        }

        int todayLength = today_trade.length();
        if (todayLength >4) {
            tv_jrcj_wang.setText(today_trade.substring(0, todayLength - 4));
            tv_jrcj_yuan.setText(today_trade.substring(todayLength - 4, todayLength));
        } else {
            tv_jrcj_wang.setVisibility(View.GONE);
            tv_jrcj_wang_unit.setVisibility(View.GONE);
            tv_jrcj_yuan.setText(today_trade.substring(0, todayLength));
        }

        int yesLength = yesterdatcj.length();
        if (yesLength >4) {
            tv_zrcj_wang.setText(yesterdatcj.substring(0, yesLength - 4));
            tv_zrcj_yuan.setText(yesterdatcj.substring(yesLength - 4, yesLength));
        } else {
            tv_zrcj_wang.setVisibility(View.GONE);
            tv_zrcj_wang_unit.setVisibility(View.GONE);
            tv_zrcj_yuan.setText(yesterdatcj.substring(0, todayLength));
        }

    }

    private void showImageViewPager() {
        int size = imageUrls.size();
        System.out.println("showImageViewPager size ==  " + size);
        if (imageUrls == null || size == 0) {
            return;
        }

        layoutIndicator.removeAllViews();
        dotViewsList.clear();
        imageViewsList.clear();
        for (int i = 0; i < size; i++) {
            ImageView view = new ImageView(getActivity());
            view.setTag(imageUrls.get(i));
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            imageViewsList.add(view);

            ImageView dotView = new ImageView(getActivity());
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
        viewPager.setInterval(2 * 1000);
        viewPager.startAutoScroll();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
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


    private void scrollPic() {
        ApiClient.scrollPic(TAG, new ResponseListener() {
            @Override
            public void onStarted() {
            }

            @Override
            public void onResponse(Object response) {
                RotationImage result = (RotationImage) response;
                if (result.code != Constant.CodeResult.SUCCESS) {
                    Toast.makeText(getActivity(), result.message, Toast.LENGTH_SHORT).show();
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
                    UIHelper.showLuckyDraw(getActivity());
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

            System.out.println("ViewPageChangeListener position = " + position);

            viewPager.setCurrentItem(position);
            int size = dotViewsList.size();
            System.out.println("ViewPageChangeListener size = " + size);
            for (int i = 0; i < size; i++) {
                dotViewsList.get(i).setBackgroundResource(R.drawable.dot_normal);
            }
            dotViewsList.get(position).setBackgroundResource(R.drawable.dot_focused);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

}
