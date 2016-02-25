package com.project.android.bruce.simpleweather.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by shuai on 2016/2/25.
 *
 * ViewPager的引导页adapter
 */
public class ViewPagerAdapter extends PagerAdapter {

    private List<View> viewList;
    private Context context;

    public ViewPagerAdapter(List<View> viewList, Context context) {
        this.viewList = viewList;
        this.context = context;
    }

    //在view不需要时进行销毁
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO: 2016/2/25 看此处是否需要强制转换container
        container.removeView(viewList.get(position));//调用container的removeView方法移除view
    }

    //获取view实例
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO: 2016/2/25 看此处是否需要强制转换container
        container.addView(viewList.get(position));
        return viewList.get(position);
    }

    //获取ViewPager的数量
    @Override
    public int getCount() {
        return viewList.size();
    }

    //判断当前的view是否是我们需要的object
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }
}
