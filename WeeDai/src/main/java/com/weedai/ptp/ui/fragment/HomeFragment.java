package com.weedai.ptp.ui.fragment;


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

import com.weedai.ptp.R;
import com.weedai.ptp.utils.UIHelper;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener {


    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    private static final int INDICATOR_COUNT = 4;
    private ImageView[] indicatorImgs = new ImageView[INDICATOR_COUNT];//存放引到图片数组

    private ImageView imgYou;
    private ImageView imgInformation;
    private ImageView imgNotice;


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

        initIndicator(view);

        imgYou = (ImageView) view.findViewById(R.id.imgYou);
        imgYou.setOnClickListener(this);

        imgInformation = (ImageView) view.findViewById(R.id.imgInformation);
        imgInformation.setOnClickListener(this);

        imgNotice = (ImageView) view.findViewById(R.id.imgNotice);
        imgNotice.setOnClickListener(this);
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

            case R.id.imgInformation:
                UIHelper.showArticle(getActivity());
                break;

            case R.id.imgNotice:
                UIHelper.showArticle(getActivity());
                break;
        }
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
