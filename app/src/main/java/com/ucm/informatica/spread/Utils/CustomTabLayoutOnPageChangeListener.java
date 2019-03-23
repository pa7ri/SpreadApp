package com.ucm.informatica.spread.Utils;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class CustomTabLayoutOnPageChangeListener implements TabLayout.OnTabSelectedListener {

    private ViewPager viewPager;
    private FragmentPagerAdapter fragmentPagerAdapter;

    public CustomTabLayoutOnPageChangeListener(ViewPager viewPager, FragmentPagerAdapter fragmentPagerAdapter) {
        this.viewPager = viewPager;
        this.fragmentPagerAdapter = fragmentPagerAdapter;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if(tab.getPosition() == 3) {// force historial start
            viewPager.setCurrentItem(3);
            fragmentPagerAdapter.getItem(3).onStart();
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

}
