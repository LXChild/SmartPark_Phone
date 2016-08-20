package com.lxchild.www.guide;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxchild.www.smartpark.MainActivity;
import com.lxchild.www.smartpark.R;

import java.util.ArrayList;

/**
 * Created by LXChild on 2015/4/27.
 */
public class GuideActivity extends Activity {

    private ViewPager vp_guide;
    /**
     * 装分页显示的view数组
     */
    private ArrayList<View> pageViews;
    private ImageView iv_point;

    /**
     * 将小圆点的图片用数组表示
     */
    private ImageView[] iv_points;
    /**
     * 包裹滑动图片的LinearLayout
     */
    private ViewGroup viewPics;
    /**
     * 包裹小圆点的LinearLayout
     */
    private ViewGroup viewPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    private void initData() {
    }

    private void initView() {
        //将要分页显示的View装入数组中
        LayoutInflater inflater = getLayoutInflater();
        pageViews = new ArrayList<>();
        pageViews.add(inflater.inflate(R.layout.guide_page1, null));
        pageViews.add(inflater.inflate(R.layout.guide_page2, null));
        pageViews.add(inflater.inflate(R.layout.guide_page3, null));

        //创建imageviews数组，大小是要显示的图片的数量
        iv_points = new ImageView[pageViews.size()];

        //从指定的XML文件加载视图
        viewPics = (ViewGroup) inflater.inflate(R.layout.activity_guide, null);

        //实例化小圆点的linearLayout和viewpager
        viewPoints = (ViewGroup) viewPics.findViewById(R.id.ll_vg);
        vp_guide = (ViewPager) viewPics.findViewById(R.id.vp_guide);

        //添加小圆点的图片
        for (int i = 0; i < pageViews.size(); i++) {
            iv_point = new ImageView(GuideActivity.this);
            //设置小圆点imageview的参数
            iv_point.setLayoutParams(new ViewGroup.LayoutParams(15, 15));//创建一个宽高均为15 的布局
            iv_point.setPadding(10, 0, 10, 0);
            //将小圆点layout添加到数组中
            iv_points[i] = iv_point;

            //默认选中的是第一张图片，此时第一个小圆点是选中状态，其他不是
            if (i == 0) {
                iv_points[i].setBackgroundResource(R.drawable.page_indicator_focused);
            } else {
                iv_points[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
            }

            //将imageviews添加到小圆点视图组
            viewPoints.addView(iv_points[i]);
        }
        //显示滑动图片的视图
        setContentView(viewPics);

        //设置viewpager的适配器和监听事件
        vp_guide.setAdapter(new GuidePageAdapter());
        vp_guide.setOnPageChangeListener(new GuidePageChangeListener());
    }

    private Button.OnClickListener Button_OnClickListener = new Button.OnClickListener() {
        public void onClick(View v) {
            //设置已经引导
            setGuided();

            //跳转
            Intent mIntent = new Intent();
            mIntent.setClass(GuideActivity.this, MainActivity.class);
            GuideActivity.this.startActivity(mIntent);
            GuideActivity.this.finish();
        }
    };

    private static final String PREFS_NAME = "Guide";
    private static final String KEY_GUIDE = "isFirstEnter";
    /**设置已启动引导界面*/
    private void setGuided() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(KEY_GUIDE, false);
        editor.apply();
    }

    class GuidePageAdapter extends PagerAdapter {

        //获取当前窗体界面数
        @Override
        public int getCount() {
            return pageViews.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(pageViews.get(position));

            // 测试页卡1内的按钮事件
            if (position == 2) {
//                Button btn = (Button) container.findViewById(R.id.btn_guide_enter);
//                btn.setOnClickListener(Button_OnClickListener);
                TextView lastTv = (TextView) container.findViewById(R.id.lastTv);
                lastTv.setOnClickListener(Button_OnClickListener);
            }

            return pageViews.get(position);
        }

        // 判断是否由对象生成界面
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        //销毁position位置的界面
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(pageViews.get(position));
        }

    }

    class GuidePageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int position) {
            // TODO Auto-generated method stub
            for (int i = 0; i < iv_points.length; i++) {
                iv_points[position].setBackgroundResource(R.drawable.page_indicator_focused);
                //不是当前选中的page，其小圆点设置为未选中的状态
                if (position != i) {
                    iv_points[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
                }
            }

        }
    }
}
