package com.zero.learningviewpager;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private ArrayList<ImageView> imageViewList;
    private String[] contentDescs;
    private TextView tv_word;
    private LinearLayout llPoints_container;
    private int previousSelectedPosition;
    private boolean isRunning=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //view
        initView();
        //model
        initData();

        //control
        initMadapter();

        // 开启轮询
        new Thread(){
            public void run() {
                isRunning = true;
                while(isRunning){
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 往下跳一位
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            System.out.println("设置当前位置: " + mViewPager.getCurrentItem());
                            mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
                        }
                    });
                }
            };
        }.start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }

    private void initView() {
        mViewPager= (ViewPager) findViewById(R.id.mviewPager);
        tv_word = (TextView) findViewById(R.id.tv_word);
        llPoints_container= (LinearLayout) findViewById(R.id.ll_points_container);
        mViewPager.setOnPageChangeListener(this);

    }

    private void initData() {
        //图片资源ID
        int[] imageResource={R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d,R.drawable.e};

        // 文本描述
        contentDescs = new String[]{
                "巩俐不低俗，我就不能低俗",
                "扑树又回来啦！再唱经典老歌引万人大合唱",
                "揭秘北京电影如何升级",
                "乐视网TV版大派送",
                "热血屌丝的反杀"
        };

        //初始化需要显示图片数据
        ImageView mImageView;
        View selectPoint;

        imageViewList=new ArrayList<>();
        for (int i=0;i<imageResource.length;i++){
            mImageView=new ImageView(this);
            mImageView.setImageResource(imageResource[i]);
            imageViewList.add(mImageView);

            //添加指示器
            selectPoint=new View(this);
            selectPoint.setBackgroundResource(R.drawable.select_bg_point);
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(5,5);
            if (i!=0)
            params.leftMargin=10;//添加指示器之间的间隔

            selectPoint.setEnabled(false);
            llPoints_container.addView(selectPoint,params);

        }





    }

    private void initMadapter() {
        llPoints_container.getChildAt(0).setEnabled(true);
        tv_word.setText(contentDescs[0]);
        previousSelectedPosition = 0;

        mViewPager.setAdapter(new MyAdapater());

        mViewPager.setCurrentItem(5000000); // 设置到某个位置

    }

    // 滚动时调用
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    // 新的条目被选中时调用
    @Override
    public void onPageSelected(int position) {
        int newPosition=position%imageViewList.size();
        tv_word.setText(contentDescs[newPosition]);//显示对应的文字


        llPoints_container.getChildAt(previousSelectedPosition).setEnabled(false);
        llPoints_container.getChildAt(newPosition).setEnabled(true);

        previousSelectedPosition=newPosition;

    }

    // 滚动状态变化时调用
    @Override
    public void onPageScrollStateChanged(int state) {

    }


    private class MyAdapater extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }


        @Override
        public boolean isViewFromObject(View view, Object object) {
            // 当划到新的条目, 又返回来, view是否可以被复用.
            // 返回判断规则
            return (view==object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int newPosition = position % imageViewList.size();

            ImageView imageView=imageViewList.get(newPosition);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //object才是需要销毁的对象。
            container.removeView((View)object);

        }
    }
}
