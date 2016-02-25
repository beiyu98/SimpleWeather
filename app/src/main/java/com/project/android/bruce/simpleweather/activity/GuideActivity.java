package com.project.android.bruce.simpleweather.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.project.android.bruce.simpleweather.R;
import com.project.android.bruce.simpleweather.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shuai on 2016/2/25.
 * ViewPager引导的活动
 */
public class GuideActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {


    private ViewPager viewPager;//创建引导页对象
    private ViewPagerAdapter vpAdapter;
    private List<View> viewList;

    //创建引导页中显示页面位置的点
    private ImageView[] dots;
    private int[] dts = {R.id.point1,R.id.point2,R.id.point3};

    //进入主界面按钮
    private Button startBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_layout);

        initViews();
        initDots();
    }

    private void initDots() {
        dots = new ImageView[viewList.size()];//将点图片数量和引导页数量设置为一致
        for (int i = 0; i < viewList.size(); i++) {
            dots[i]= (ImageView) findViewById(dts[i]);
        }
    }

    private void initViews() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        viewList = new ArrayList<>();
        viewList.add(layoutInflater.inflate(R.layout.one_layout,null));
        viewList.add(layoutInflater.inflate(R.layout.two_layout,null));
        viewList.add(layoutInflater.inflate(R.layout.three_layout,null));

        vpAdapter = new ViewPagerAdapter(viewList,this);
        viewPager = (ViewPager) findViewById(R.id.guide_view_pager);
        viewPager.setAdapter(vpAdapter);

        startBtn = (Button) viewList.get(2).findViewById(R.id.start);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuideActivity.this,MainActivity.class));
                finish();//将当前页面关闭掉，进入主界面，减少内存的占用
            }
        });
        viewPager.addOnPageChangeListener(this); //监听ViewPager的页面，设置点的类型

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    //监听ViewPager的页面，设置点的类型
    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < viewList.size(); i++) {
            if (position == i){
                dots[i].setImageResource(R.drawable.point_light);
            }else {
                dots[i].setImageResource(R.drawable.point_dark);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
