package com.example.codetribe.thusang;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by codetribe on 2017/08/31.
 */

public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

    public MyViewPagerAdapter() {}

    @Override
    public int getCount() {
        return 0;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}

