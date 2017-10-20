package com.android.longc.testviewprger;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private ViewPager viewpager;
    private LinearLayout ll_point_group;
    private TextView tv_title;

    // ListView 的使用
    // 1.在布局文件中定义ListView
    // 2.在代码中实例化ListView
    // 3.准备数据
    // 4.设置适配器-item布局-绑定数据

    private List<ImageView> imageViews;

    /**
     * 上一次高亮显示的位置
     */
    private int prePosition = 0;
    /**
     * 是否已经滚动
     */
    private boolean isDragging = false;

    // 图片资源ID
    private final int[] imageIds = { R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e,
            R.drawable.f, };
    // 图片详情
    private final String[] imageDesrc = { "鹅", "松鼠", "人", "鹿", "鹤", "猫" };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            int item = viewpager.getCurrentItem() + 1;
            viewpager.setCurrentItem(item);

            handler.sendEmptyMessageDelayed(0, 4000);
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ViewPager 的使用

        // 1.在布局文件中定义ViewPager
        // 2.在代码中实例化ViewPager
        viewpager = (ViewPager) findViewById(R.id.viewpager);

        tv_title = (TextView) findViewById(R.id.tv_title);
        ll_point_group = (LinearLayout) findViewById(R.id.ll_point_group);

        // 3.准备数据
        imageViews = new ArrayList<ImageView>();
        for (int i = 0; i < imageIds.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(imageIds[i]);

            imageViews.add(imageView);

            // 添加点
            ImageView point = new ImageView(this);
            point.setBackgroundResource(R.drawable.point_selector);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(8, 8);
            if (i == 0) {
                point.setEnabled(true);// 当前页面为红色点
            } else {
                point.setEnabled(false);// 其他灰色点
                params.leftMargin = 8;
            }
            point.setLayoutParams(params);
            ll_point_group.addView(point);
        }

        // 4.设置适配器(PagerAdapter)-item-绑定数据

        viewpager.setAdapter(new MyPagerAdapter());

        // 设置监听ViewPager页面的改变
        viewpager.setOnPageChangeListener(new MyPageChangeListener());

        // 设置中间位置
        int item = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % imageViews.size();// 要保证imageView的整数倍
        viewpager.setCurrentItem(item);
        tv_title.setText(imageDesrc[prePosition]);

        // 发消息
        handler.sendEmptyMessageDelayed(0, 3000);



    }

    class MyPageChangeListener implements OnPageChangeListener {
        /**
         * 当页面滚动了的时候回调这个方法
         *
         * @param position
         *            当前页面的位置
         * @param positionOffset
         *            滑动页面的百分比
         * @param positionOffsetPixels
         *            在屏幕上滑动的像数
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // int realPosition = position%imageViews.size();

        }

        /**
         * 当某个页面被选中了的时候回调
         *
         * @param position
         * 被选中页面的位置
         */
        @Override
        public void onPageSelected(int position) {
            int realPosition = position % imageViews.size();

            // 设置对应页面的文本信息
            tv_title.setText(imageDesrc[realPosition]);
            // 把上一个高亮的设置默认-灰色
            ll_point_group.getChildAt(prePosition).setEnabled(false);
            // 当前的设置为高亮-红色
            ll_point_group.getChildAt(realPosition).setEnabled(true);

            prePosition = realPosition;
        }

        /**
         * 当页面滚动状态变化的时候回调这个方法 静止->滑动 滑动-->静止 静止-->拖拽
         */
        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_DRAGGING) {// 拖拽
                isDragging=true;
                handler.removeCallbacksAndMessages(null);
            } else if (state == ViewPager.SCROLL_STATE_SETTLING) {

            } else if (state == ViewPager.SCROLL_STATE_IDLE && isDragging) {//空闲与拖拽状态
                isDragging=false;
                handler.removeCallbacksAndMessages(null);
                handler.sendEmptyMessageDelayed(0, 4000);
            }

        }

    }

    class MyPagerAdapter extends PagerAdapter {
        /**
         * 得到图片总数
         */
        @Override
        public int getCount() {

            // return imageViews.size();
            return Integer.MAX_VALUE;
        }

        /**
         * 比较view与object是否是同一实例
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;

        }

        /**
         *
         * 相当于getView方法
         *
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int realPosition = position % imageViews.size();

            ImageView imageView = imageViews.get(realPosition);
            container.addView(imageView);// 添加到ViewPager中

            imageView.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:// 按下图片
                            handler.removeCallbacksAndMessages(null);
                            break;
                        case MotionEvent.ACTION_MOVE:// 在图片上移动

                            break;
                        case MotionEvent.ACTION_CANCEL://
//						handler.removeCallbacksAndMessages(null);
//						handler.sendEmptyMessageDelayed(0, 4000);
                            break;
                        case MotionEvent.ACTION_UP:// 手指离开
                            handler.sendEmptyMessageDelayed(0, 4000);
                            break;

                        default:
                            break;
                    }
                    return false;
                }
            });

            imageView.setTag(position);
            imageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int position =(Integer) v.getTag()%imageViews.size();
                    String text=imageDesrc[position];
                    Toast.makeText(MainActivity.this,"Text:"+text, Toast.LENGTH_SHORT).show();
                }
            });

            return imageView;
        }


        /**
         * 释放资源
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}

