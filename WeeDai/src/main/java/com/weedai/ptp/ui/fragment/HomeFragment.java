package com.weedai.ptp.ui.fragment;


import android.app.ProgressDialog;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Config;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.model.Invest;
import com.weedai.ptp.model.SignIn;
import com.weedai.ptp.utils.UIHelper;
import com.weedai.ptp.volley.ResponseListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private final static String TAG = "HomeFragment";

    public static boolean isLoginFromHome;

    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    private static final int INDICATOR_COUNT = 4;
    private ImageView[] indicatorImgs = new ImageView[INDICATOR_COUNT];//存放引到图片数组

    private ImageView imgYou;
    private ImageView imgYa;
    private ImageView imgSign;
    private ImageView imgMyAccount;
    private ImageView imgInformation;
    private ImageView imgNotice;

    private RelativeLayout layoutInformation;

    private TextView tvSign;
    private TextView tvMyAccount;

    private ProgressDialog progressDialog;

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

        init(view);
//        loadData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Config.isLogin) {
            tvMyAccount.setText("已登录");
            if (Config.isSignIn) {
                tvSign.setText("今日已签");
            } else {
                tvSign.setText("今日未签");
            }
        } else {
            tvMyAccount.setText(getActivity().getString(R.string.home_my_account_right));
            tvSign.setText(getActivity().getString(R.string.home_my_account_right));
        }
    }

    private void init(View view) {
        List<View> views = new ArrayList<View>();
        ImageView imageView1 = new ImageView(getActivity());
        imageView1.setBackgroundResource(R.drawable.home_view1);
        ImageView imageView2 = new ImageView(getActivity());
        imageView2.setBackgroundResource(R.drawable.home_view2);
        ImageView imageView3 = new ImageView(getActivity());
        imageView3.setBackgroundResource(R.drawable.home_view3);
        ImageView imageView4 = new ImageView(getActivity());
        imageView4.setBackgroundResource(R.drawable.home_view4);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.showLuckyDraw(getActivity());
            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.showLuckyDraw(getActivity());
            }
        });
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.showLuckyDraw(getActivity());
            }
        });
        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.showLuckyDraw(getActivity());
            }
        });
        views.add(imageView1);
        views.add(imageView2);
        views.add(imageView3);
        views.add(imageView4);

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(views);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPageChangeListener());
        viewPager.setOffscreenPageLimit(INDICATOR_COUNT);
        viewPager.setCurrentItem(0);
        viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.showLogin(getActivity());
            }
        });

        initIndicator(view);

        imgYou = (ImageView) view.findViewById(R.id.imgYou);
        imgYa =(ImageView) view.findViewById(R.id.imgYa);
        imgSign = (ImageView) view.findViewById(R.id.imgSign);
        imgMyAccount = (ImageView) view.findViewById(R.id.imgMyAccount);
        imgInformation = (ImageView) view.findViewById(R.id.imgInformation);
        imgNotice = (ImageView) view.findViewById(R.id.imgNotice);

        imgYou.setOnClickListener(this);
        imgYa.setOnClickListener(this);
        imgSign.setOnClickListener(this);
        imgMyAccount.setOnClickListener(this);
        imgInformation.setOnClickListener(this);
        imgNotice.setOnClickListener(this);

        layoutInformation = (RelativeLayout) view.findViewById(R.id.layoutInformation);
        layoutInformation.setOnClickListener(this);

        tvSign = (TextView) view.findViewById(R.id.tvSign);
        tvMyAccount = (TextView) view.findViewById(R.id.tvMyAccount);
//        if (Config.isLogin) {
//            tvMyAccount.setText("已登录");
//            if (Config.isSignIn) {
//                tvSign.setText("今日已签");
//            } else {
//                tvSign.setText("今日未签");
//            }
//        } else {
//            tvMyAccount.setText(getActivity().getString(R.string.home_my_account_right));
//            tvSign.setText(getActivity().getString(R.string.home_my_account_right));
//        }
    }


    /**
     * 初始化引导图标
     * 动态创建多个小圆点，然后组装到线性布局里
     */
    private void initIndicator(View view) {

        View v = view.findViewById(R.id.indicator);// 线性水平布局，负责动态调整导航图标

        ImageView imageView;
        for (int i = 0; i < INDICATOR_COUNT; i++) {

            imageView = new ImageView(getActivity());
            LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(15, 15);
            linearParams.setMargins(7, 10, 7, 10);
            imageView.setLayoutParams(linearParams);

            indicatorImgs[i] = imageView;
            if (i == 0) {
                indicatorImgs[i].setBackgroundResource(R.drawable.dot_focused);
            } else {
                indicatorImgs[i].setBackgroundResource(R.drawable.dot_normal);
            }
            ((ViewGroup) v).addView(indicatorImgs[i]);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.imgYou:
                UIHelper.showOptimizingFinancial(getActivity());
                break;
            case R.id.imgYa:
                UIHelper.showLiCaiRecommend(getActivity());
                break;
            case R.id.imgSign:

                if (Config.isLogin) {
                    if (Config.isSignIn) {  //成功签到
                        return;
                    }
                    signIn();  //签到

                } else {  // 未登录
                    HomeFragment.isLoginFromHome = true;
                    UIHelper.showLogin(getActivity());
                }
                break;
            case R.id.imgMyAccount:
                if (Config.isLogin) {
                    UIHelper.showAccount(getActivity());
                } else {
                    HomeFragment.isLoginFromHome = true;
                    UIHelper.showLogin(getActivity());
                }
                break;
            case R.id.imgInformation:
                UIHelper.showArticle(getActivity());
                break;

            case R.id.imgNotice:
                UIHelper.showArticle(getActivity());
                break;
            case R.id.layoutInformation:
                UIHelper.showArticle(getActivity());
                break;
        }
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
                    tvSign.setText("今日已签");
                    Toast.makeText(getActivity(), "今日签到获得奖励", Toast.LENGTH_SHORT).show();
                } else if (result.message.equals("signuped")) {
                    Config.isSignIn = true;
                    tvSign.setText("今日已签");
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
            }
        });
    }

    private class ViewPagerAdapter extends PagerAdapter {

        List<View> views;

        public ViewPagerAdapter(List<View> views) {
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
            for (int i = 0; i < INDICATOR_COUNT; i++) {
                indicatorImgs[i].setBackgroundResource(R.drawable.dot_normal);
            }
            indicatorImgs[position].setBackgroundResource(R.drawable.dot_focused);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

}
