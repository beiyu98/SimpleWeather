package com.project.android.bruce.simpleweather.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.project.android.bruce.simpleweather.R;
import com.project.android.bruce.simpleweather.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shuai on 2016/2/25.
 * ViewPager引导的活动
 */
public class GuideActivity extends AppCompatActivity {


    private ViewPager viewPager;//创建引导页对象
    private ViewPagerAdapter vpAdapter;
    private List<View> viewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_layout);

        initViews();
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

    }
}
