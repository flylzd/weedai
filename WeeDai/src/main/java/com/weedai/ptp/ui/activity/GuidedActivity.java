package com.weedai.ptp.ui.activity;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.weedai.ptp.R;
import com.weedai.ptp.utils.UIHelper;
import com.weedai.ptp.view.AutoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

public class GuidedActivity extends Activity {

    private AutoScrollViewPager viewPager;
    private LinearLayout layoutIndicator;

    private int GUIDEED_CONUT = 4;
    //放轮播图片的ImageView 的list
    private List<View> imageViewsList = new ArrayList<View>();
    //放圆点的View的list
    private List<View> dotViewsList = new ArrayList<View>();

    private static final String SHAREDPREFERENCES_NAME = "first_pref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guided);

        initView();
    }

    private void initView() {

        viewPager = (AutoScrollViewPager) findViewById(R.id.viewPager);
        layoutIndicator = (LinearLayout) findViewById(R.id.layoutIndicator);

        showImageViewPager();

    }

    private void showImageViewPager() {

        View view1 = getLayoutInflater().inflate(R.layout.view_guided_1, null);
        View view2 = getLayoutInflater().inflate(R.layout.view_guided_2, null);
        View view3 = getLayoutInflater().inflate(R.layout.view_guided_3, null);
        View view4 = getLayoutInflater().inflate(R.layout.view_guided_4, null);
        imageViewsList.add(view1);
        imageViewsList.add(view2);
        imageViewsList.add(view3);
        imageViewsList.add(view4);

        Button btnStart = (Button) view4.findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setGuided();

                UIHelper.showMain(GuidedActivity.this);
                GuidedActivity.this.finish();
            }
        });

        for (int i = 0; i < GUIDEED_CONUT; i++) {

            ImageView dotView = new ImageView(GuidedActivity.this);
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
//        viewPager.startAutoScroll();
//        viewPager.setInterval(2 * 1000);
    }

    /**
     * method desc：设置已经引导过了，下次启动不用再次引导
     */
    private void setGuided() {
        SharedPreferences preferences = getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        // 存入数据
        editor.putBoolean("isFirstIn", false);
        // 提交修改
        editor.commit();
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
}
