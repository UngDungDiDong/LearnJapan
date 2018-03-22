package com.japan.jav.learnjapan.test_feature_khang_duc.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.japan.jav.learnjapan.R;

/**
 * Created by huuduc on 15/03/2018.
 */

public class TestActivity extends AppCompatActivity{

    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_khang_duc);

        addControls();
    }

    private void addControls() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
