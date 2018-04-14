package com.japan.jav.learnjapan.add_vocab_thanh.customView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.japan.jav.learnjapan.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by thanh on 4/13/2018.
 */

public class VocabView extends LinearLayout {
    private Context mContext;

    public VocabView(Context context) {
        super(context);
        this.mContext = context;
        View contactView = LayoutInflater.from(mContext)
                .inflate(R.layout.item_moji_thanh, null);
        ButterKnife.bind(this, contactView);




        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(contactView, layoutParams);
    }

}
