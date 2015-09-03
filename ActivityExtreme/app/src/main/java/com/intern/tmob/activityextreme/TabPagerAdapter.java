package com.intern.tmob.activityextreme;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TabPagerAdapter extends PagerAdapter {
    String[] mTitles;
    Context mContext;

    TabPagerAdapter(Context c, String[] titles){
        mContext = c;
        mTitles = titles;
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }


    @Override
    public boolean isViewFromObject(View view, Object o) {
        return o == view;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.pager_item,
                container, false);
        container.addView(view);
        TextView title = (TextView) view.findViewById(R.id.item_title);
        title.setText(mTitles[position]);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
