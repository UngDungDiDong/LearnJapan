package com.japan.jav.learnjapan.home_navigation_nhi_tam.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.japan.jav.learnjapan.home_navigation_nhi_tam.fragment.KanjiFragment;
import com.japan.jav.learnjapan.home_navigation_nhi_tam.fragment.MojiFragment;

/**
 * Created by tamlv on 3/29/18.
 */

public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {
    private final static String [] FRAGMENT_TITLE = new String[]{"Moji", "Kanji"};

    public HomeFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new MojiFragment();
            case 1:
                return new KanjiFragment();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return FRAGMENT_TITLE[0];
            case 1:
                return FRAGMENT_TITLE[1];
            default:
                return "";
        }
    }

    @Override
    public int getCount() {
        return FRAGMENT_TITLE.length;
    }
}
