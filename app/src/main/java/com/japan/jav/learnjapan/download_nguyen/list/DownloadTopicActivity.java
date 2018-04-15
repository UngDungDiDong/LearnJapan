package com.japan.jav.learnjapan.download_nguyen.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.japan.jav.learnjapan.R;
import com.japan.jav.learnjapan.download_nguyen.detail.DetailTopicActivity;
import com.japan.jav.learnjapan.download_nguyen.list.adapter.DownloadTopicAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by matas on 3/17/18.
 */

public class DownloadTopicActivity extends AppCompatActivity {
    @BindView(R.id.rv_toppic)
    RecyclerView rvTopic;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private DownloadTopicAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topbar_recycleview_nguyen);
        ButterKnife.bind(this);
        initData();
        initListener();
    }

    private void initListener() {
        adapter.setOnItemClickListener(new DownloadTopicAdapter.OnItemClickListener() {
            @Override
            public void onClick() {
                Intent intent = new Intent(DownloadTopicActivity.this, DetailTopicActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        mToolbar.setTitle("Topic");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("A");
        list.add("A");
        list.add("A");
        list.add("A");
        adapter = new DownloadTopicAdapter(this, list);
        rvTopic.setLayoutManager(new LinearLayoutManager(this));
        rvTopic.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
