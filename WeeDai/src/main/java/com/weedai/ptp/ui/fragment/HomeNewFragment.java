package com.weedai.ptp.ui.fragment;


import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.android.volley.error.VolleyError;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.weedai.ptp.R;
import com.weedai.ptp.app.ApiClient;
import com.weedai.ptp.constant.Config;
import com.weedai.ptp.constant.Constant;
import com.weedai.ptp.constant.Urls;
import com.weedai.ptp.model.ArticleList;
import com.weedai.ptp.model.RotationImage;
import com.weedai.ptp.model.RotationImageList;
import com.weedai.ptp.model.SignIn;
import com.weedai.ptp.utils.UIHelper;
import com.weedai.ptp.view.AutoScrollViewPager;
import com.weedai.ptp.volley.ResponseListener;

import java.util.ArrayList;
import java.util.List;

public class HomeNewFragment extends Fragment implements View.OnClickListener {

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

    private View layoutHomeLiCai;
    private View layoutHomeXiangMu;
    private View layoutHomeZhaiQuan;
    private View layoutHomeGeRen;

    private View layoutHomeVideo;

    private View layoutCalculatorInterest;
    private View layoutCalculatorNetCredit;

    private View layoutInformation;


    private ProgressDialog progressDialog;

    private ArticleList item;

    public static HomeNewFragment newInstance() {
        HomeNewFragment fragment = new HomeNewFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_new, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        System.out.println("HomeFragment onViewCreated");
        init(view);

        if (item != null && imageUrls.size() != 0) {
//            setArticle();
        } else {
//            loadData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("HomeFragment onResume");
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
//        viewPager.startAutoScroll();

        if (imageUrls.size() != 0) {
            System.out.println("onResume imageViewsList.size() == " + imageViewsList.size());
            showImageViewPager();
        } else {
            scrollPic();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        viewPager.stopAutoScroll();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.layoutHomeLiCai:
                UIHelper.showOptimizingFinancial(getActivity());
                break;
            case R.id.layoutHomeXiangMu:
                UIHelper.showLiCaiRecommend(getActivity());
                break;
            case R.id.layoutHomeZhaiQuan:
                break;
            case R.id.layoutHomeGeRen:
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
            case R.id.layoutHomeVideo:
                UIHelper.showHomeVideo(getActivity());
                break;
            case R.id.layoutCalculatorInterest:
                UIHelper.showCalculatorInterest(getActivity());
                break;
            case R.id.layoutCalculatorNetCredit:
                UIHelper.showCalculatorNetCredit(getActivity());
                break;
        }
    }

    private void init(View view) {

        viewPager = (AutoScrollViewPager) view.findViewById(R.id.viewPager);
        layoutIndicator = (LinearLayout) view.findViewById(R.id.layoutIndicator);

        layoutHomeLiCai = view.findViewById(R.id.layoutHomeLiCai);
        layoutHomeXiangMu = view.findViewById(R.id.layoutHomeXiangMu);
        layoutHomeZhaiQuan = view.findViewById(R.id.layoutHomeZhaiQuan);
        layoutHomeGeRen = view.findViewById(R.id.layoutHomeGeRen);

        layoutHomeVideo = view.findViewById(R.id.layoutHomeVideo);

        layoutCalculatorInterest = view.findViewById(R.id.layoutCalculatorInterest);
        layoutCalculatorNetCredit = view.findViewById(R.id.layoutCalculatorNetCredit);

        layoutInformation = view.findViewById(R.id.layoutCalculatorNetCredit);

        layoutHomeLiCai.setOnClickListener(this);
        layoutHomeXiangMu.setOnClickListener(this);
        layoutHomeZhaiQuan.setOnClickListener(this);
        layoutHomeGeRen.setOnClickListener(this);

        layoutHomeVideo.setOnClickListener(this);

        layoutCalculatorInterest.setOnClickListener(this);
        layoutCalculatorNetCredit.setOnClickListener(this);

        layoutInformation.setOnClickListener(this);
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
