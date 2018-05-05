package com.japan.jav.learnjapan.learn_trung_nam.adapter;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.japan.jav.learnjapan.learn_trung_nam.fragment.CardKanjiFragment;
import com.japan.jav.learnjapan.learn_trung_nam.fragment.CardMojiFragment;
import com.japan.jav.learnjapan.model.DataTypeEnum;
import com.japan.jav.learnjapan.model.Kanji;
import com.japan.jav.learnjapan.model.Moji;
import com.japan.jav.learnjapan.model.Set;

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
    private Set set;
    private DataTypeEnum dataTypeEnum;
    private Context context;
    public CardFragmentPagerAdapter(FragmentManager fm, DataTypeEnum dataTypeEnum, Context context) {
        super(fm);
        this.context = context;
        this.dataTypeEnum = dataTypeEnum;
        createCardList();
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    public void setSet(Set set) {
        this.set = set;
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
                cardFrag.setSetId(this.set.getId());
                cardFrag.setContext(this.context);
                cardFrag.setItem((Moji) this.mojiList.get(i));
                fragments.add(cardFrag);
            }
            this.mFragments = fragments;
        }
        else{
            ArrayList<CardKanjiFragment> fragments = new ArrayList<>();
            for(int i = 0; i< this.kanList.size(); i++){
                CardKanjiFragment cardFrag = new CardKanjiFragment();
                cardFrag.setContext(this.context);
                cardFrag.setSetId(this.set.getId());
                cardFrag.setItem((Kanji) this.kanList.get(i));
                fragments.add(cardFrag);
            }
            this.mFragments = fragments;
        }

    }
}
