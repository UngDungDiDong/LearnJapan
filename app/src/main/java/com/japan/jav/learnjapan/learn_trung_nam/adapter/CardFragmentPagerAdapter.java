package com.japan.jav.learnjapan.learn_trung_nam.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.japan.jav.learnjapan.learn_trung_nam.fragment.CardKanjiFragment;
import com.japan.jav.learnjapan.learn_trung_nam.fragment.CardMojiFragment;
import com.japan.jav.learnjapan.model.DataTypeEnum;
import com.japan.jav.learnjapan.model.Kanji;
import com.japan.jav.learnjapan.model.Moji;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trungnguyeen on 1/14/18.
 */

public class CardFragmentPagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = CardFragmentPagerAdapter.class.getSimpleName();
    private List<?> mFragments = new ArrayList<>();
    private ArrayList<Kanji> kanList = new ArrayList<>();
    private ArrayList<Moji> mojiList = new ArrayList<>();
    private DataTypeEnum dataTypeEnum;
    public CardFragmentPagerAdapter(FragmentManager fm, DataTypeEnum dataTypeEnum) {
        super(fm);
        this.dataTypeEnum = dataTypeEnum;
        createCardList();
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    public void setKanList(ArrayList<Kanji> kanList) {
        this.kanList = kanList;
    }

    public void setMojiList(ArrayList<Moji> mojiList) {
        this.mojiList = mojiList;
    }

    @Override
    public Fragment getItem(int position) {
        return (Fragment) mFragments.get(position);
    }

    public void createCardList(){
        if(dataTypeEnum == DataTypeEnum.MOJI){
            ArrayList<CardMojiFragment> fragments = new ArrayList<>();
            for(int i = 0; i< this.mojiList.size(); i++){
                CardMojiFragment cardFrag = new CardMojiFragment();
                cardFrag.setItem((Moji) this.mojiList.get(i));
                fragments.add(cardFrag);
            }
            this.mFragments = fragments;
        }
        else{
            ArrayList<CardKanjiFragment> fragments = new ArrayList<>();
            for(int i = 0; i< this.kanList.size(); i++){
                CardKanjiFragment cardFrag = new CardKanjiFragment();
                cardFrag.setItem((Kanji) this.kanList.get(i));
                fragments.add(cardFrag);
            }
            this.mFragments = fragments;
        }

    }
}
