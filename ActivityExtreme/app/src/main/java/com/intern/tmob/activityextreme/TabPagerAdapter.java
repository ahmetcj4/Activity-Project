package com.intern.tmob.activityextreme;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class TabPagerAdapter extends PagerAdapter {
    Context mContext;
    View[] mViews;

    TabPagerAdapter(Context c,View[] views){
        mContext = c;
        mViews = views;
    }

    @Override
    public int getCount() {
        return mViews.length;
    }


    @Override
    public boolean isViewFromObject(View view, Object o) {
        return o == view;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return (String)mViews[position].getTag();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mViews[position]);
        return mViews[position];
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
