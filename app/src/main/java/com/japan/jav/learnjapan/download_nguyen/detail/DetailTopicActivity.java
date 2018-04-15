package com.japan.jav.learnjapan.download_nguyen.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.japan.jav.learnjapan.R;
import com.japan.jav.learnjapan.download_nguyen.detail.adapter.DetailTopicAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by thanh on 4/13/2018.
 */

public class DetailTopicActivity extends AppCompatActivity {
    @BindView(R.id.rv_toppic)
    RecyclerView rvTopic;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.imv_heart)
    ImageView imvHeart;

    private DetailTopicAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topbar_recycleview_nguyen);
        ButterKnife.bind(this);
        initData();
        initListener();
    }

    private void initListener() {

    }

    private void initData() {
        mToolbar.setTitle("Detail Topic");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imvHeart.setVisibility(View.VISIBLE);

        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("A");
        list.add("A");
        list.add("A");
        list.add("A");
        adapter = new DetailTopicAdapter(this, list);
        rvTopic.setLayoutManager(new LinearLayoutManager(this));
        rvTopic.setAdapter(adapter);
    }
}
