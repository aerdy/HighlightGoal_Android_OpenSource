package com.necisstudio.highlightgoal.adapter;

/**
 * Created by jarod on 12/15/14.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class Adapter_ViewPager extends FragmentStatePagerAdapter {
    List<Fragment> fList ;
    public Adapter_ViewPager(FragmentManager fm, List<Fragment> fList) {
        super(fm);
        this.fList = fList;
    }

    @Override
    public int getCount() {
        return this.fList.size();
    }
    @Override
    public Fragment getItem(int position) {
        return this.fList.get(position);
    }
}